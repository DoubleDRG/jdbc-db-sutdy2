package study.buddyBook.service;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import study.buddyBook.connection.ConnectionConst;
import study.buddyBook.domain.Member;
import study.buddyBook.repository.MemberRepositoryV2;

import java.sql.DriverManager;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.*;
import static study.buddyBook.connection.ConnectionConst.*;

@Slf4j
class MemberServiceV2Test
{
    private MemberRepositoryV2 memberRepository;
    private MemberServiceV2 memberService;

    @BeforeEach
    void before()
    {
        DriverManagerDataSource dataSource = new DriverManagerDataSource(URL, USERNAME, PASSWORD);
        memberRepository = new MemberRepositoryV2(dataSource);
        memberService = new MemberServiceV2(dataSource, memberRepository);
    }

    @AfterEach
    void after() throws SQLException
    {
        memberRepository.delete("memberA");
        memberRepository.delete("memberB");
        memberRepository.delete("bad");
    }

    @Test
    @DisplayName("정상 이체")
    void accountTransfer() throws SQLException
    {
        Member memberA = new Member("memberA", 10000);
        Member memberB = new Member("memberB", 10000);
        memberRepository.save(memberA);
        memberRepository.save(memberB);

        memberService.accountTransfer(memberA.getMemberId(), memberB.getMemberId(), 2000);

        Member findMemberA = memberRepository.findById(memberA.getMemberId());
        Member findMemberB = memberRepository.findById(memberB.getMemberId());
        assertThat(findMemberA.getMoney()).isEqualTo(8000);
        assertThat(findMemberB.getMoney()).isEqualTo(12000);
    }

    @Test
    @DisplayName("이체 오류")
    void accountTransferError() throws SQLException
    {
        Member memberA = new Member("memberA", 10000);
        Member memberBad = new Member("bad", 10000);
        memberRepository.save(memberA);
        memberRepository.save(memberBad);

        assertThatThrownBy(() ->
        {
            memberService.accountTransfer(memberA.getMemberId(), memberBad.getMemberId(), 2000);
        }).isInstanceOf(IllegalStateException.class);

        Member findMemberA = memberRepository.findById(memberA.getMemberId());
        Member findMemberB = memberRepository.findById(memberBad.getMemberId());
        assertThat(findMemberA.getMoney()).isEqualTo(10000);
        assertThat(findMemberB.getMoney()).isEqualTo(10000);
    }
}
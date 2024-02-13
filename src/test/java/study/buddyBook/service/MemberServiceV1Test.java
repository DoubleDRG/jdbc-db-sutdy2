package study.buddyBook.service;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import study.buddyBook.domain.Member;
import study.buddyBook.repository.MemberRepositoryV1;

import static org.assertj.core.api.Assertions.*;
import static study.buddyBook.connection.ConnectionConst.*;

@Slf4j
class MemberServiceV1Test
{
    public static final String MEMBER_A = "memberA";
    public static final String MEMBER_B = "memberB";
    public static final String MEMBER_BAD = "bad";

    private MemberRepositoryV1 memberRepository;
    private MemberServiceV1 memberService;

    @BeforeEach
    void before()
    {
        DriverManagerDataSource dataSource = new DriverManagerDataSource(URL, USERNAME, PASSWORD);
        memberRepository = new MemberRepositoryV1(dataSource);
        memberService = new MemberServiceV1(memberRepository);
    }

    @AfterEach
    void after()
    {
        memberRepository.delete(MEMBER_A);
        memberRepository.delete(MEMBER_B);
        memberRepository.delete(MEMBER_BAD);
    }

    @Test
    @DisplayName("정상이체")
    void accountTransfer()
    {
        Member memberA = new Member(MEMBER_A, 10000);
        Member memberB = new Member(MEMBER_B, 10000);
        memberRepository.save(memberA);
        memberRepository.save(memberB);

        memberService.accountTransfer(memberA.getMemberId(), memberB.getMemberId(), 2000);

        memberA = memberRepository.findById(memberA.getMemberId());
        memberB = memberRepository.findById(memberB.getMemberId());
        assertThat(memberA.getMoney()).isEqualTo(8000);
        assertThat(memberB.getMoney()).isEqualTo(12000);
    }

    @Test
    @DisplayName("이체 중 예외 발생")
    void accountTransferError()
    {
        Member memberA = new Member(MEMBER_A, 10000);
        Member memberBad = new Member(MEMBER_BAD, 10000);
        memberRepository.save(memberA);
        memberRepository.save(memberBad);

        assertThatThrownBy(() ->
        {
            memberService.accountTransfer(memberA.getMemberId(), memberBad.getMemberId(), 2000);
        })
                .isInstanceOf(IllegalStateException.class);

        Member findMemberA = memberRepository.findById(memberA.getMemberId());
        Member findMemberBad = memberRepository.findById(memberBad.getMemberId());

        assertThat(findMemberA.getMoney()).isEqualTo(8000);
        assertThat(findMemberBad.getMoney()).isEqualTo(10000);
    }
}
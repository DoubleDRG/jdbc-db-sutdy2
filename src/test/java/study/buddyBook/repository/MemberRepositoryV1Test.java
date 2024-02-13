package study.buddyBook.repository;

import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import study.buddyBook.domain.Member;

import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.*;
import static study.buddyBook.connection.ConnectionConst.*;

@Slf4j
public class MemberRepositoryV1Test
{
    MemberRepositoryV1 memberRepository;

    @BeforeEach
    void beforeEach()
    {
        //1.Spring이 제공하는 DriverManagerDataSource를 사용한다.
        //DriverManagerDataSource dataSource = new DriverManagerDataSource(URL, USERNAME, PASSWORD);

        //2.Spring이 제공하는 HikariDataSource를 사용한다.
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(URL);
        dataSource.setUsername(USERNAME);
        dataSource.setPassword(PASSWORD);
        dataSource.setMaximumPoolSize(10);
        dataSource.setPoolName("doubleDrgPool");

        memberRepository = new MemberRepositoryV1(dataSource);
    }

    @Test
    void crud()
    {
        //save
        Member member = new Member("memberA", 10000);
        memberRepository.save(member);

        //findById
        Member findMember = memberRepository.findById(member.getMemberId());
        assertThat(findMember).isEqualTo(member);

        //update
        memberRepository.update(member.getMemberId(), 20000);
        findMember = memberRepository.findById(member.getMemberId());
        assertThat(findMember.getMoney()).isEqualTo(20000);

        //delete
        memberRepository.delete(member.getMemberId());
        assertThatThrownBy(() ->
        {
            memberRepository.findById(member.getMemberId());
        }).isInstanceOf(NoSuchElementException.class);
    }
}

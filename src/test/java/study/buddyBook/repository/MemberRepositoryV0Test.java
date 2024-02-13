package study.buddyBook.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import study.buddyBook.domain.Member;

import java.sql.SQLException;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.*;

class MemberRepositoryV0Test
{
    MemberRepositoryV0 memberRepository = new MemberRepositoryV0();

    @Test
    void crud() throws SQLException
    {
        Member member = new Member();
        member.setMemberId("memberA");
        member.setMoney(10000);

        //save
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
        assertThatThrownBy(() -> memberRepository.findById(member.getMemberId()))
                .isInstanceOf(NoSuchElementException.class);
    }
}
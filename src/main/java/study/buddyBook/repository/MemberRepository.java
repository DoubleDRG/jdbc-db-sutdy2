package study.buddyBook.repository;

import study.buddyBook.domain.Member;

import java.sql.SQLException;

public interface MemberRepository
{
    Member save(Member member);

    Member findById(String memberId) throws SQLException;

    void update(String memberId, int money);

    void delete(String memberId);
}

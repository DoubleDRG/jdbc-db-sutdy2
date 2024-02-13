package study.buddyBook.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import study.buddyBook.domain.Member;
import study.buddyBook.repository.MemberRepositoryV2;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Slf4j
@RequiredArgsConstructor
public class MemberServiceV2
{
    private final DataSource dataSource;
    private final MemberRepositoryV2 memberRepository;

    public void accountTransfer(String fromId, String toId, int money) throws SQLException
    {
        Connection con = dataSource.getConnection();
        try
        {
            //트랜잭션 시작
            con.setAutoCommit(false);
            bizLogic(con, fromId, toId, money);
            //트랜잭션 정상종료
            con.commit();
        }
        catch (Exception e)
        {
            //트랜잭션 오류종료
            con.rollback();
            throw new IllegalStateException(e);
        }
        finally
        {
            //커넥션 반납
            release(con);
        }
    }

    public void bizLogic(Connection con, String fromId, String toId, int money) throws SQLException
    {
        Member fromMember = memberRepository.findById(con, fromId);
        Member toMember = memberRepository.findById(con, toId);

        memberRepository.update(con, fromId, fromMember.getMoney() - money);
        validation(toMember);
        memberRepository.update(con, toId, toMember.getMoney() + money);
    }

    private void validation(Member toMember)
    {
        if (toMember.getMemberId().equals("bad"))
        {
            throw new IllegalStateException("이체 중 예외 발생");
        }
    }

    private void release(Connection con)
    {
        if (con != null)
        {
            try
            {
                //커넥션을 다시 AutoCommit모드로 전환 후 반납해야 한다.
                con.setAutoCommit(true);
                con.close();
            }
            catch (SQLException e)
            {
                log.info("error", e);
            }
        }
    }
}

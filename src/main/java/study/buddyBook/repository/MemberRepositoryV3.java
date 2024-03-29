package study.buddyBook.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.stereotype.Repository;
import study.buddyBook.domain.Member;

import javax.sql.DataSource;
import java.sql.*;
import java.util.NoSuchElementException;

//트랜잭션 매니저
@Slf4j
public class MemberRepositoryV3
{
    private final DataSource dataSource;

    public MemberRepositoryV3(DataSource dataSource)
    {
        this.dataSource = dataSource;
    }

    public Member save(Member member) throws SQLException
    {
        String sql = "insert into member(member_id, money) values(?,?)";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try
        {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, member.getMemberId());
            pstmt.setInt(2, member.getMoney());
            pstmt.executeUpdate();
            return member;
        }
        catch (SQLException e)
        {
            throw e;
        }
        finally
        {
            close(con, pstmt, rs);
        }
    }

    public Member findById(String memberId) throws SQLException
    {
        String sql = "select * from member where member_id=?";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try
        {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, memberId);
            rs = pstmt.executeQuery();

            if (rs.next())
            {
                Member member = new Member();
                member.setMemberId(rs.getString("member_id"));
                member.setMoney(rs.getInt("money"));
                return member;
            } else
            {
                throw new NoSuchElementException("member not found memberId=" + memberId);
            }
        }
        catch (SQLException e)
        {
            throw e;
        }
        finally
        {
            close(con, pstmt, rs);
        }
    }

    public void update(String member_id, int money) throws SQLException
    {
        String sql = "update member set money=? where member_id=?";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try
        {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, money);
            pstmt.setString(2, member_id);
            pstmt.executeUpdate();
        }
        catch (SQLException e)
        {
            throw e;
        }
        finally
        {
            close(con, pstmt, rs);
        }
    }

    public void delete(String memberId) throws SQLException
    {
        String sql = "delete from member where member_id=?";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try
        {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, memberId);
            pstmt.executeUpdate();
        }
        catch (SQLException e)
        {
            throw e;
        }
        finally
        {
            close(con, pstmt, rs);
        }
    }

    //커넥션 획득
    private Connection getConnection() throws SQLException
    {
        //트랜잭션 동기화를 사용하려면 DataSourceUtils를 사용해야 한다.
        //트랜잭션 동기화 매니저가 관리하는 커넥션이 있으면 그 커넥션을 반환한다.
        //트랜잭션 동기화 매니저가 관리하는 커넥션이 없으면 새로운 커넥션을 생성해서 반환한다.
        return DataSourceUtils.getConnection(dataSource);
    }

    //커넥션 반납
    private void close(Connection con, Statement stmt, ResultSet rs)
    {
        JdbcUtils.closeResultSet(rs);
        JdbcUtils.closeStatement(stmt);
        //트랜잭션 동기화를 사용하려면 DataSourceUtils를 사용해야 한다.
        //트랜잭션을 사용하기 위해 동기화된 커넥션은 커넥션을 닫지 않고 그대로 유지한다.
        //트랜잭션 동기화 매니저가 관리하는 커넥션이 없는 경우 해당 커넥션을 닫는다.
        DataSourceUtils.releaseConnection(con, dataSource);
    }
}

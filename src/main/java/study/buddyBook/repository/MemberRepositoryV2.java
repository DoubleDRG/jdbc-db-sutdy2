package study.buddyBook.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.support.JdbcUtils;
import study.buddyBook.domain.Member;

import javax.sql.DataSource;
import java.sql.*;
import java.util.NoSuchElementException;

//트랜잭션을 고려하여 커넥션을 파라미터방식으로 전달받는다.
@Slf4j
public class MemberRepositoryV2
{
    private final DataSource dataSource;

    public MemberRepositoryV2(DataSource dataSource)
    {
        this.dataSource = dataSource;
    }

    public Member save(Member member) throws SQLException
    {
        String sql = "insert into member(member_id,money) values(?,?)";

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
                throw new NoSuchElementException("member not found memberId = " + memberId);
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

    public Member findById(Connection con, String memberId) throws SQLException
    {
        String sql = "select * from member where member_id=?";

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try
        {
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
                throw new NoSuchElementException("member not found memberId = " + memberId);
            }
        }
        catch (SQLException e)
        {
            throw e;
        }
        finally
        {
            JdbcUtils.closeResultSet(rs);
            JdbcUtils.closeStatement(pstmt);
        }

    }

    public void update(String memberId, int money) throws SQLException
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
            pstmt.setString(2, memberId);
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

    public void update(Connection con, String memberId, int money) throws SQLException
    {
        String sql = "update member set money=? where member_id=?";

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try
        {
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, money);
            pstmt.setString(2, memberId);
            pstmt.executeUpdate();
        }
        catch (SQLException e)
        {
            throw e;
        }
        finally
        {
            JdbcUtils.closeResultSet(rs);
            JdbcUtils.closeStatement(pstmt);
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

    //커넥션을 반납한다.
    private void close(Connection con, Statement stmt, ResultSet rs)
    {
        JdbcUtils.closeResultSet(rs);
        JdbcUtils.closeStatement(stmt);
        JdbcUtils.closeConnection(con);
    }

    //커넥션을 얻어온다.
    private Connection getConnection()
    {
        try
        {
            return dataSource.getConnection();
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }
}

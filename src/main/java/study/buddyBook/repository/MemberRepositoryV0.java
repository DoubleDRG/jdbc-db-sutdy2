package study.buddyBook.repository;

import lombok.extern.slf4j.Slf4j;
import study.buddyBook.connection.DBConnectionUtil;
import study.buddyBook.domain.Member;

import java.sql.*;
import java.util.NoSuchElementException;

//JDBC DriverManager
@Slf4j
public class MemberRepositoryV0
{
    public Member save(Member member)
    {
        String sql = "insert into member(member_id,money) values(?,?)";

        Connection con = null;
        PreparedStatement pstmt = null;

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
            throw new RuntimeException(e);
        }
        finally
        {
            close(con, pstmt, null);
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
            log.error("db error", e);
            throw e;
        }
        finally
        {
            close(con, pstmt, rs);
        }
    }

    public void update(String memberId, int money)
    {
        String sql = "update member set money=? where member_id=?";

        Connection con = null;
        PreparedStatement pstmt = null;

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
            throw new RuntimeException(e);
        }
        finally
        {
            close(con, pstmt, null);
        }

    }

    public void delete(String memberId)
    {
        String sql = "delete from member where member_id=?";

        Connection con = null;
        PreparedStatement pstmt = null;

        try
        {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, memberId);
            pstmt.executeUpdate();
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
        finally
        {
            close(con, pstmt, null);
        }

    }

    //JDBC인터페이스의 구현체인 H2 Driver를 사용해서 Connection을 받아온다.
    private static Connection getConnection()
    {
        return DBConnectionUtil.getConnection();
    }

    private void close(Connection con, Statement stmt, ResultSet rs)
    {
        if (rs != null)
        {
            try
            {
                rs.close();
            }
            catch (SQLException e)
            {
                throw new RuntimeException(e);
            }
        }
        if (stmt != null)
        {
            try
            {
                stmt.close();
            }
            catch (SQLException e)
            {
                throw new RuntimeException(e);
            }
        }
        if (con != null)
        {
            try
            {
                con.close();
            }
            catch (SQLException e)
            {
                throw new RuntimeException(e);
            }
        }
    }
}

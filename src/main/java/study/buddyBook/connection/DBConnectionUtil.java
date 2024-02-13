package study.buddyBook.connection;

import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static study.buddyBook.connection.ConnectionConst.*;

//JDBC의 DriverManager가 Connection을 얻어온다.
@Slf4j
public class DBConnectionUtil
{
    public static Connection getConnection()
    {
        try
        {
            Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            log.info("get connection = {}, class = {}", connection, connection.getClass());
            return connection;
        }
        catch (SQLException e)
        {
            log.info("connection Error ");
            throw new RuntimeException(e);
        }
    }
}

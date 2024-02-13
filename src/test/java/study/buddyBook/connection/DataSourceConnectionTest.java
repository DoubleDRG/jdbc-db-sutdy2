package study.buddyBook.connection;

import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static study.buddyBook.connection.ConnectionConst.*;

@Slf4j
public class DataSourceConnectionTest
{
    //JDBC에서 제공하는 DriverManager로 커넥션 얻기
    //매번 새로운 Connection을 얻는다.
    //사용할 때마다 메타정보를 입력해야 한다.
    @Test
    void driverManager() throws SQLException
    {
        Connection con1 = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        Connection con2 = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        log.info("con1 ={} class={}", con1, con1.getClass());
        log.info("con2 ={} class={}", con2, con2.getClass());
    }

    //Spring에서 제공하는 DriverManagerDataSource로 커넥션 얻기
    //매번 새로운 Connection을 얻는다.
    //메타정보를 한 번만 입력한다. (설정과 사용의 분리)
    @Test
    void driverManagerDataSource() throws SQLException
    {
        DriverManagerDataSource dataSource = new DriverManagerDataSource(URL, USERNAME, PASSWORD);
        Connection con1 = dataSource.getConnection();
        Connection con2 = dataSource.getConnection();
        log.info("con1 ={} class={}", con1, con1.getClass());
        log.info("con2 ={} class={}", con2, con2.getClass());
    }

    //Spring에서 제공하는 HikariDataSource로 커넥션 얻기
    //Connection을 Connection풀에서 꺼낸다.
    @Test
    void HikariCpDataSource() throws SQLException, InterruptedException
    {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(URL);
        dataSource.setUsername(USERNAME);
        dataSource.setPassword(PASSWORD);
        dataSource.setMaximumPoolSize(10);
        dataSource.setPoolName("doubleDrgPool");

        Connection con1 = dataSource.getConnection();
        Connection con2 = dataSource.getConnection();

        //Connection풀에 Connection을 채우는 과정은 별도의 쓰레드에서 작동한다.
        //Connection이 Connection풀에 채워지기 전에 테스트가 끝나는 것을 방지한다.
        Thread.sleep(1000);

        log.info("con1 ={} class={}", con1, con1.getClass());
        log.info("con2 ={} class={}", con2, con2.getClass());
    }
}

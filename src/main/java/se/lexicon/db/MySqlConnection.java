package se.lexicon.db;

import com.mysql.cj.jdbc.MysqlDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class MySqlConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/event_manager";
    private static final String USER = "root";
    private static final String PASSWORD = "Orebrolan@07";

    // Private constructor to prevent instantiation
    private MySqlConnection() {
    }

    private static DataSource mysqlDataSource;

    // This method provides a DataSource, which is a connection provider.
    // Instead of creating a new connection every time, it allows connections to be merged and reused.
    public static DataSource getMysqlDataSource() {
        if (mysqlDataSource == null) {

            // Used MysqlDataSource which is non-pooled and no need to configure connection pooling.
            MysqlDataSource ds = new MysqlDataSource();
            ds.setURL(URL);
            ds.setUser(USER);
            ds.setPassword(PASSWORD);
            mysqlDataSource = ds;
        }
        return mysqlDataSource;
    }
}

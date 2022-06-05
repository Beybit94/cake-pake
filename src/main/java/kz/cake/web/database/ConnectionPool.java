package kz.cake.web.database;

import java.sql.Connection;

public interface ConnectionPool {
    void init();
    Connection getConnection();
    boolean releaseConnection(Connection connection);
    String getUrl();
    String getUser();
    String getPassword();
}

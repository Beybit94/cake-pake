package kz.cake.web.database;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BasicConnectionPool implements ConnectionPool {
    private static final int MAX_TIMEOUT = 5;
    private static final int MAX_POOL_SIZE = 10;
    private static final int INITIAL_POOL_SIZE = 5;
    public static ConnectionPool Instance;
    private final Map<String, String> env = System.getenv();
    private final Logger logger = LogManager.getLogger(BasicConnectionPool.class.getName());
    private List<Connection> connectionPool;
    private List<Connection> usedConnections = new ArrayList<>();

    public BasicConnectionPool() {
        init();
    }

    private static Connection createConnection(String url, String user, String password) throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    public static ConnectionPool connect() {
        if (Instance == null) {
            Instance = new BasicConnectionPool();
        }

        return Instance;
    }

    @Override
    public void init() {
        initPool();
    }

    private void initPool() {
        try {
            Class.forName("org.postgresql.Driver").getDeclaredConstructor().newInstance();
            connectionPool = new ArrayList<>(INITIAL_POOL_SIZE);
            for (int i = 0; i < INITIAL_POOL_SIZE; i++) {
                connectionPool.add(createConnection(getUrl(), getUser(), getPassword()));
                logger.info(String.format("Pool [%d] successfully added", i));
            }
        } catch (Exception e) {
            logger.error(e);
        }

    }

    @Override
    public Connection getConnection() {
        if (connectionPool.isEmpty()) {
            try {
                if (usedConnections.size() > MAX_POOL_SIZE)
                    throw new RuntimeException("Maximum pool size reached, no available connections!");
                connectionPool.add(createConnection(getUrl(), getUser(), getPassword()));
            } catch (SQLException e) {
                logger.error(e);
            }
        }

        Connection connection = connectionPool.remove(connectionPool.size() - 1);
        try {
            if (!connection.isValid(MAX_TIMEOUT)) {
                connection = createConnection(getUrl(), getUser(), getPassword());
            }
        } catch (SQLException e) {
            logger.error(e);
        }

        usedConnections.add(connection);
        return connection;
    }

    @Override
    public boolean releaseConnection(Connection connection) {
        connectionPool.add(connection);
        return usedConnections.remove(connection);
    }

    @Override
    public String getUrl() {
        return env.get("url");
    }

    @Override
    public String getUser() {
        return env.get("username");
    }

    @Override
    public String getPassword() {
        return env.get("password");
    }
}

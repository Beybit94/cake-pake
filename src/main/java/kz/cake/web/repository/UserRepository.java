package kz.cake.web.repository;

import kz.cake.web.database.BasicConnectionPool;
import kz.cake.web.entity.User;
import kz.cake.web.repository.base.BaseRepository;
import kz.cake.web.service.UserService;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserRepository extends BaseRepository<User> {
    private final Logger logger = LogManager.getLogger(UserRepository.class);

    public UserRepository() {
    }

    public Optional<User> findUserByName(String userName) {
        User user = null;
        Connection connection = BasicConnectionPool.Instance.getConnection();
        try {
            try (PreparedStatement preparedStatement = connection.prepareStatement("select * from web.users where username=?")) {
                preparedStatement.setString(1, userName);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    user = new User.Builder()
                            .id(resultSet.getLong("id"))
                            .active(resultSet.getBoolean("active"))
                            .name(resultSet.getString("username"))
                            .password(resultSet.getString("password"))
                            .sex(resultSet.getString("sex"))
                            .address(resultSet.getString("address"))
                            .build();
                }
            }
        } catch (Exception e) {
            logger.error(e);
        } finally {
            BasicConnectionPool.Instance.releaseConnection(connection);
        }

        return Optional.ofNullable(user);
    }

    @Override
    public List<User> getAll() {
        List<User> userList = new ArrayList<>();
        Connection connection = BasicConnectionPool.Instance.getConnection();
        try {
            try (PreparedStatement preparedStatement = connection.prepareStatement("select * from web.users order by id")) {
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    userList.add(new User.Builder()
                            .id(resultSet.getLong("id"))
                            .active(resultSet.getBoolean("active"))
                            .name(resultSet.getString("username"))
                            .password(resultSet.getString("password"))
                            .sex(resultSet.getString("sex"))
                            .address(resultSet.getString("address"))
                            .build());
                }
            }
        } catch (Exception e) {
            logger.error(e);
        } finally {
            BasicConnectionPool.Instance.releaseConnection(connection);
        }
        return userList;
    }
}

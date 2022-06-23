package kz.cake.web.repository;

import kz.cake.web.database.BasicConnectionPool;
import kz.cake.web.entity.UserRole;
import kz.cake.web.repository.base.BaseRepository;
import kz.cake.web.service.UserService;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class UserRoleRepository extends BaseRepository<UserRole> {
    private final Logger logger = LogManager.getLogger(UserRoleRepository.class);

    public UserRoleRepository() {
    }

    public List<UserRole> findByUserId(Long userId) {
        List<UserRole> userRoles = new ArrayList<>();
        Connection connection = BasicConnectionPool.Instance.getConnection();
        try {
            try (PreparedStatement preparedStatement = connection.prepareStatement("select * from web.user_roles where user_id=?")) {
                preparedStatement.setLong(1, userId);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    UserRole userRole = new UserRole(resultSet.getLong("user_id"), resultSet.getLong("role_id"));
                    userRoles.add(userRole);
                }
            }
        } catch (Exception e) {
            logger.error(e);
        } finally {
            BasicConnectionPool.Instance.releaseConnection(connection);
        }

        return userRoles;
    }
}

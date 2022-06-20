package kz.cake.web.service;

import kz.cake.web.database.BasicConnectionPool;
import kz.cake.web.entity.Role;
import kz.cake.web.repository.RoleRepository;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Optional;

public class RoleService extends BaseService<Role, RoleRepository> {
    private final Logger logger = LogManager.getLogger(UserService.class);

    public RoleService() {
        this.repository = new RoleRepository();
    }

    public Optional<Role> findByCode(String code){
        Role role = null;
        Connection connection = BasicConnectionPool.Instance.getConnection();
        try {
            try (PreparedStatement preparedStatement = connection.prepareStatement("select * from web.roles where code=?")) {
                preparedStatement.setString(1, code);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    role = new Role.Builder()
                            .id(resultSet.getLong("id"))
                            .active(resultSet.getBoolean("active"))
                            .code(resultSet.getString("code"))
                            .build();
                }
            }
        } catch (Exception e) {
            logger.error(e);
        } finally {
            BasicConnectionPool.Instance.releaseConnection(connection);
        }

        return Optional.ofNullable(role);
    }

    public Role getById(Long id){
        Role role = null;
        Connection connection = BasicConnectionPool.Instance.getConnection();
        try {
            try (PreparedStatement preparedStatement = connection.prepareStatement("select * from web.roles where id=?")) {
                preparedStatement.setLong(1, id);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    role = new Role.Builder()
                            .id(resultSet.getLong("id"))
                            .active(resultSet.getBoolean("active"))
                            .code(resultSet.getString("code"))
                            .build();
                }
            }
        } catch (Exception e) {
            logger.error(e);
        } finally {
            BasicConnectionPool.Instance.releaseConnection(connection);
        }

        return role;
    }
}

package kz.cake.web.repository;

import kz.cake.web.entity.Role;
import kz.cake.web.repository.base.DictionaryRepository;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class RoleRepository extends DictionaryRepository<Role> {
    private final Logger logger = LogManager.getLogger(RoleRepository.class);

    public RoleRepository() {
        supplier = () -> new Role();
    }

   /* @Override
    public Optional<Role> findByCode(String code) {
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

    @Override
    public Role getById(Long id) {
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

    @Override
    public List<Role> getAll() {
        List<Role> list = new ArrayList<>();
        Connection connection = BasicConnectionPool.Instance.getConnection();
        try {
            try (PreparedStatement preparedStatement = connection.prepareStatement("select * from web.roles where active=true order by id")) {
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    list.add(new Role.Builder()
                            .id(resultSet.getLong("id"))
                            .active(resultSet.getBoolean("active"))
                            .code(resultSet.getString("code"))
                            .build());
                }
            }
        } catch (Exception e) {
            logger.error(e);
        } finally {
            BasicConnectionPool.Instance.releaseConnection(connection);
        }
        return list;
    }*/
}

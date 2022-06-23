package kz.cake.web.repository;

import kz.cake.web.database.BasicConnectionPool;
import kz.cake.web.entity.Local;
import kz.cake.web.helpers.constants.CurrentSession;
import kz.cake.web.repository.base.BaseRepository;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LocalRepository extends BaseRepository<Local> {
    private final Logger logger = LogManager.getLogger(RoleRepository.class);

    public LocalRepository() {
    }

    public Local getByCode(String code) {
        Local local = null;
        Connection connection = BasicConnectionPool.Instance.getConnection();
        try {
            try (PreparedStatement preparedStatement = connection.prepareStatement("select * from web.local where code=? and language_id=?")) {
                preparedStatement.setString(1, code);
                preparedStatement.setLong(2, CurrentSession.Instance.getCurrentLanguageId());
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    local = new Local.Builder()
                            .id(resultSet.getLong("id"))
                            .active(resultSet.getBoolean("active"))
                            .code(resultSet.getString("code"))
                            .message(resultSet.getString("message"))
                            .build();
                }
            }
        } catch (Exception e) {
            logger.error(e);
        } finally {
            BasicConnectionPool.Instance.releaseConnection(connection);
        }

        return local;
    }
}

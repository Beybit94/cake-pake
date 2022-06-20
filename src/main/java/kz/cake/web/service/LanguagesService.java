package kz.cake.web.service;

import kz.cake.web.database.BasicConnectionPool;
import kz.cake.web.entity.User;
import kz.cake.web.entity.system.Languages;
import kz.cake.web.repository.LanguagesRepository;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Optional;

public class LanguagesService extends BaseService<Languages, LanguagesRepository> {
    private final Logger logger = LogManager.getLogger(LanguagesService.class);

    public LanguagesService() {
        this.repository = new LanguagesRepository();
    }

    public Optional<Languages> findByCOde(String code){
        Languages languages = null;
        Connection connection = BasicConnectionPool.Instance.getConnection();
        try {
            try (PreparedStatement preparedStatement = connection.prepareStatement("select * from web.languages where code=?")) {
                preparedStatement.setString(1, code);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    languages = new Languages.Builder()
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

        return Optional.ofNullable(languages);
    }
}

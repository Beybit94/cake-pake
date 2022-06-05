package kz.cake.web;

import kz.cake.web.database.BasicConnectionPool;
import kz.cake.web.entity.base.Base;
import kz.cake.web.entity.base.BaseDictionary;
import kz.cake.web.entity.system.System;
import kz.cake.web.repository.base.BaseRepository;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.reflections.Reflections;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class RequestListener implements ServletRequestListener {
    private final Logger logger = LogManager.getLogger(this.getClass().getName());

    @Override
    public void requestInitialized(ServletRequestEvent sre) {
        BasicConnectionPool.connect();
        initSchema();
        initTables();
    }

    private void initSchema() {
        Connection connection = BasicConnectionPool.Instance.getConnection();
        try {
            connection.createStatement().execute("CREATE SCHEMA IF NOT EXISTS web");
        } catch (Exception e) {
            logger.error(e);
        } finally {
            BasicConnectionPool.connect().releaseConnection(connection);
        }
    }

    private void initTables() {
        List<String> systemPrimary = Arrays.asList("Languages", "Settings");
        List<String> systemSecondary = Arrays.asList("Local");

        List<String> users = Arrays.asList("User");
        List<String> products = Arrays.asList("Product");
        List<String> dictionaries = Arrays.asList("Role", "City", "OrderStatus", "ProductSize", "ProductCategory");

        Reflections reflections = new Reflections("kz.cake.web");
        Set<Class<? extends BaseRepository>> repositories = reflections.getSubTypesOf(BaseRepository.class);

        Connection connection = BasicConnectionPool.Instance.getConnection();
        try {
            reflections.getSubTypesOf(System.class).stream()
                    .filter(m -> systemPrimary.contains(m.getSimpleName()))
                    .forEach(object -> initTable(object, repositories));

            reflections.getSubTypesOf(System.class).stream()
                    .filter(m -> systemSecondary.contains(m.getSimpleName()))
                    .forEach(object -> initTable(object, repositories));

            reflections.getSubTypesOf(Base.class).stream()
                    .filter(m -> users.contains(m.getSimpleName()))
                    .forEach(object -> initTable(object, repositories));

            reflections.getSubTypesOf(BaseDictionary.class).stream()
                    .filter(m -> dictionaries.contains(m.getSimpleName()))
                    .forEach(object -> initTable(object, repositories));

            reflections.getSubTypesOf(Base.class).stream()
                    .filter(m -> products.contains(m.getSimpleName()))
                    .forEach(object -> initTable(object, repositories));

            reflections.getSubTypesOf(Base.class).stream()
                    .filter(m -> !systemPrimary.contains(m.getSimpleName()) &&
                            !systemSecondary.contains(m.getSimpleName()) &&
                            !users.contains(m.getSimpleName()) &&
                            !products.contains(m.getSimpleName()) &&
                            !dictionaries.contains(m.getSimpleName()))
                    .forEach(object -> initTable(object, repositories));

            connection.createStatement().execute("INSERT INTO web.settings (is_init_tables,is_demo_mode,demo_mode_datetime,active) VALUES (true, false, null,true)");
        } catch (SQLException e) {
            logger.error(e);
        }finally {
            BasicConnectionPool.connect().releaseConnection(connection);
        }
    }

    private void initTable(Class<? extends Base> object, Set<Class<? extends BaseRepository>> repositories) {
        try {
            String className = object.getSimpleName();
            Optional<Class<? extends BaseRepository>> result = repositories.stream()
                    .filter(repository -> repository.getSimpleName().equals(className + "Repository"))
                    .findFirst();
            if (result.isPresent()) {
                BaseRepository repository = result.get().getDeclaredConstructor().newInstance();
                Base model = object.getDeclaredConstructor().newInstance();
                repository.createTable(model);
            }
            Thread.sleep(1000);
        } catch (Exception e) {
            logger.error(e);
        }
    }
}

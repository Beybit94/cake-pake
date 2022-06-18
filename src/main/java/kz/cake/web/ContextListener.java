package kz.cake.web;

import kz.cake.web.database.BasicConnectionPool;
import kz.cake.web.service.SettingsService;
import org.apache.log4j.PropertyConfigurator;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.File;
import java.sql.Connection;


public class ContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent event) {
        ServletContext context = event.getServletContext();
        String log4jConfigFile = context.getInitParameter("log4j-config-location");
        String fullPath = context.getRealPath("") + File.separator + log4jConfigFile;
        PropertyConfigurator.configure(fullPath);
        BasicConnectionPool.connect();
        initSchema();

        SettingsService settingsService = new SettingsService();
        settingsService.init();
    }

    private void initSchema() {
        Connection connection = BasicConnectionPool.Instance.getConnection();
        try {
            connection.createStatement().execute("CREATE SCHEMA IF NOT EXISTS web");
        } catch (Exception e) {
           e.printStackTrace();
        } finally {
            BasicConnectionPool.connect().releaseConnection(connection);
        }
    }
}

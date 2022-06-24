package kz.cake.web.service;

import kz.cake.web.database.BasicConnectionPool;
import kz.cake.web.entity.*;
import kz.cake.web.entity.base.Base;
import kz.cake.web.entity.base.BaseDictionary;
import kz.cake.web.helpers.StringUtils;
import kz.cake.web.helpers.constants.Language;
import kz.cake.web.model.DictionaryDto;
import kz.cake.web.repository.SettingsRepository;
import kz.cake.web.repository.base.BaseRepository;
import kz.cake.web.service.base.BaseService;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.reflections.Reflections;

import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class SettingsService extends BaseService<Settings, SettingsRepository> {
    private final Logger logger = LogManager.getLogger(this.getClass().getName());

    public SettingsService() {
        this.repository = new SettingsRepository();
    }

    public Optional<Settings> getOne() {
        Settings settings = null;
        Connection connection = BasicConnectionPool.Instance.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement("select * from web.settings limit 1")) {
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                settings = new Settings.Builder()
                        .isInitTables(resultSet.getBoolean("is_init_tables"))
                        .isDemoMode(resultSet.getBoolean("is_demo_mode"))
                        .build();
            }
        } catch (Exception e) {
            logger.error(e);
        } finally {
            BasicConnectionPool.Instance.releaseConnection(connection);
        }

        return Optional.ofNullable(settings);
    }

    public void init() {
        getOne().ifPresentOrElse(
                s -> {
                    if (!s.getInitTables()) {
                        initTables();
                        try {
                            initData();
                        } catch (NoSuchAlgorithmException e) {
                            e.printStackTrace();
                        }
                    }
                },
                () -> {
                    initTables();
                    try {
                        initData();
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    }
                }
        );
    }

    private void initTables() {
        List<String> systemPrimary = Arrays.asList("Languages", "Settings");
        List<String> systemSecondary = Arrays.asList("Local");

        List<String> users = Arrays.asList("User");
        List<String> products = Arrays.asList("Product");
        List<String> dictionaries = Arrays.asList("Role", "City", "OrderStatus", "ProductSize", "ProductCategory");

        Reflections reflections = new Reflections("kz.cake.web");
        Set<Class<? extends BaseRepository>> repositories = reflections.getSubTypesOf(BaseRepository.class);

        reflections.getSubTypesOf(Base.class).stream()
                .filter(m -> systemPrimary.contains(m.getSimpleName()))
                .forEach(object -> initTable(object, repositories));

        reflections.getSubTypesOf(Base.class).stream()
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
            Thread.sleep(500);
        } catch (Exception e) {
            logger.error(e);
        }
    }

    private void initData() throws NoSuchAlgorithmException {
        LanguagesService languagesService = new LanguagesService();
        languagesService.save(new Languages.Builder().code(Language.en).active(true).build());
        languagesService.save(new Languages.Builder().code(Language.ru).active(true).build());

        Optional<Languages> en = languagesService.findByCode("en");
        Optional<Languages> ru = languagesService.findByCode("ru");

        LocalService localService = new LocalService();
        localService.save(new Local.Builder().code("admin").languageId(en.get().getId()).message("admin").build());
        localService.save(new Local.Builder().code("manager").languageId(en.get().getId()).message("manager").build());
        localService.save(new Local.Builder().code("user").languageId(en.get().getId()).message("user").build());
        localService.save(new Local.Builder().code("admin").languageId(ru.get().getId()).message("админ").build());
        localService.save(new Local.Builder().code("manager").languageId(ru.get().getId()).message("менеджер").build());
        localService.save(new Local.Builder().code("user").languageId(ru.get().getId()).message("пользователь").build());

        RoleService roleService = new RoleService();
        roleService.save(new Role.Builder().code("admin").active(true).build());
        roleService.save(new Role.Builder().code("manager").active(true).build());
        roleService.save(new Role.Builder().code("user").active(true).build());

        UserService userService = new UserService();
        userService.save(new User.Builder()
                .name("admin")
                .password(StringUtils.encryptPassword("admin"))
                .active(true)
                .build());

        Optional<DictionaryDto> adminRole = roleService.findByCode("admin");
        Optional<User> adminUser = userService.findUserByName("admin");
        UserRoleService userRoleService = new UserRoleService();
        userRoleService.save(new UserRole(adminUser.get().getId(), adminRole.get().getId()));

        save(new Settings.Builder()
                .isInitTables(true)
                .isDemoMode(false)
                .build());
    }
}

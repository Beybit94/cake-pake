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

        localService.save(new Local.Builder().code("new").languageId(en.get().getId()).message("New").build());
        localService.save(new Local.Builder().code("inprogress").languageId(en.get().getId()).message("In progress").build());
        localService.save(new Local.Builder().code("completed").languageId(en.get().getId()).message("Completed").build());
        localService.save(new Local.Builder().code("new").languageId(ru.get().getId()).message("Новый").build());
        localService.save(new Local.Builder().code("inprogress").languageId(ru.get().getId()).message("В процессе").build());
        localService.save(new Local.Builder().code("completed").languageId(ru.get().getId()).message("Завершен").build());

        OrderStatusService orderStatusService = new OrderStatusService();
        orderStatusService.save(new OrderStatus.Builder().code("new").active(true).build());
        orderStatusService.save(new OrderStatus.Builder().code("inprogress").active(true).build());
        orderStatusService.save(new OrderStatus.Builder().code("completed").active(true).build());

        localService.save(new Local.Builder().code("cake").languageId(en.get().getId()).message("cake").build());
        localService.save(new Local.Builder().code("cheesecake").languageId(en.get().getId()).message("cheesecake").build());
        localService.save(new Local.Builder().code("pie").languageId(en.get().getId()).message("pie").build());
        localService.save(new Local.Builder().code("cake").languageId(ru.get().getId()).message("торт").build());
        localService.save(new Local.Builder().code("cheesecake").languageId(ru.get().getId()).message("чизкейк").build());
        localService.save(new Local.Builder().code("pie").languageId(ru.get().getId()).message("пирог").build());

        ProductCategoryService productCategoryService = new ProductCategoryService();
        productCategoryService.save(new ProductCategory.Builder().code("cake").active(true).build());
        productCategoryService.save(new ProductCategory.Builder().code("cheesecake").active(true).build());
        productCategoryService.save(new ProductCategory.Builder().code("pie").active(true).build());

        localService.save(new Local.Builder().code("biscuit").languageId(en.get().getId()).message("biscuit").build());
        localService.save(new Local.Builder().code("bento").languageId(en.get().getId()).message("bento").build());
        localService.save(new Local.Builder().code("trifle").languageId(en.get().getId()).message("trifle").build());
        localService.save(new Local.Builder().code("whoopee").languageId(en.get().getId()).message("whoopee").build());
        localService.save(new Local.Builder().code("spanish").languageId(en.get().getId()).message("spanish").build());
        localService.save(new Local.Builder().code("classic").languageId(en.get().getId()).message("classic").build());
        localService.save(new Local.Builder().code("chocolate").languageId(en.get().getId()).message("chocolate").build());
        localService.save(new Local.Builder().code("meat").languageId(en.get().getId()).message("meat").build());
        localService.save(new Local.Builder().code("other").languageId(en.get().getId()).message("other").build());

        localService.save(new Local.Builder().code("biscuit").languageId(ru.get().getId()).message("бисквит").build());
        localService.save(new Local.Builder().code("bento").languageId(ru.get().getId()).message("бенто").build());
        localService.save(new Local.Builder().code("trifle").languageId(ru.get().getId()).message("трайфл").build());
        localService.save(new Local.Builder().code("whoopee").languageId(ru.get().getId()).message("вупипай").build());
        localService.save(new Local.Builder().code("spanish").languageId(ru.get().getId()).message("испанский").build());
        localService.save(new Local.Builder().code("classic").languageId(ru.get().getId()).message("классический").build());
        localService.save(new Local.Builder().code("chocolate").languageId(ru.get().getId()).message("шоколадный").build());
        localService.save(new Local.Builder().code("meat").languageId(ru.get().getId()).message("мясной").build());
        localService.save(new Local.Builder().code("other").languageId(ru.get().getId()).message("другое").build());

        productCategoryService.findByCode("cake").ifPresent(m->{
            productCategoryService.save(new ProductCategory.Builder().parent(m.getId()).code("biscuit").active(true).build());
            productCategoryService.save(new ProductCategory.Builder().parent(m.getId()).code("bento").active(true).build());
            productCategoryService.save(new ProductCategory.Builder().parent(m.getId()).code("trifle").active(true).build());
            productCategoryService.save(new ProductCategory.Builder().parent(m.getId()).code("whoopee").active(true).build());
            productCategoryService.save(new ProductCategory.Builder().parent(m.getId()).code("other").active(true).build());
        });
        productCategoryService.findByCode("cheesecake").ifPresent(m->{
            productCategoryService.save(new ProductCategory.Builder().parent(m.getId()).code("classic").active(true).build());
            productCategoryService.save(new ProductCategory.Builder().parent(m.getId()).code("chocolate").active(true).build());
            productCategoryService.save(new ProductCategory.Builder().parent(m.getId()).code("spanish").active(true).build());
            productCategoryService.save(new ProductCategory.Builder().parent(m.getId()).code("other").active(true).build());
        });
        productCategoryService.findByCode("pie").ifPresent(m->{
            productCategoryService.save(new ProductCategory.Builder().parent(m.getId()).code("classic").active(true).build());
            productCategoryService.save(new ProductCategory.Builder().parent(m.getId()).code("meat").active(true).build());
            productCategoryService.save(new ProductCategory.Builder().parent(m.getId()).code("other").active(true).build());
        });

        save(new Settings.Builder()
                .isInitTables(true)
                .isDemoMode(false)
                .build());
    }
}

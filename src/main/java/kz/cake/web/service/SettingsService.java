package kz.cake.web.service;

import kz.cake.web.database.BasicConnectionPool;
import kz.cake.web.entity.*;
import kz.cake.web.entity.base.Base;
import kz.cake.web.entity.base.BaseDictionary;
import kz.cake.web.helpers.StringUtils;
import kz.cake.web.helpers.constants.LocaleCodes;
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
import java.sql.SQLException;
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
                        } catch (NoSuchAlgorithmException | SQLException | IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                },
                () -> {
                    initTables();
                    try {
                        initData();
                    } catch (NoSuchAlgorithmException | SQLException | IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
        );
    }

    private void initTables() {
        List<String> systemPrimary = Arrays.asList("Languages", "Settings");
        List<String> systemSecondary = Arrays.asList("Local");

        List<String> users = Arrays.asList("User");
        List<String> products = Arrays.asList("Product","Order");
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

    private void initData() throws NoSuchAlgorithmException, SQLException, IllegalAccessException {

        //region Language
        LanguagesService languagesService = new LanguagesService();
        Languages en = languagesService.save(new Languages.Builder().code(LocaleCodes.languageEn.getName()).active(true).build());
        Languages ru = languagesService.save(new Languages.Builder().code(LocaleCodes.languageRu.getName()).active(true).build());
        //endregion

        //region Roles
        LocalService localService = new LocalService();
        localService.save(new Local.Builder().code("admin").languageId(en.getId()).message("admin").build());
        localService.save(new Local.Builder().code("manager").languageId(en.getId()).message("manager").build());
        localService.save(new Local.Builder().code("user").languageId(en.getId()).message("user").build());
        localService.save(new Local.Builder().code("admin").languageId(ru.getId()).message("админ").build());
        localService.save(new Local.Builder().code("manager").languageId(ru.getId()).message("менеджер").build());
        localService.save(new Local.Builder().code("user").languageId(ru.getId()).message("пользователь").build());

        RoleService roleService = new RoleService();
        Role adminRole = roleService.save(new Role.Builder().code("admin").active(true).build());
        Role managerRole = roleService.save(new Role.Builder().code("manager").active(true).build());
        Role userRole = roleService.save(new Role.Builder().code("user").active(true).build());
        //endregion

        //region User
        UserService userService = new UserService();
        UserRoleService userRoleService = new UserRoleService();

        User adminUser = userService.save(new User.Builder()
                .name("admin")
                .password(StringUtils.encryptPassword("admin"))
                .active(true)
                .build());
        userRoleService.save(new UserRole(adminUser.getId(), adminRole.getId()));

        User managerUser = userService.save(new User.Builder()
                .name("manager")
                .password(StringUtils.encryptPassword("test"))
                .active(true)
                .build());
        userRoleService.save(new UserRole(managerRole.getId(), managerUser.getId()));
        //endregion

        //region City
        localService.save(new Local.Builder().code("Almaty").languageId(en.getId()).message("Almaty").build());
        localService.save(new Local.Builder().code("Astana").languageId(en.getId()).message("Astana").build());
        localService.save(new Local.Builder().code("Almaty").languageId(ru.getId()).message("Алматы").build());
        localService.save(new Local.Builder().code("Astana").languageId(ru.getId()).message("Астана").build());

        CityService cityService = new CityService();
        cityService.save(new City.Builder().code("Almaty").active(true).build());
        cityService.save(new City.Builder().code("Astana").active(true).build());
        //endregion

        //region Order status
        localService.save(new Local.Builder().code("draft").languageId(en.getId()).message("Draft").build());
        localService.save(new Local.Builder().code("new").languageId(en.getId()).message("New").build());
        localService.save(new Local.Builder().code("inprogress").languageId(en.getId()).message("In progress").build());
        localService.save(new Local.Builder().code("completed").languageId(en.getId()).message("Completed").build());
        localService.save(new Local.Builder().code("draft").languageId(ru.getId()).message("Черновик").build());
        localService.save(new Local.Builder().code("new").languageId(ru.getId()).message("Новый").build());
        localService.save(new Local.Builder().code("inprogress").languageId(ru.getId()).message("В процессе").build());
        localService.save(new Local.Builder().code("completed").languageId(ru.getId()).message("Завершен").build());

        OrderStatusService orderStatusService = new OrderStatusService();
        orderStatusService.save(new OrderStatus.Builder().code("new").active(true).build());
        orderStatusService.save(new OrderStatus.Builder().code("inprogress").active(true).build());
        orderStatusService.save(new OrderStatus.Builder().code("completed").active(true).build());
        orderStatusService.save(new OrderStatus.Builder().code("draft").active(true).build());
        //endregion

        //region Product size
        localService.save(new Local.Builder().code("large").languageId(en.getId()).message("large").build());
        localService.save(new Local.Builder().code("medium").languageId(en.getId()).message("medium").build());
        localService.save(new Local.Builder().code("small").languageId(en.getId()).message("small").build());
        localService.save(new Local.Builder().code("large").languageId(ru.getId()).message("большой").build());
        localService.save(new Local.Builder().code("medium").languageId(ru.getId()).message("средний").build());
        localService.save(new Local.Builder().code("small").languageId(ru.getId()).message("маленький").build());

        ProductSizeService productSizeService = new ProductSizeService();
        productSizeService.save(new ProductSize.Builder().code("large").active(true).build());
        productSizeService.save(new ProductSize.Builder().code("medium").active(true).build());
        productSizeService.save(new ProductSize.Builder().code("small").active(true).build());
        //endregion

        //region Product category
        localService.save(new Local.Builder().code("cake").languageId(en.getId()).message("cake").build());
        localService.save(new Local.Builder().code("cheesecake").languageId(en.getId()).message("cheesecake").build());
        localService.save(new Local.Builder().code("pie").languageId(en.getId()).message("pie").build());
        localService.save(new Local.Builder().code("cake").languageId(ru.getId()).message("торт").build());
        localService.save(new Local.Builder().code("cheesecake").languageId(ru.getId()).message("чизкейк").build());
        localService.save(new Local.Builder().code("pie").languageId(ru.getId()).message("пирог").build());

        ProductCategoryService productCategoryService = new ProductCategoryService();
        ProductCategory cake = productCategoryService.save(new ProductCategory.Builder().code("cake").active(true).build());
        ProductCategory cheesecake = productCategoryService.save(new ProductCategory.Builder().code("cheesecake").active(true).build());
        ProductCategory pie = productCategoryService.save(new ProductCategory.Builder().code("pie").active(true).build());

        localService.save(new Local.Builder().code("biscuit").languageId(en.getId()).message("biscuit").build());
        localService.save(new Local.Builder().code("bento").languageId(en.getId()).message("bento").build());
        localService.save(new Local.Builder().code("trifle").languageId(en.getId()).message("trifle").build());
        localService.save(new Local.Builder().code("whoopee").languageId(en.getId()).message("whoopee").build());
        localService.save(new Local.Builder().code("spanish").languageId(en.getId()).message("spanish").build());
        localService.save(new Local.Builder().code("classic").languageId(en.getId()).message("classic").build());
        localService.save(new Local.Builder().code("chocolate").languageId(en.getId()).message("chocolate").build());
        localService.save(new Local.Builder().code("meat").languageId(en.getId()).message("meat").build());
        localService.save(new Local.Builder().code("other").languageId(en.getId()).message("other").build());

        localService.save(new Local.Builder().code("biscuit").languageId(ru.getId()).message("бисквит").build());
        localService.save(new Local.Builder().code("bento").languageId(ru.getId()).message("бенто").build());
        localService.save(new Local.Builder().code("trifle").languageId(ru.getId()).message("трайфл").build());
        localService.save(new Local.Builder().code("whoopee").languageId(ru.getId()).message("вупипай").build());
        localService.save(new Local.Builder().code("spanish").languageId(ru.getId()).message("испанский").build());
        localService.save(new Local.Builder().code("classic").languageId(ru.getId()).message("классический").build());
        localService.save(new Local.Builder().code("chocolate").languageId(ru.getId()).message("шоколадный").build());
        localService.save(new Local.Builder().code("meat").languageId(ru.getId()).message("мясной").build());
        localService.save(new Local.Builder().code("other").languageId(ru.getId()).message("другое").build());

        productCategoryService.save(new ProductCategory.Builder().parent(cake.getId()).code("biscuit").active(true).build());
        productCategoryService.save(new ProductCategory.Builder().parent(cake.getId()).code("bento").active(true).build());
        productCategoryService.save(new ProductCategory.Builder().parent(cake.getId()).code("trifle").active(true).build());
        productCategoryService.save(new ProductCategory.Builder().parent(cake.getId()).code("whoopee").active(true).build());
        productCategoryService.save(new ProductCategory.Builder().parent(cake.getId()).code("other").active(true).build());

        productCategoryService.save(new ProductCategory.Builder().parent(cheesecake.getId()).code("classic").active(true).build());
        productCategoryService.save(new ProductCategory.Builder().parent(cheesecake.getId()).code("chocolate").active(true).build());
        productCategoryService.save(new ProductCategory.Builder().parent(cheesecake.getId()).code("spanish").active(true).build());
        productCategoryService.save(new ProductCategory.Builder().parent(cheesecake.getId()).code("other").active(true).build());

        productCategoryService.save(new ProductCategory.Builder().parent(pie.getId()).code("classic").active(true).build());
        productCategoryService.save(new ProductCategory.Builder().parent(pie.getId()).code("meat").active(true).build());
        productCategoryService.save(new ProductCategory.Builder().parent(pie.getId()).code("other").active(true).build());
        //endregion

        save(new Settings.Builder()
                .isInitTables(true)
                .isDemoMode(false)
                .build());
    }
}

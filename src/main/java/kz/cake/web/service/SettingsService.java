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

import java.math.BigDecimal;
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
        List<String> products = Arrays.asList("Product", "Order");
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
        localService.save(new Local.Builder().code(LocaleCodes.roleAdmin.getName()).languageId(en.getId()).message("admin").build());
        localService.save(new Local.Builder().code(LocaleCodes.roleManager.getName()).languageId(en.getId()).message("manager").build());
        localService.save(new Local.Builder().code(LocaleCodes.roleUser.getName()).languageId(en.getId()).message("user").build());
        localService.save(new Local.Builder().code(LocaleCodes.roleAdmin.getName()).languageId(ru.getId()).message("??????????").build());
        localService.save(new Local.Builder().code(LocaleCodes.roleManager.getName()).languageId(ru.getId()).message("????????????????").build());
        localService.save(new Local.Builder().code(LocaleCodes.roleUser.getName()).languageId(ru.getId()).message("????????????????????????").build());

        RoleService roleService = new RoleService();
        Role adminRole = roleService.save(new Role.Builder().code(LocaleCodes.roleAdmin.getName()).active(true).build());
        Role managerRole = roleService.save(new Role.Builder().code(LocaleCodes.roleManager.getName()).active(true).build());
        Role userRole = roleService.save(new Role.Builder().code(LocaleCodes.roleUser.getName()).active(true).build());
        //endregion

        //region User
        UserService userService = new UserService();
        UserRoleService userRoleService = new UserRoleService();

        User adminUser = userService.save(new User.Builder()
                .name(LocaleCodes.userAdmin.getName())
                .password(StringUtils.encryptPassword("admin"))
                .active(true)
                .build());
        userRoleService.save(new UserRole(adminUser.getId(), adminRole.getId()));

        User kulikovUser = userService.save(new User.Builder()
                .name(LocaleCodes.userKulikov.getName())
                .password(StringUtils.encryptPassword("test"))
                .active(true)
                .build());
        userRoleService.save(new UserRole(kulikovUser.getId(), managerRole.getId()));

        User qulpinayUser = userService.save(new User.Builder()
                .name(LocaleCodes.userQulpinay.getName())
                .password(StringUtils.encryptPassword("test"))
                .active(true)
                .build());
        userRoleService.save(new UserRole(qulpinayUser.getId(), managerRole.getId()));

        User bekaUser = userService.save(new User.Builder()
                .name(LocaleCodes.userBeka.getName())
                .password(StringUtils.encryptPassword("test"))
                .active(true)
                .build());
        userRoleService.save(new UserRole(bekaUser.getId(), userRole.getId()));
        //endregion

        //region City
        localService.save(new Local.Builder().code(LocaleCodes.cityAlmaty.getName()).languageId(en.getId()).message("Almaty").build());
        localService.save(new Local.Builder().code(LocaleCodes.cityAstana.getName()).languageId(en.getId()).message("Astana").build());
        localService.save(new Local.Builder().code(LocaleCodes.cityAlmaty.getName()).languageId(ru.getId()).message("????????????").build());
        localService.save(new Local.Builder().code(LocaleCodes.cityAstana.getName()).languageId(ru.getId()).message("????????????").build());

        CityService cityService = new CityService();
        City almaty = cityService.save(new City.Builder().code(LocaleCodes.cityAlmaty.getName()).active(true).build());
        City astana = cityService.save(new City.Builder().code(LocaleCodes.cityAstana.getName()).active(true).build());
        //endregion

        //region Order status
        localService.save(new Local.Builder().code(LocaleCodes.statusDraft.getName()).languageId(en.getId()).message("Draft").build());
        localService.save(new Local.Builder().code(LocaleCodes.statusNew.getName()).languageId(en.getId()).message("New").build());
        localService.save(new Local.Builder().code(LocaleCodes.statusCompleted.getName()).languageId(en.getId()).message("Completed").build());
        localService.save(new Local.Builder().code(LocaleCodes.statusDraft.getName()).languageId(ru.getId()).message("????????????????").build());
        localService.save(new Local.Builder().code(LocaleCodes.statusNew.getName()).languageId(ru.getId()).message("??????????").build());
        localService.save(new Local.Builder().code(LocaleCodes.statusCompleted.getName()).languageId(ru.getId()).message("????????????????").build());

        OrderStatusService orderStatusService = new OrderStatusService();
        orderStatusService.save(new OrderStatus.Builder().code(LocaleCodes.statusNew.getName()).active(true).build());
        orderStatusService.save(new OrderStatus.Builder().code(LocaleCodes.statusCompleted.getName()).active(true).build());
        orderStatusService.save(new OrderStatus.Builder().code(LocaleCodes.statusDraft.getName()).active(true).build());
        //endregion

        //region Product size
        localService.save(new Local.Builder().code(LocaleCodes.sizeLarge.getName()).languageId(en.getId()).message("large").build());
        localService.save(new Local.Builder().code(LocaleCodes.sizeMedium.getName()).languageId(en.getId()).message("medium").build());
        localService.save(new Local.Builder().code(LocaleCodes.sizeSmall.getName()).languageId(en.getId()).message("small").build());
        localService.save(new Local.Builder().code(LocaleCodes.sizeLarge.getName()).languageId(ru.getId()).message("??????????????").build());
        localService.save(new Local.Builder().code(LocaleCodes.sizeMedium.getName()).languageId(ru.getId()).message("??????????????").build());
        localService.save(new Local.Builder().code(LocaleCodes.sizeSmall.getName()).languageId(ru.getId()).message("??????????????????").build());

        ProductSizeService productSizeService = new ProductSizeService();
        ProductSize l = productSizeService.save(new ProductSize.Builder().code(LocaleCodes.sizeLarge.getName()).active(true).build());
        ProductSize m = productSizeService.save(new ProductSize.Builder().code(LocaleCodes.sizeMedium.getName()).active(true).build());
        ProductSize s = productSizeService.save(new ProductSize.Builder().code(LocaleCodes.sizeSmall.getName()).active(true).build());
        //endregion

        //region Product category
        localService.save(new Local.Builder().code(LocaleCodes.categoryCake.getName()).languageId(en.getId()).message("cake").build());
        localService.save(new Local.Builder().code(LocaleCodes.categoryCheesecake.getName()).languageId(en.getId()).message("cheesecake").build());
        localService.save(new Local.Builder().code(LocaleCodes.categoryPie.getName()).languageId(en.getId()).message("pie").build());
        localService.save(new Local.Builder().code(LocaleCodes.categoryCake.getName()).languageId(ru.getId()).message("????????").build());
        localService.save(new Local.Builder().code(LocaleCodes.categoryCheesecake.getName()).languageId(ru.getId()).message("??????????????").build());
        localService.save(new Local.Builder().code(LocaleCodes.categoryPie.getName()).languageId(ru.getId()).message("??????????").build());

        ProductCategoryService productCategoryService = new ProductCategoryService();
        ProductCategory cake = productCategoryService.save(new ProductCategory.Builder().code(LocaleCodes.categoryCake.getName()).active(true).build());
        ProductCategory cheesecake = productCategoryService.save(new ProductCategory.Builder().code(LocaleCodes.categoryCheesecake.getName()).active(true).build());
        ProductCategory pie = productCategoryService.save(new ProductCategory.Builder().code(LocaleCodes.categoryPie.getName()).active(true).build());

        localService.save(new Local.Builder().code(LocaleCodes.categoryBiscuit.getName()).languageId(en.getId()).message("biscuit").build());
        localService.save(new Local.Builder().code(LocaleCodes.categoryBento.getName()).languageId(en.getId()).message("bento").build());
        localService.save(new Local.Builder().code(LocaleCodes.categoryTrifle.getName()).languageId(en.getId()).message("trifle").build());
        localService.save(new Local.Builder().code(LocaleCodes.categoryWhoopee.getName()).languageId(en.getId()).message("whoopee").build());
        localService.save(new Local.Builder().code(LocaleCodes.categorySpanish.getName()).languageId(en.getId()).message("spanish").build());
        localService.save(new Local.Builder().code(LocaleCodes.categoryClassic.getName()).languageId(en.getId()).message("classic").build());
        localService.save(new Local.Builder().code(LocaleCodes.categoryChocolate.getName()).languageId(en.getId()).message("chocolate").build());
        localService.save(new Local.Builder().code(LocaleCodes.categoryMeat.getName()).languageId(en.getId()).message("meat").build());
        localService.save(new Local.Builder().code(LocaleCodes.categoryOther.getName()).languageId(en.getId()).message("other").build());

        localService.save(new Local.Builder().code(LocaleCodes.categoryBiscuit.getName()).languageId(ru.getId()).message("??????????????").build());
        localService.save(new Local.Builder().code(LocaleCodes.categoryBento.getName()).languageId(ru.getId()).message("??????????").build());
        localService.save(new Local.Builder().code(LocaleCodes.categoryTrifle.getName()).languageId(ru.getId()).message("????????????").build());
        localService.save(new Local.Builder().code(LocaleCodes.categoryWhoopee.getName()).languageId(ru.getId()).message("??????????????").build());
        localService.save(new Local.Builder().code(LocaleCodes.categorySpanish.getName()).languageId(ru.getId()).message("??????????????????").build());
        localService.save(new Local.Builder().code(LocaleCodes.categoryClassic.getName()).languageId(ru.getId()).message("????????????????????????").build());
        localService.save(new Local.Builder().code(LocaleCodes.categoryChocolate.getName()).languageId(ru.getId()).message("????????????????????").build());
        localService.save(new Local.Builder().code(LocaleCodes.categoryMeat.getName()).languageId(ru.getId()).message("????????????").build());
        localService.save(new Local.Builder().code(LocaleCodes.categoryOther.getName()).languageId(ru.getId()).message("????????????").build());

        ProductCategory biscuit = productCategoryService.save(new ProductCategory.Builder().parent(cake.getId()).code(LocaleCodes.categoryBiscuit.getName()).active(true).build());
        ProductCategory bento = productCategoryService.save(new ProductCategory.Builder().parent(cake.getId()).code(LocaleCodes.categoryBento.getName()).active(true).build());
        ProductCategory trifle = productCategoryService.save(new ProductCategory.Builder().parent(cake.getId()).code(LocaleCodes.categoryTrifle.getName()).active(true).build());
        ProductCategory whoopee = productCategoryService.save(new ProductCategory.Builder().parent(cake.getId()).code(LocaleCodes.categoryWhoopee.getName()).active(true).build());
        ProductCategory other_cake = productCategoryService.save(new ProductCategory.Builder().parent(cake.getId()).code(LocaleCodes.categoryOther.getName()).active(true).build());

        ProductCategory classic_chees = productCategoryService.save(new ProductCategory.Builder().parent(cheesecake.getId()).code(LocaleCodes.categoryClassic.getName()).active(true).build());
        ProductCategory chocolate = productCategoryService.save(new ProductCategory.Builder().parent(cheesecake.getId()).code(LocaleCodes.categoryChocolate.getName()).active(true).build());
        ProductCategory spanish = productCategoryService.save(new ProductCategory.Builder().parent(cheesecake.getId()).code(LocaleCodes.categorySpanish.getName()).active(true).build());
        productCategoryService.save(new ProductCategory.Builder().parent(cheesecake.getId()).code(LocaleCodes.categoryOther.getName()).active(true).build());

        ProductCategory classic_pie = productCategoryService.save(new ProductCategory.Builder().parent(pie.getId()).code(LocaleCodes.categoryClassic.getName()).active(true).build());
        ProductCategory meat = productCategoryService.save(new ProductCategory.Builder().parent(pie.getId()).code(LocaleCodes.categoryMeat.getName()).active(true).build());
        productCategoryService.save(new ProductCategory.Builder().parent(pie.getId()).code(LocaleCodes.categoryOther.getName()).active(true).build());
        //endregion


        //region Product
        ProductService productService = new ProductService();
        productService.save(new Product.Builder()
                .active(true)
                .name("?????????????????? ????????")
                .description("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam a magna quis erat faucibus interdum. Suspendisse pharetra rhoncus nibh non congue. Duis mauris urna, tempus sed varius vitae, lobortis vitae tellus. Vivamus porta, lacus sit amet congue finibus, turpis purus tincidunt ante, id volutpat sem mi a enim. Nam a pulvinar purus. Ut pharetra sollicitudin lectus at viverra. Suspendisse sed mauris sed mauris gravida posuere sit amet ut magna. In in augue eu erat ultricies ornare. Sed sapien tortor, viverra ac dolor eget, dictum feugiat risus. Nunc venenatis sodales vulputate. Nam vitae est odio. Nullam nec quam posuere, blandit purus malesuada, semper turpis. Integer nec velit dolor. Morbi blandit blandit mauris, nec bibendum lacus dictum sit amet. Cras non magna at lectus sagittis hendrerit.")
                .price(new BigDecimal(10000))
                .sizeId(l.getId())
                .cityId(almaty.getId())
                .categoryId(biscuit.getId())
                .userId(kulikovUser.getId())
                .build());
        productService.save(new Product.Builder()
                .active(true)
                .name("?????????? ????????")
                .description("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam a magna quis erat faucibus interdum. Suspendisse pharetra rhoncus nibh non congue. Duis mauris urna, tempus sed varius vitae, lobortis vitae tellus. Vivamus porta, lacus sit amet congue finibus, turpis purus tincidunt ante, id volutpat sem mi a enim. Nam a pulvinar purus. Ut pharetra sollicitudin lectus at viverra. Suspendisse sed mauris sed mauris gravida posuere sit amet ut magna. In in augue eu erat ultricies ornare. Sed sapien tortor, viverra ac dolor eget, dictum feugiat risus. Nunc venenatis sodales vulputate. Nam vitae est odio. Nullam nec quam posuere, blandit purus malesuada, semper turpis. Integer nec velit dolor. Morbi blandit blandit mauris, nec bibendum lacus dictum sit amet. Cras non magna at lectus sagittis hendrerit.")
                .price(new BigDecimal(6000))
                .sizeId(l.getId())
                .cityId(almaty.getId())
                .categoryId(bento.getId())
                .userId(kulikovUser.getId())
                .build());
        productService.save(new Product.Builder()
                .active(true)
                .name("????????????")
                .description("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam a magna quis erat faucibus interdum. Suspendisse pharetra rhoncus nibh non congue. Duis mauris urna, tempus sed varius vitae, lobortis vitae tellus. Vivamus porta, lacus sit amet congue finibus, turpis purus tincidunt ante, id volutpat sem mi a enim. Nam a pulvinar purus. Ut pharetra sollicitudin lectus at viverra. Suspendisse sed mauris sed mauris gravida posuere sit amet ut magna. In in augue eu erat ultricies ornare. Sed sapien tortor, viverra ac dolor eget, dictum feugiat risus. Nunc venenatis sodales vulputate. Nam vitae est odio. Nullam nec quam posuere, blandit purus malesuada, semper turpis. Integer nec velit dolor. Morbi blandit blandit mauris, nec bibendum lacus dictum sit amet. Cras non magna at lectus sagittis hendrerit.")
                .price(new BigDecimal(5000))
                .sizeId(l.getId())
                .cityId(almaty.getId())
                .categoryId(trifle.getId())
                .userId(kulikovUser.getId())
                .build());
        productService.save(new Product.Builder()
                .active(true)
                .name("??????????????")
                .description("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam a magna quis erat faucibus interdum. Suspendisse pharetra rhoncus nibh non congue. Duis mauris urna, tempus sed varius vitae, lobortis vitae tellus. Vivamus porta, lacus sit amet congue finibus, turpis purus tincidunt ante, id volutpat sem mi a enim. Nam a pulvinar purus. Ut pharetra sollicitudin lectus at viverra. Suspendisse sed mauris sed mauris gravida posuere sit amet ut magna. In in augue eu erat ultricies ornare. Sed sapien tortor, viverra ac dolor eget, dictum feugiat risus. Nunc venenatis sodales vulputate. Nam vitae est odio. Nullam nec quam posuere, blandit purus malesuada, semper turpis. Integer nec velit dolor. Morbi blandit blandit mauris, nec bibendum lacus dictum sit amet. Cras non magna at lectus sagittis hendrerit.")
                .price(new BigDecimal(11000))
                .sizeId(l.getId())
                .cityId(almaty.getId())
                .categoryId(whoopee.getId())
                .userId(kulikovUser.getId())
                .build());
        productService.save(new Product.Builder()
                .active(true)
                .name("????????????????")
                .description("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam a magna quis erat faucibus interdum. Suspendisse pharetra rhoncus nibh non congue. Duis mauris urna, tempus sed varius vitae, lobortis vitae tellus. Vivamus porta, lacus sit amet congue finibus, turpis purus tincidunt ante, id volutpat sem mi a enim. Nam a pulvinar purus. Ut pharetra sollicitudin lectus at viverra. Suspendisse sed mauris sed mauris gravida posuere sit amet ut magna. In in augue eu erat ultricies ornare. Sed sapien tortor, viverra ac dolor eget, dictum feugiat risus. Nunc venenatis sodales vulputate. Nam vitae est odio. Nullam nec quam posuere, blandit purus malesuada, semper turpis. Integer nec velit dolor. Morbi blandit blandit mauris, nec bibendum lacus dictum sit amet. Cras non magna at lectus sagittis hendrerit.")
                .price(new BigDecimal(12000))
                .sizeId(l.getId())
                .cityId(almaty.getId())
                .categoryId(other_cake.getId())
                .userId(kulikovUser.getId())
                .build());
        productService.save(new Product.Builder()
                .active(true)
                .name("???????????????????????? ??????????????")
                .description("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam a magna quis erat faucibus interdum. Suspendisse pharetra rhoncus nibh non congue. Duis mauris urna, tempus sed varius vitae, lobortis vitae tellus. Vivamus porta, lacus sit amet congue finibus, turpis purus tincidunt ante, id volutpat sem mi a enim. Nam a pulvinar purus. Ut pharetra sollicitudin lectus at viverra. Suspendisse sed mauris sed mauris gravida posuere sit amet ut magna. In in augue eu erat ultricies ornare. Sed sapien tortor, viverra ac dolor eget, dictum feugiat risus. Nunc venenatis sodales vulputate. Nam vitae est odio. Nullam nec quam posuere, blandit purus malesuada, semper turpis. Integer nec velit dolor. Morbi blandit blandit mauris, nec bibendum lacus dictum sit amet. Cras non magna at lectus sagittis hendrerit.")
                .price(new BigDecimal(5000))
                .sizeId(l.getId())
                .cityId(astana.getId())
                .categoryId(classic_chees.getId())
                .userId(qulpinayUser.getId())
                .build());
        productService.save(new Product.Builder()
                .active(true)
                .name("???????????????????? ??????????????")
                .description("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam a magna quis erat faucibus interdum. Suspendisse pharetra rhoncus nibh non congue. Duis mauris urna, tempus sed varius vitae, lobortis vitae tellus. Vivamus porta, lacus sit amet congue finibus, turpis purus tincidunt ante, id volutpat sem mi a enim. Nam a pulvinar purus. Ut pharetra sollicitudin lectus at viverra. Suspendisse sed mauris sed mauris gravida posuere sit amet ut magna. In in augue eu erat ultricies ornare. Sed sapien tortor, viverra ac dolor eget, dictum feugiat risus. Nunc venenatis sodales vulputate. Nam vitae est odio. Nullam nec quam posuere, blandit purus malesuada, semper turpis. Integer nec velit dolor. Morbi blandit blandit mauris, nec bibendum lacus dictum sit amet. Cras non magna at lectus sagittis hendrerit.")
                .price(new BigDecimal(5000))
                .sizeId(l.getId())
                .cityId(astana.getId())
                .categoryId(chocolate.getId())
                .userId(qulpinayUser.getId())
                .build());
        productService.save(new Product.Builder()
                .active(true)
                .name("?????????????????? ??????????????")
                .description("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam a magna quis erat faucibus interdum. Suspendisse pharetra rhoncus nibh non congue. Duis mauris urna, tempus sed varius vitae, lobortis vitae tellus. Vivamus porta, lacus sit amet congue finibus, turpis purus tincidunt ante, id volutpat sem mi a enim. Nam a pulvinar purus. Ut pharetra sollicitudin lectus at viverra. Suspendisse sed mauris sed mauris gravida posuere sit amet ut magna. In in augue eu erat ultricies ornare. Sed sapien tortor, viverra ac dolor eget, dictum feugiat risus. Nunc venenatis sodales vulputate. Nam vitae est odio. Nullam nec quam posuere, blandit purus malesuada, semper turpis. Integer nec velit dolor. Morbi blandit blandit mauris, nec bibendum lacus dictum sit amet. Cras non magna at lectus sagittis hendrerit.")
                .price(new BigDecimal(8000))
                .sizeId(l.getId())
                .cityId(astana.getId())
                .categoryId(spanish.getId())
                .userId(qulpinayUser.getId())
                .build());
        productService.save(new Product.Builder()
                .active(true)
                .name("???????????????????????? ??????????")
                .description("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam a magna quis erat faucibus interdum. Suspendisse pharetra rhoncus nibh non congue. Duis mauris urna, tempus sed varius vitae, lobortis vitae tellus. Vivamus porta, lacus sit amet congue finibus, turpis purus tincidunt ante, id volutpat sem mi a enim. Nam a pulvinar purus. Ut pharetra sollicitudin lectus at viverra. Suspendisse sed mauris sed mauris gravida posuere sit amet ut magna. In in augue eu erat ultricies ornare. Sed sapien tortor, viverra ac dolor eget, dictum feugiat risus. Nunc venenatis sodales vulputate. Nam vitae est odio. Nullam nec quam posuere, blandit purus malesuada, semper turpis. Integer nec velit dolor. Morbi blandit blandit mauris, nec bibendum lacus dictum sit amet. Cras non magna at lectus sagittis hendrerit.")
                .price(new BigDecimal(5000))
                .sizeId(l.getId())
                .cityId(astana.getId())
                .categoryId(classic_pie.getId())
                .userId(qulpinayUser.getId())
                .build());
        productService.save(new Product.Builder()
                .active(true)
                .name("???????????? ??????????")
                .description("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam a magna quis erat faucibus interdum. Suspendisse pharetra rhoncus nibh non congue. Duis mauris urna, tempus sed varius vitae, lobortis vitae tellus. Vivamus porta, lacus sit amet congue finibus, turpis purus tincidunt ante, id volutpat sem mi a enim. Nam a pulvinar purus. Ut pharetra sollicitudin lectus at viverra. Suspendisse sed mauris sed mauris gravida posuere sit amet ut magna. In in augue eu erat ultricies ornare. Sed sapien tortor, viverra ac dolor eget, dictum feugiat risus. Nunc venenatis sodales vulputate. Nam vitae est odio. Nullam nec quam posuere, blandit purus malesuada, semper turpis. Integer nec velit dolor. Morbi blandit blandit mauris, nec bibendum lacus dictum sit amet. Cras non magna at lectus sagittis hendrerit.")
                .price(new BigDecimal(5000))
                .sizeId(l.getId())
                .cityId(astana.getId())
                .categoryId(meat.getId())
                .userId(qulpinayUser.getId())
                .build());
        //endregion

        save(new Settings.Builder()
                .isInitTables(true)
                .isDemoMode(false)
                .build());
    }
}

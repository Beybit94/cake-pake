package kz.cake.web.service;

import kz.cake.web.database.BasicConnectionPool;
import kz.cake.web.entity.Role;
import kz.cake.web.entity.User;
import kz.cake.web.entity.UserRole;
import kz.cake.web.helpers.StringUtils;
import kz.cake.web.helpers.constants.PageNames;
import kz.cake.web.model.CurrentUser;
import kz.cake.web.model.ValidationError;
import kz.cake.web.repository.UserRepository;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserService extends BaseService<User, UserRepository> {
    private final Logger logger = LogManager.getLogger(UserService.class);

    public UserService() {
        this.repository = new UserRepository();
    }

    @Override
    public void execute(String action, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, NoSuchAlgorithmException {
        List<ValidationError> errorList = new ArrayList<>();
        Optional<User> user = null;
        switch (action) {
            case "Find":
                user = findUserByName(request.getParameter("username"));
                if (!user.isPresent()) {
                    errorList.add(new ValidationError("error.userNotFound"));
                } else {
                    if (!user.get().isActive()) {
                        errorList.add(new ValidationError("error.userNotActive"));
                    } else {
                        String hash = StringUtils.encryptPassword(request.getParameter("password"));
                        if (!user.get().getPassword().equals(hash)) {
                            errorList.add(new ValidationError("error.passwordNotCorrect"));
                        }
                    }
                }

                if (errorList.isEmpty()) {
                    HttpSession session = request.getSession(true);

                    CurrentUser currentUser = new CurrentUser();
                    currentUser.setUserId(user.get().getId());
                    currentUser.setUserName(user.get().getName());

                    UserRoleService userRoleService = new UserRoleService();
                    RoleService roleService = new RoleService();
                    userRoleService.findByUserId(currentUser.getUserId()).forEach(ur -> {
                        Role role = roleService.getById(ur.getRoleId());
                        currentUser.getRoles().add(role.getCode());
                    });

                    session.setAttribute("user", currentUser);
                    RequestDispatcher dispatcher = request.getRequestDispatcher(PageNames.main.getName());
                    dispatcher.forward(request, response);
                } else {
                    request.setAttribute("errors", errorList);
                    RequestDispatcher dispatcher = request.getRequestDispatcher(PageNames.login.getName());
                    dispatcher.forward(request, response);
                }
                break;
            case "Save":
                if (!request.getParameter("password").equals(request.getParameter("confirm"))) {
                    errorList.add(new ValidationError("error.passwordNotMatch"));
                }

                user = findUserByName(request.getParameter("username"));
                if(user.isPresent()){
                    errorList.add(new ValidationError("error.userExist"));
                }

                if (errorList.isEmpty()) {
                    save(new User.Builder()
                            .name(request.getParameter("username"))
                            .password(StringUtils.encryptPassword(request.getParameter("password")))
                            .active(true)
                            .build());
                    user = findUserByName(request.getParameter("username"));

                    RoleService roleService = new RoleService();
                    UserRoleService userRoleService = new UserRoleService();

                    Optional<Role> role = roleService.findByCode(request.getParameter("role"));
                    if(role.isPresent()){
                        userRoleService.save(new UserRole(user.get().getId(),role.get().getId()));
                    }

                    HttpSession session = request.getSession(true);
                    CurrentUser currentUser = new CurrentUser();
                    currentUser.setUserId(user.get().getId());
                    currentUser.setUserName(user.get().getName());

                    userRoleService.findByUserId(currentUser.getUserId()).forEach(ur -> {
                        currentUser.getRoles().add(roleService.getById(ur.getRoleId()).getCode());
                    });

                    session.setAttribute("user", currentUser);
                    RequestDispatcher dispatcher = request.getRequestDispatcher(PageNames.main.getName());
                    dispatcher.forward(request, response);
                } else {
                    request.setAttribute("errors", errorList);
                    RequestDispatcher dispatcher = request.getRequestDispatcher(PageNames.register.getName());
                    dispatcher.forward(request, response);
                }
                break;
            case "Logout":
                request.getSession().invalidate();
                RequestDispatcher dispatcher = request.getRequestDispatcher(PageNames.main.getName());
                dispatcher.forward(request, response);
                break;
        }
    }

    public Optional<User> findUserByName(String userName) {
        User user = null;
        Connection connection = BasicConnectionPool.Instance.getConnection();
        try {
            try (PreparedStatement preparedStatement = connection.prepareStatement("select * from web.users where username=?")) {
                preparedStatement.setString(1, userName);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    user = new User.Builder()
                            .id(resultSet.getLong("id"))
                            .active(resultSet.getBoolean("active"))
                            .name(resultSet.getString("username"))
                            .password(resultSet.getString("password"))
                            .sex(resultSet.getString("sex"))
                            .address(resultSet.getString("address"))
                            .build();
                }
            }
        } catch (Exception e) {
            logger.error(e);
        } finally {
            BasicConnectionPool.Instance.releaseConnection(connection);
        }

        return Optional.ofNullable(user);
    }
}

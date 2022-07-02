package kz.cake.web.controller;

import kz.cake.web.controller.base.BaseController;
import kz.cake.web.entity.User;
import kz.cake.web.entity.UserRole;
import kz.cake.web.helpers.CurrentSession;
import kz.cake.web.helpers.StringUtils;
import kz.cake.web.helpers.constants.ActionNames;
import kz.cake.web.helpers.constants.PageNames;
import kz.cake.web.helpers.constants.SessionParameters;
import kz.cake.web.model.CurrentUserDto;
import kz.cake.web.model.DictionaryDto;
import kz.cake.web.model.ValidationErrorDto;
import kz.cake.web.service.RoleService;
import kz.cake.web.service.UserRoleService;
import kz.cake.web.service.UserService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AuthController extends BaseController {
    private final UserService userService;

    public AuthController() {
        userService = new UserService();
    }

    public void profile(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute(SessionParameters.user.getName(), userService.read(CurrentSession.Instance.getCurrentUser().getUserId()));
        RequestDispatcher dispatcher = request.getRequestDispatcher(PageNames.profile.getName());
        dispatcher.forward(request, response);
    }

    public void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<ValidationErrorDto> errorList = new ArrayList<>();
        Optional<User> user = userService.findUserByName(request.getParameter("username"));
        if (!user.isPresent()) {
            errorList.add(new ValidationErrorDto("error.userNotFound"));
        } else {
            if (!user.get().isActive()) {
                errorList.add(new ValidationErrorDto("error.userNotActive"));
            } else {
                String hash = StringUtils.encryptPassword(request.getParameter("password"));
                if (!user.get().getPassword().equals(hash)) {
                    errorList.add(new ValidationErrorDto("error.passwordNotCorrect"));
                }
            }
        }

        if (errorList.isEmpty()) {
            HttpSession session = request.getSession(true);

            CurrentUserDto currentUser = new CurrentUserDto();
            currentUser.setUserId(user.get().getId());
            currentUser.setUserName(user.get().getUsername());

            UserRoleService userRoleService = new UserRoleService();
            RoleService roleService = new RoleService();
            userRoleService.findByUserId(currentUser.getUserId()).forEach(ur -> {
                DictionaryDto role = roleService.getByIdWithLocal(ur.getRoleId());
                currentUser.getRoles().add(role.getCode());
            });

            session.setAttribute(SessionParameters.user.getName(), currentUser);
            CurrentSession.Instance.setCurrentUser(currentUser);
            RequestDispatcher dispatcher = request.getRequestDispatcher(ActionNames.ProductList.getName());
            dispatcher.forward(request, response);
        } else {
            request.setAttribute(SessionParameters.errors.getName(), errorList);
            RequestDispatcher dispatcher = request.getRequestDispatcher(PageNames.login.getName());
            dispatcher.forward(request, response);
        }
    }

    public void register(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException, IllegalAccessException {
        List<ValidationErrorDto> errorList = new ArrayList<>();
        if (request.getParameter("password").isEmpty()) {
            errorList.add(new ValidationErrorDto("error.passwordNotCorrect"));
        }
        if (request.getParameter("password").length() < 4) {
            errorList.add(new ValidationErrorDto("error.passwordNotCorrect"));
        }
        if (!request.getParameter("password").equals(request.getParameter("confirm"))) {
            errorList.add(new ValidationErrorDto("error.passwordNotMatch"));
        }

        Optional<User> user = userService.findUserByName(request.getParameter("username"));
        if (user.isPresent()) {
            errorList.add(new ValidationErrorDto("error.userExist"));
        }

        if (errorList.isEmpty()) {
            User newUser = userService.save(new User.Builder()
                    .name(request.getParameter("username"))
                    .password(StringUtils.encryptPassword(request.getParameter("password")))
                    .active(true)
                    .build());

            RoleService roleService = new RoleService();
            UserRoleService userRoleService = new UserRoleService();

            Optional<DictionaryDto> role = roleService.findByCode(request.getParameter("role"));
            if (role.isPresent()) {
                userRoleService.save(new UserRole(newUser.getId(), role.get().getId()));
            }

            HttpSession session = request.getSession(true);
            CurrentUserDto currentUser = new CurrentUserDto();
            currentUser.setUserId(newUser.getId());
            currentUser.setUserName(newUser.getUsername());

            userRoleService.findByUserId(currentUser.getUserId()).forEach(ur -> {
                currentUser.getRoles().add(roleService.getByIdWithLocal(ur.getRoleId()).getCode());
            });

            session.setAttribute(SessionParameters.user.getName(), currentUser);
            CurrentSession.Instance.setCurrentUser(currentUser);
            RequestDispatcher dispatcher = request.getRequestDispatcher(ActionNames.ProductList.getName());
            dispatcher.forward(request, response);
        } else {
            request.setAttribute(SessionParameters.errors.getName(), errorList);
            RequestDispatcher dispatcher = request.getRequestDispatcher(PageNames.register.getName());
            dispatcher.forward(request, response);
        }
    }

    public void logout(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getSession().invalidate();
        CurrentSession.Instance.clear();
        RequestDispatcher dispatcher = request.getRequestDispatcher(ActionNames.ProductList.getName());
        dispatcher.forward(request, response);
    }

    public void change(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException, IllegalAccessException {
        User user = userService.read(CurrentSession.Instance.getCurrentUser().getUserId());
        user.setSex(request.getParameter("sex"));
        user.setAddress(request.getParameter("address"));
        userService.save(user);
        profile(request, response);
    }
}

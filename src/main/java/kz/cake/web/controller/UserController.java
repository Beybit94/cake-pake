package kz.cake.web.controller;

import kz.cake.web.controller.base.BaseController;
import kz.cake.web.entity.User;
import kz.cake.web.entity.UserRole;
import kz.cake.web.exceptions.CustomValidationException;
import kz.cake.web.helpers.CurrentSession;
import kz.cake.web.helpers.StringUtils;
import kz.cake.web.helpers.UrlRouter;
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

public class UserController extends BaseController {
    private final UserService userService;

    public UserController() {
        userService = new UserService();
    }

    public void list(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(true);
        session.setAttribute(SessionParameters.users.getName(), userService.getAllWithRole());
        RequestDispatcher dispatcher = request.getRequestDispatcher(PageNames.users.getName());
        dispatcher.forward(request, response);
    }

    public void add(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException, IllegalAccessException {
        String username = request.getParameter("username");
        userService.save(new User.Builder()
                .name(username)
                .password(StringUtils.encryptPassword(request.getParameter("password")))
                .sex(request.getParameter("sex"))
                .address(request.getParameter("address"))
                .active(true)
                .build());
        userService.setRole(username, "manager");
        list(request, response);
    }

    public void edit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException, IllegalAccessException {
        String username = request.getParameter("username");
        Optional<User> user = userService.findUserByName(username);
        if (user.isPresent()) {
            user.get().setAddress(request.getParameter("address"));
            user.get().setSex(request.getParameter("sex"));
            userService.save(user.get());
        }
        list(request, response);
    }

    public void remove(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, CustomValidationException {
        String username = request.getParameter("username");
        Optional<User> user = userService.findUserByName(username);
        if (user.isPresent()) {
            userService.delete(user.get());
        }
        list(request, response);
    }

    public void reset(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException, IllegalAccessException {
        String username = request.getParameter("username");
        Optional<User> user = userService.findUserByName(username);
        if (user.isPresent()) {
            String hash = StringUtils.encryptPassword(request.getParameter("password"));
            user.get().setPassword(hash);
            userService.save(user.get());
        }
        UrlRouter.Instance.route(ActionNames.UserList.getName(), request, response);
    }

    public void unblock(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException, IllegalAccessException {
        String username = request.getParameter("username");
        Optional<User> user = userService.findUserByName(username);
        if (user.isPresent()) {
            user.get().setActive(true);
            userService.save(user.get());
        }
        UrlRouter.Instance.route(ActionNames.UserList.getName(), request, response);
    }
}

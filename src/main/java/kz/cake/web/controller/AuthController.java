package kz.cake.web.controller;

import kz.cake.web.controller.base.BaseController;
import kz.cake.web.entity.User;
import kz.cake.web.entity.UserRole;
import kz.cake.web.helpers.CurrentSession;
import kz.cake.web.helpers.StringUtils;
import kz.cake.web.helpers.constants.*;
import kz.cake.web.model.CurrentUserDto;
import kz.cake.web.model.DictionaryDto;
import kz.cake.web.model.OrderDto;
import kz.cake.web.model.ValidationErrorDto;
import kz.cake.web.service.*;

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
    private final OrderService orderService;

    public AuthController() {
        userService = new UserService();
        orderService = new OrderService();
    }

    public void profile(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute(SessionParameters.user.getName(), userService.read(CurrentSession.Instance.getCurrentUser().getUserId()));
        RequestDispatcher dispatcher = request.getRequestDispatcher(PageNames.profile.getName());
        dispatcher.forward(request, response);
    }

    public void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<ValidationErrorDto> errorList = new ArrayList<>();
        Optional<User> user = userService.findUserByName(request.getParameter(RequestParameters.username.getName()));
        if (!user.isPresent()) {
            errorList.add(new ValidationErrorDto("error.userNotFound"));
        } else {
            if (!user.get().isActive()) {
                errorList.add(new ValidationErrorDto("error.userNotActive"));
            } else {
                String hash = StringUtils.encryptPassword(request.getParameter(RequestParameters.password.getName()));
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

            CurrentSession.Instance.setCurrentUser(currentUser);
            session.setAttribute(SessionParameters.user.getName(), currentUser);
            session.setAttribute(SessionParameters.orderDraft.getName(), orderService.getDraft().orElse(new OrderDto()));

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
        if (request.getParameter(RequestParameters.password.getName()).isEmpty()) {
            errorList.add(new ValidationErrorDto("error.passwordNotCorrect"));
        }
        if (request.getParameter(RequestParameters.password.getName()).length() < 4) {
            errorList.add(new ValidationErrorDto("error.passwordNotCorrect"));
        }
        if (!request.getParameter(RequestParameters.password.getName()).equals(request.getParameter(RequestParameters.confirm.getName()))) {
            errorList.add(new ValidationErrorDto("error.passwordNotMatch"));
        }

        Optional<User> user = userService.findUserByName(request.getParameter(RequestParameters.username.getName()));
        if (user.isPresent()) {
            errorList.add(new ValidationErrorDto("error.userExist"));
        }

        if (errorList.isEmpty()) {
            User newUser = userService.save(new User.Builder()
                    .name(request.getParameter(RequestParameters.username.getName()))
                    .password(StringUtils.encryptPassword(request.getParameter(RequestParameters.password.getName())))
                    .active(true)
                    .build());

            RoleService roleService = new RoleService();
            UserRoleService userRoleService = new UserRoleService();

            Optional<DictionaryDto> role = roleService.findByCode(request.getParameter(RequestParameters.role.getName()));
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

            CurrentSession.Instance.setCurrentUser(currentUser);
            session.setAttribute(SessionParameters.user.getName(), currentUser);
            session.setAttribute(SessionParameters.orderDraft.getName(), orderService.getDraft().orElse(new OrderDto()));

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
        user.setSex(request.getParameter(RequestParameters.sex.getName()));
        user.setAddress(request.getParameter(RequestParameters.address.getName()));
        userService.save(user);
        profile(request, response);
    }
}

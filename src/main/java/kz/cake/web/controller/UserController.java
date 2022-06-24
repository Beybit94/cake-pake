package kz.cake.web.controller;

import kz.cake.web.entity.User;
import kz.cake.web.entity.UserRole;
import kz.cake.web.helpers.StringUtils;
import kz.cake.web.helpers.constants.ActionNames;
import kz.cake.web.helpers.CurrentSession;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserController extends BaseController {
    private final UserService userService;

    public UserController() {
        userService = new UserService();
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
            response.sendRedirect(request.getContextPath() + "/");
        } else {
            request.setAttribute(SessionParameters.errors.getName(), errorList);
            RequestDispatcher dispatcher = request.getRequestDispatcher(PageNames.login.getName());
            dispatcher.forward(request, response);
        }
    }

    public void register(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<ValidationErrorDto> errorList = new ArrayList<>();
        if (!request.getParameter("password").equals(request.getParameter("confirm"))) {
            errorList.add(new ValidationErrorDto("error.passwordNotMatch"));
        }

        Optional<User> user = userService.findUserByName(request.getParameter("username"));
        if (user.isPresent()) {
            errorList.add(new ValidationErrorDto("error.userExist"));
        }

        if (errorList.isEmpty()) {
            userService.save(new User.Builder()
                    .name(request.getParameter("username"))
                    .password(StringUtils.encryptPassword(request.getParameter("password")))
                    .active(true)
                    .build());
            user = userService.findUserByName(request.getParameter("username"));

            RoleService roleService = new RoleService();
            UserRoleService userRoleService = new UserRoleService();

            Optional<DictionaryDto> role = roleService.findByCode(request.getParameter("role"));
            if (role.isPresent()) {
                userRoleService.save(new UserRole(user.get().getId(), role.get().getId()));
            }

            HttpSession session = request.getSession(true);
            CurrentUserDto currentUser = new CurrentUserDto();
            currentUser.setUserId(user.get().getId());
            currentUser.setUserName(user.get().getUsername());

            userRoleService.findByUserId(currentUser.getUserId()).forEach(ur -> {
                currentUser.getRoles().add(roleService.getByIdWithLocal(ur.getRoleId()).getCode());
            });

            session.setAttribute(SessionParameters.user.getName(), currentUser);
            CurrentSession.Instance.setCurrentUser(currentUser);
            RequestDispatcher dispatcher = request.getRequestDispatcher(PageNames.main.getName());
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
        RequestDispatcher dispatcher = request.getRequestDispatcher(PageNames.main.getName());
        dispatcher.forward(request, response);
    }

    public void list(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(true);
        session.setAttribute(SessionParameters.users.getName(), userService.getAllWithRole());
        RequestDispatcher dispatcher = request.getRequestDispatcher(PageNames.users.getName());
        dispatcher.forward(request, response);
    }

    public void add(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        userService.save(new User.Builder()
                .name(username)
                .password(StringUtils.encryptPassword(request.getParameter("password")))
                .sex(request.getParameter("sex"))
                .address(request.getParameter("address"))
                .active(true)
                .build());
        userService.setRole(username, "manager");
        UrlRouter.Instance.route(ActionNames.UserList.getName(), request, response);
    }

    public void edit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        userService.findUserByName(username).ifPresent(user -> {
            user.setAddress(request.getParameter("address"));
            user.setSex(request.getParameter("sex"));
            userService.save(user);
        });
        UrlRouter.Instance.route(ActionNames.UserList.getName(), request, response);
    }

    public void reset(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        userService.findUserByName(username).ifPresent(user -> {
            String hash = StringUtils.encryptPassword(request.getParameter("password"));
            user.setPassword(hash);
            userService.save(user);
        });
        UrlRouter.Instance.route(ActionNames.UserList.getName(), request, response);
    }

    public void remove(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        userService.findUserByName(username).ifPresent(user->{
            userService.delete(user);
        });
        UrlRouter.Instance.route(ActionNames.UserList.getName(), request, response);
    }

    public void unblock(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        userService.findUserByName(username).ifPresent(user->{
            user.setActive(true);
            userService.save(user);
        });
        UrlRouter.Instance.route(ActionNames.UserList.getName(), request, response);
    }
}

package kz.cake.web.controller;

import kz.cake.web.helpers.UrlRouter;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MainController extends HttpServlet {
    private final Logger logger = LogManager.getLogger(MainController.class);

    public MainController() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            UrlRouter.Instance.route(req, resp);
        } catch (Exception e) {
            logger.error(e);
        }
    }
}

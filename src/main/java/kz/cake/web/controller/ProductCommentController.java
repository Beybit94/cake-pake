package kz.cake.web.controller;

import kz.cake.web.controller.base.BaseController;
import kz.cake.web.entity.ProductComment;
import kz.cake.web.helpers.CurrentSession;
import kz.cake.web.helpers.UrlRouter;
import kz.cake.web.helpers.constants.ActionNames;
import kz.cake.web.helpers.constants.RequestParameters;
import kz.cake.web.service.ProductCommentService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;

public class ProductCommentController extends BaseController {
    private final ProductCommentService productCommentService;

    public ProductCommentController() {
        productCommentService = new ProductCommentService();
    }

    public void add(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException, IllegalAccessException {
        Long id = Long.parseLong(request.getParameter(RequestParameters.id.getName()));
        productCommentService.save(new ProductComment.Builder()
                .productId(id)
                .userId(CurrentSession.Instance.getCurrentUser().getUserId())
                .comment(request.getParameter(RequestParameters.comment.getName()))
                .commentDate(new Timestamp(System.currentTimeMillis()))
                .build());
        UrlRouter.Instance.route(ActionNames.ProductDetail.getName(), request, response);
    }
}

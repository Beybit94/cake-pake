package kz.cake.web.service;

import kz.cake.web.entity.ProductComment;
import kz.cake.web.helpers.StringUtils;
import kz.cake.web.model.CommentDto;
import kz.cake.web.repository.ProductCommentRepository;
import kz.cake.web.service.base.BaseService;

import java.util.List;
import java.util.stream.Collectors;

public class ProductCommentService extends BaseService<ProductComment, ProductCommentRepository> {
    private final UserService userService;

    public ProductCommentService() {
        this.repository = new ProductCommentRepository();
        userService = new UserService();
    }

    public List<CommentDto> getByProductId(Long id) {
        return repository.getByProductId(id).stream().map(m -> map(m)).collect(Collectors.toList());
    }

    private CommentDto map(ProductComment productComment) {
        CommentDto productCommentDto = new CommentDto();
        productCommentDto.setProductId(productComment.getProductId());
        productCommentDto.setComment(productComment.getComment());
        productCommentDto.setCommentDate(StringUtils.localDateString(productComment.getCommentDate()));
        productCommentDto.setUser(userService.read(productComment.getUserId()));
        return productCommentDto;
    }
}

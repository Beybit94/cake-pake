package kz.cake.web.service;

import kz.cake.web.entity.ProductComment;
import kz.cake.web.helpers.StringUtils;
import kz.cake.web.model.ProductCommentDto;
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

    public List<ProductCommentDto> getByProductId(Long id) {
        return repository.getByProductId(id).stream().map(m -> map(m)).collect(Collectors.toList());
    }

    private ProductCommentDto map(ProductComment productComment) {
        ProductCommentDto productCommentDto = new ProductCommentDto();
        productCommentDto.setProductId(productComment.getProductId());
        productCommentDto.setComment(productComment.getComment());
        productCommentDto.setCommentDate(StringUtils.localDateString(productComment.getCommentDate()));
        productCommentDto.setUser(userService.read(productComment.getUserId()));
        return productCommentDto;
    }
}

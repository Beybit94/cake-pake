package kz.cake.web.repository;

import kz.cake.web.entity.ProductComment;
import kz.cake.web.repository.base.BaseRepository;

public class ProductCommentRepository extends BaseRepository<ProductComment> {
    public ProductCommentRepository() {
        supplier = () -> new ProductComment();
    }
}

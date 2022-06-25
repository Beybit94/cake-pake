package kz.cake.web.repository;

import kz.cake.web.entity.ProductPhoto;
import kz.cake.web.repository.base.BaseRepository;

public class ProductPhotoRepository extends BaseRepository<ProductPhoto> {
    public ProductPhotoRepository() {
        supplier = () -> new ProductPhoto();
    }
}

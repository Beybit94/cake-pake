package kz.cake.web.service;

import kz.cake.web.entity.Product;
import kz.cake.web.model.ProductDto;
import kz.cake.web.model.ProductFilterDto;
import kz.cake.web.repository.ProductRepository;
import kz.cake.web.service.base.BaseService;

import java.util.List;

public class ProductService extends BaseService<Product, ProductRepository> {
    public ProductService() {
        this.repository = new ProductRepository();
    }

    public List<ProductDto> find(ProductFilterDto filter){
        return null;
    }
}

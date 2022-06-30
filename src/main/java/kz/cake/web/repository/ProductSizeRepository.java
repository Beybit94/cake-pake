package kz.cake.web.repository;

import kz.cake.web.entity.Product;
import kz.cake.web.entity.ProductSize;
import kz.cake.web.exceptions.CustomValidationException;
import kz.cake.web.helpers.constants.ActionNames;
import kz.cake.web.repository.base.DictionaryRepository;

import java.util.Optional;

public class ProductSizeRepository extends DictionaryRepository<ProductSize> {
    private final ProductRepository productRepository;

    public ProductSizeRepository() {
        supplier = () -> new ProductSize();
        productRepository = new ProductRepository();
    }

    @Override
    public void delete(ProductSize entity) throws CustomValidationException {
        Optional<Product> find = productRepository.getAll().stream().filter(m -> m.getSizeId().equals(entity.getId())).findAny();
        if (find.isPresent()) {
            throw new CustomValidationException("error.activeRecord", ActionNames.CityList);
        }

        super.delete(entity);
    }
}

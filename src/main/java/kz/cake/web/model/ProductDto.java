package kz.cake.web.model;

import kz.cake.web.entity.ProductComment;
import kz.cake.web.entity.User;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ProductDto {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private String priceText;
    private User user;
    private DictionaryDto city;
    private DictionaryDto productSize;
    private DictionaryDto productCategory;
    private List<ProductPhotoDto> photos = new ArrayList();
    private List<ProductCommentDto> comments = new ArrayList();

    public ProductDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getPriceText() {
        return priceText;
    }

    public void setPriceText(String priceText) {
        this.priceText = priceText;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public DictionaryDto getCity() {
        return city;
    }

    public void setCity(DictionaryDto city) {
        this.city = city;
    }

    public DictionaryDto getProductSize() {
        return productSize;
    }

    public void setProductSize(DictionaryDto productSize) {
        this.productSize = productSize;
    }

    public DictionaryDto getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(DictionaryDto productCategory) {
        this.productCategory = productCategory;
    }

    public List<ProductPhotoDto> getPhotos() {
        return photos;
    }

    public void setPhotos(List<ProductPhotoDto> photos) {
        this.photos = photos;
    }

    public List<ProductCommentDto> getComments() {
        return comments;
    }

    public void setComments(List<ProductCommentDto> comments) {
        this.comments = comments;
    }
}

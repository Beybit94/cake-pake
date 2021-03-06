package kz.cake.web.model;

import java.math.BigDecimal;

public class ProductFilterDto {
    private Long userId;
    private Long cityId;
    private Long sizeId;
    private Long categoryId;
    private BigDecimal fromPrice;
    private BigDecimal toPrice;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getCityId() {
        return cityId;
    }

    public void setCityId(Long cityId) {
        this.cityId = cityId;
    }

    public Long getSizeId() {
        return sizeId;
    }

    public void setSizeId(Long sizeId) {
        this.sizeId = sizeId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public BigDecimal getFromPrice() {
        return fromPrice;
    }

    public void setFromPrice(BigDecimal fromPrice) {
        this.fromPrice = fromPrice;
    }

    public BigDecimal getToPrice() {
        return toPrice;
    }

    public void setToPrice(BigDecimal toPrice) {
        this.toPrice = toPrice;
    }

    public BigDecimal getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(BigDecimal minPrice) {
        this.minPrice = minPrice;
    }

    public BigDecimal getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(BigDecimal maxPrice) {
        this.maxPrice = maxPrice;
    }

    @Override
    public String toString() {
        return "ProductFilterDto{" +
                "userId=" + userId +
                ", cityId=" + cityId +
                ", sizeId=" + sizeId +
                ", categoryId=" + categoryId +
                ", fromPrice=" + fromPrice +
                ", toPrice=" + toPrice +
                ", minPrice=" + minPrice +
                ", maxPrice=" + maxPrice +
                '}';
    }
}

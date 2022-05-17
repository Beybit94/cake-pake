package kz.cake.web.entity;

public class ProductPhoto extends Base<Long> {
    private Long productId;
    private String thumbnail;
    private String image;

    private ProductPhoto(Long productId, String thumbnail, String image) {
        this.productId = productId;
        this.thumbnail = thumbnail;
        this.image = image;
    }

    public Long getProductId() {
        return productId;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String getImage() {
        return image;
    }

    public static class Builder {
        private Long productId;
        private String thumbnail;
        private String image;

        public Builder() {
        }

        public Builder productId(Long productId) {
            this.productId = productId;
            return this;
        }

        public Builder thumbnail(String thumbnail) {
            this.thumbnail = thumbnail;
            return this;
        }

        public Builder image(String image) {
            this.image = image;
            return this;
        }

        public ProductPhoto build() {
            return new ProductPhoto(productId, thumbnail, image);
        }
    }
}

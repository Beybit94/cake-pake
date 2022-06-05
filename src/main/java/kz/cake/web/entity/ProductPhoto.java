package kz.cake.web.entity;

import kz.cake.web.entity.base.Base;

public class ProductPhoto extends Base<Long> {
    private Long productId;
    private String thumbnail;
    private String image;

    public ProductPhoto() {
        super();
    }

    private ProductPhoto(Long productId, String thumbnail, String image) {
        this.productId = productId;
        this.thumbnail = thumbnail;
        this.image = image;
    }

    @Override
    public String getTableName() {
        return "web.product_photos";
    }

    @Override
    public String getParameters() {
        return "id,product_id,thumbnail,image,active";
    }

    @Override
    public String getCreateTableSql() {
        return String.format("CREATE TABLE IF NOT EXISTS %s (" +
                "id bigserial PRIMARY KEY," +
                "product_id bigint NOT NULL," +
                "thumbnail varchar(255) NOT NULL," +
                "image varchar(255) NOT NULL," +
                "active boolean DEFAULT true not null," +
                "FOREIGN KEY (product_id) REFERENCES web.products (id)" +
                ");", getTableName());
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

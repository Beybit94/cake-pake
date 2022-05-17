package kz.cake.web.entity;

import java.time.LocalDateTime;

public class ProductComment extends Base<Long>{
    private Long userId;
    private String comment;
    private Long productId;
    private LocalDateTime date;

    private ProductComment(Long userId, String comment, Long productId, LocalDateTime date) {
        this.userId = userId;
        this.comment = comment;
        this.productId = productId;
        this.date = date;
    }

    public Long getUserId() {
        return userId;
    }

    public String getComment() {
        return comment;
    }

    public Long getProductId() {
        return productId;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public static class Builder {
        private Long userId;
        private String comment;
        private Long productId;
        private LocalDateTime date;

        public Builder(){}

        public Builder userId(Long userId) {
            this.userId = userId;
            return this;
        }

        public Builder comment(String comment) {
            this.comment = comment;
            return this;
        }

        public Builder productId(Long productId) {
            this.productId = productId;
            return this;
        }

        public Builder date(LocalDateTime date) {
            this.date = date;
            return this;
        }

        public ProductComment build() {
            return new ProductComment(userId, comment, productId, date);
        }
    }
}

package kz.cake.web.entity;

import kz.cake.web.entity.base.Base;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class ProductComment extends Base<Long> {
    private Long userId;
    private Long productId;
    private String comment;
    private Timestamp commentDate;

    public ProductComment() {
        super();
    }

    private ProductComment(Long userId, String comment, Long productId, Timestamp date) {
        this.userId = userId;
        this.comment = comment;
        this.productId = productId;
        this.commentDate = date;
    }

    @Override
    public String getTableName() {
        return "web.product_comments";
    }

    @Override
    public String getParameters() {
        return "id,active,user_id,product_id,comment,comment_date";
    }

    @Override
    public String getCreateTableSql() {
        return String.format("CREATE TABLE IF NOT EXISTS %s (" +
                "id bigserial PRIMARY KEY," +
                "user_id bigint NOT NULL," +
                "product_id bigint NOT NULL," +
                "comment text NOT NULL," +
                "comment_date timestamp NOT NULL DEFAULT now()," +
                "active boolean not null DEFAULT true," +
                "FOREIGN KEY (user_id) REFERENCES web.users (id)," +
                "FOREIGN KEY (product_id) REFERENCES web.products (id)" +
                ");", getTableName());
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

    public Timestamp getCommentDate() {
        return commentDate;
    }

    public static class Builder {
        private Long userId;
        private String comment;
        private Long productId;
        private Timestamp commentDate;

        public Builder() {
        }

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

        public Builder commentDate(Timestamp date) {
            this.commentDate = date;
            return this;
        }

        public ProductComment build() {
            return new ProductComment(userId, comment, productId, commentDate);
        }
    }

    @Override
    public String toString() {
        return "ProductComment{" +
                "userId=" + userId +
                ", productId=" + productId +
                ", comment='" + comment + '\'' +
                ", commentDate=" + commentDate +
                '}';
    }
}

package kz.cake.web.model;

import kz.cake.web.entity.User;

public class CommentDto {
    private Long productId;
    private String comment;
    private String commentDate;
    private User user;

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCommentDate() {
        return commentDate;
    }

    public void setCommentDate(String commentDate) {
        this.commentDate = commentDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "ProductCommentDto{" +
                "productId=" + productId +
                ", comment='" + comment + '\'' +
                ", commentDate='" + commentDate + '\'' +
                ", user=" + user +
                '}';
    }
}

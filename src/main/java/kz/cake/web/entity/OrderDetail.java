package kz.cake.web.entity;

public class OrderDetail extends Base<Long>{
    private Long productId;
    private Integer quantity;

    private OrderDetail(Long id, Long productId, Integer quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getProductId() {
        return productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public static class Builder{
        private Long productId;
        private Integer quantity;

        public Builder productId(Long productId){
            this.productId = productId;
            return this;
        }

        public Builder quantity(Integer quantity){
            this.quantity = quantity;
            return this;
        }

        public OrderDetail build(){
            return new OrderDetail(null, productId, quantity);
        }
    }
}

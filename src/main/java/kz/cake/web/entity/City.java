package kz.cake.web.entity;

public class City extends BaseDictionary<Long> {
    private City(String code) {
        super(code);
    }

    public static class Builder {
        private String code;

        public Builder(){}

        public Builder code(String code) {
            this.code = code;
            return this;
        }

        public City build() {
            return new City(code);
        }
    }
}

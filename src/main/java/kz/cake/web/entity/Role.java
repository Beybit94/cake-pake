package kz.cake.web.entity;

public class Role extends BaseDictionary<Long> {
    private Role(String code) {
        super(code);
    }

    public static class Builder {
        private String code;

        public Builder(){}

        public Builder code(String code) {
            this.code = code;
            return this;
        }

        public Role build() {
            return new Role(code);
        }
    }
}

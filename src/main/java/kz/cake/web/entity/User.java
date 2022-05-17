package kz.cake.web.entity;

public class User extends Base<Long> {
    private String name;
    private String password;
    private String sex;
    private String address;

    private User() {
        super();
    }

    private User(String name, String password, String sex, String address) {
        super();
        this.name = name;
        this.password = password;
        this.sex = sex;
        this.address = address;
    }

    public String getName(){
        return this.name;
    }

    public String getPassword(){
        return this.password;
    }

    public String getSex() {
        return this.sex;
    }

    public String getAddress(){
        return this.address;
    }

    public static class Builder {
        private String name;
        private String password;
        private String sex;
        private String address;

        public Builder() {
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder sex(String sex) {
            this.sex = sex;
            return this;
        }

        public Builder address(String address) {
            this.address = address;
            return this;
        }

        public User build() {
            return new User(name, password, sex, address);
        }
    }
}

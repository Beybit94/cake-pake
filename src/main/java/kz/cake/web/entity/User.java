package kz.cake.web.entity;

import kz.cake.web.entity.base.Base;

public class User extends Base<Long> {
    private String name;
    private String password;
    private String sex;
    private String address;

    public User() {
        super();
    }

    private User(String name, String password, String sex, String address) {
        super();
        this.name = name;
        this.password = password;
        this.sex = sex;
        this.address = address;
    }

    @Override
    public String getTableName(){
        return "web.users";
    }

    @Override
    public String getParameters() {
        return "id,username,password,sex,address,active";
    }

    @Override
    public String getCreateTableSql() {
        return String.format("CREATE TABLE IF NOT EXISTS %s (" +
                "id bigserial PRIMARY KEY," +
                "username varchar(150) UNIQUE NOT NULL," +
                "password varchar(20) NOT NULL," +
                "sex varchar(10) NOT NULL," +
                "address varchar(255) NOT NULL," +
                "active boolean DEFAULT true not null" +
                ");", getTableName());
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

package kz.cake.web.entity;

import kz.cake.web.entity.base.Base;

public class User extends Base<Long> {
    private String username;
    private String password;
    private String sex;
    private String address;

    public User() {
        super();
    }

    private User(Long id, boolean active, String username, String password, String sex, String address) {
        super();
        this.id = id;
        this.active = active;
        this.username = username;
        this.password = password;
        this.sex = sex;
        this.address = address;
    }

    @Override
    public String getTableName() {
        return "web.users";
    }

    @Override
    public String getParameters() {
        return "id,active,username,password,sex,address";
    }

    @Override
    public String getCreateTableSql() {
        return String.format("CREATE TABLE IF NOT EXISTS %s (" +
                "id bigserial PRIMARY KEY," +
                "username varchar(150) UNIQUE NOT NULL," +
                "password varchar(250) NOT NULL," +
                "sex varchar(10) NULL," +
                "address varchar(255) NULL," +
                "active boolean DEFAULT true not null" +
                ");", getTableName());
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public String getSex() {
        return this.sex;
    }

    public String getAddress() {
        return this.address;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public static class Builder {
        private Long id;
        private boolean active;
        private String name;
        private String password;
        private String sex;
        private String address;

        public Builder() {
        }

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder active(boolean active) {
            this.active = active;
            return this;
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
            return new User(id, active, name, password, sex, address);
        }
    }
}

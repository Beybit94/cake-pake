package kz.cake.web.entity;

import kz.cake.web.entity.base.Base;

public class UserRole extends Base<Long> {
    private Long userId;
    private Long roleId;

    public UserRole(){
        super();
    }
    public UserRole(Long userId, Long roleId) {
        this.userId = userId;
        this.roleId = roleId;
    }

    @Override
    public String getTableName() {
        return "web.user_roles";
    }

    @Override
    public String getParameters() {
        return "id,active,user_id,role_id";
    }

    @Override
    public String getCreateTableSql() {
        return String.format("CREATE TABLE IF NOT EXISTS %s (" +
                "id bigserial PRIMARY KEY," +
                "user_id bigint NOT NULL," +
                "role_id bigint NOT NULL," +
                "active boolean DEFAULT true not null," +
                "FOREIGN KEY (user_id) REFERENCES web.users (id)," +
                "FOREIGN KEY (role_id) REFERENCES web.roles (id)," +
                "UNIQUE (user_id, role_id)" +
                ");", getTableName());
    }
}

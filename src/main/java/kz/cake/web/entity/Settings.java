package kz.cake.web.entity;

import kz.cake.web.entity.base.Base;

import java.sql.Timestamp;

public class Settings extends Base<Long> {
    private Boolean isInitTables;
    private Boolean isDemoMode;
    private Timestamp demoActivatedDateTime;

    public Settings() {
        super();
    }

    private Settings(Boolean isInitTables, Boolean isDemoMode, Timestamp demoActivatedDateTime) {
        this.isInitTables = isInitTables;
        this.isDemoMode = isDemoMode;
        this.demoActivatedDateTime = demoActivatedDateTime;
    }

    public Boolean getInitTables() {
        return isInitTables;
    }

    public void setInitTables(Boolean initTables) {
        isInitTables = initTables;
    }

    public Boolean getDemoMode() {
        return isDemoMode;
    }

    public Timestamp getDemoActivatedDateTime() {
        return demoActivatedDateTime;
    }

    @Override
    public String getTableName() {
        return "web.settings";
    }

    @Override
    public String getParameters() {
        return "id,active,is_init_tables,is_demo_mode,demo_mode_datetime";
    }

    @Override
    public String getCreateTableSql() {
        return String.format("CREATE TABLE IF NOT EXISTS %s (" +
                "id bigserial PRIMARY KEY," +
                "is_init_tables boolean DEFAULT false not null," +
                "is_demo_mode boolean DEFAULT false not null," +
                "demo_mode_datetime timestamp NULL," +
                "active boolean DEFAULT true not null" +
                ");", getTableName());
    }

    public static class Builder {
        private Boolean isInitTables;
        private Boolean isDemoMode;
        private Timestamp demoActivatedDateTime;

        public Builder isInitTables(Boolean isInitTables) {
            this.isInitTables = isInitTables;
            return this;
        }

        public Builder isDemoMode(Boolean isDemoMode) {
            this.isDemoMode = isDemoMode;
            return this;
        }

        public Builder demoModeDateTime(Timestamp demoModeDateTime) {
            this.demoActivatedDateTime = demoModeDateTime;
            return this;
        }

        public Settings build() {
            return new Settings(isInitTables, isDemoMode, demoActivatedDateTime);
        }
    }
}

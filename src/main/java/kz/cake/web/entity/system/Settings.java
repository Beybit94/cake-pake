package kz.cake.web.entity.system;

import java.time.LocalDateTime;

public class Settings extends System<Long> {
    private Boolean isInitTables;
    private Boolean isDemoMode;
    private LocalDateTime demoModeDateTime;

    public Settings() {
        super();
    }

    private Settings(Boolean isInitTables, Boolean isDemoMode, LocalDateTime demoModeDateTime) {
        this.isInitTables = isInitTables;
        this.isDemoMode = isDemoMode;
        this.demoModeDateTime = demoModeDateTime;
    }

    public Boolean getInitTables() {
        return isInitTables;
    }

    public Boolean getDemoMode() {
        return isDemoMode;
    }

    public LocalDateTime getDemoModeDateTime() {
        return demoModeDateTime;
    }

    @Override
    public String getTableName() {
        return "web.settings";
    }

    @Override
    public String getParameters() {
        return "id,is_init_tables,is_demo_mode,demo_mode_datetime,active";
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
        private LocalDateTime demoModeDateTime;

        public Builder() {
        }

        public Builder isInitTables(Boolean isInitTables) {
            this.isInitTables = isInitTables;
            return this;
        }

        public Builder isDemoMode(Boolean isDemoMode) {
            this.isDemoMode = isDemoMode;
            return this;
        }

        public Builder demoModeDateTime(LocalDateTime demoModeDateTime) {
            this.demoModeDateTime = demoModeDateTime;
            return this;
        }

        public Settings build() {
            return new Settings(isInitTables, isDemoMode, demoModeDateTime);
        }
    }
}

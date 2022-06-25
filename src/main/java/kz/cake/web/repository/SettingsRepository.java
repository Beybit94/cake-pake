package kz.cake.web.repository;

import kz.cake.web.entity.Settings;
import kz.cake.web.repository.base.BaseRepository;

public class SettingsRepository extends BaseRepository<Settings> {
    public SettingsRepository() {
        supplier = () -> new Settings();
    }
}

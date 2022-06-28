package kz.cake.web.service;

import kz.cake.web.entity.Languages;
import kz.cake.web.helpers.CacheProvider;
import kz.cake.web.repository.LanguagesRepository;
import kz.cake.web.service.base.BaseService;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class LanguagesService extends BaseService<Languages, LanguagesRepository> {
    public LanguagesService() {
        this.repository = new LanguagesRepository();
    }

    public Optional<Languages> findByCode(String code) {
        if (CacheProvider.contains("Languages")) {
            List<Languages> list = CacheProvider.get("Languages");
            return list.stream()
                    .filter(m -> m.getCode().equals(code))
                    .findFirst();
        } else {
            getAll();
            Optional<Languages> item = repository.findByCode(code);
            if (!item.isPresent()) Optional.ofNullable(null);
            return Optional.of(item.get());
        }
    }

    @Override
    public void save(Languages entity) throws SQLException, IllegalAccessException {
        super.save(entity);
        CacheProvider.remove("Languages");
    }

    @Override
    public List<Languages> getAll() {
        return CacheProvider.get("Languages", () -> repository.getAll());
    }
}

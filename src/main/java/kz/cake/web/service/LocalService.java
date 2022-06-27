package kz.cake.web.service;

import kz.cake.web.entity.Languages;
import kz.cake.web.entity.Local;
import kz.cake.web.exceptions.CustomValidationException;
import kz.cake.web.helpers.CacheProvider;
import kz.cake.web.helpers.CurrentSession;
import kz.cake.web.helpers.constants.ActionNames;
import kz.cake.web.model.DictionaryDto;
import kz.cake.web.model.LocalDto;
import kz.cake.web.repository.LocalRepository;
import kz.cake.web.service.base.BaseService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class LocalService extends BaseService<Local, LocalRepository> {
    private final LanguagesService languagesService;

    public LocalService() {
        this.repository = new LocalRepository();
        languagesService = new LanguagesService();
    }

    @Override
    public void save(Local entity) {
        super.save(entity);
        CacheProvider.remove("Locals");
        CacheProvider.remove("LocalsWithLanguage");
    }

    @Override
    public void delete(Local entity) throws CustomValidationException {
        super.delete(entity);
        CacheProvider.remove("Locals");
        CacheProvider.remove("LocalsWithLanguage");
    }

    @Override
    public List<Local> getAll() {
        return CacheProvider.get("Locals", () -> repository.getAll());
    }

    public List<LocalDto> getAllWithLanguage() {
        return CacheProvider.get("LocalsWithLanguage", () -> repository.getAll().stream()
                .map(m -> {
                    Languages lang = languagesService.read(m.getLanguageId());
                    return new LocalDto(m.getId(), m.getCode(), m.getMessage(), lang.getCode());
                })
                .collect(Collectors.toList()));
    }

    public Optional<Local> findByCode(String code) {
        if (CacheProvider.contains("Locals")) {
            List<Local> list = CacheProvider.get("Locals");
            return list.stream()
                    .filter(m -> m.getCode().equals(code) && m.getLanguageId().equals(CurrentSession.Instance.getCurrentLanguageId()))
                    .findFirst();
        } else {
            getAll();
            Optional<Local> item = repository.findByCode(code);
            if (!item.isPresent()) Optional.ofNullable(null);
            return Optional.of(item.get());
        }
    }

    public Local getById(Long id) {
        if (CacheProvider.contains("Locals")) {
            List<Local> list = CacheProvider.get("Locals");
            return list.stream().filter(m -> m.getId().equals(id)).findFirst().get();
        } else {
            getAll();
            return repository.read(id);
        }
    }

    public List<Local> getAllByLanguage() {
        if (CacheProvider.contains("Locals")) {
            List<Local> list = CacheProvider.get("Locals");
            return list.stream()
                    .filter(m -> m.getLanguageId().equals(CurrentSession.Instance.getCurrentLanguageId()))
                    .collect(Collectors.toList());
        } else {
            getAll();
            return repository.getAllByLanguage();
        }
    }
}

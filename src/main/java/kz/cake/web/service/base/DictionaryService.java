package kz.cake.web.service.base;

import kz.cake.web.entity.base.BaseDictionary;
import kz.cake.web.exceptions.CustomValidationException;
import kz.cake.web.helpers.CacheProvider;
import kz.cake.web.model.DictionaryDto;
import kz.cake.web.repository.base.DictionaryRepository;
import kz.cake.web.service.LocalService;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public abstract class DictionaryService<T1 extends BaseDictionary, T2 extends DictionaryRepository> extends BaseService<T1, T2> {
    protected final LocalService localService;
    protected Supplier<T1> supplier;

    public DictionaryService() {
        this.localService = new LocalService();
    }

    public void save(DictionaryDto dictionary) throws SQLException, IllegalAccessException {
        T1 entity = supplier.get();
        entity.setId(dictionary.getId());
        entity.setActive(dictionary.isActive());
        entity.setCode(dictionary.getCode());
        super.save(entity);

        CacheProvider.remove(cacheKey());
        CacheProvider.remove(cacheKeyWithLocal());
    }

    public void delete(DictionaryDto dictionary) throws CustomValidationException {
        T1 entity = supplier.get();
        entity.setId(dictionary.getId());
        entity.setActive(dictionary.isActive());
        entity.setCode(dictionary.getCode());
        super.delete(entity);

        CacheProvider.remove(cacheKey());
        CacheProvider.remove(cacheKeyWithLocal());
    }

    public List<DictionaryDto> getDictionaryWithLocal() {
        return CacheProvider.get(cacheKeyWithLocal(), () -> {
                    List<T1> items = repository.getAll();
                    return items;
                })
                .stream()
                .map(m -> mapWithLocal(m))
                .collect(Collectors.toList());
    }

    public List<DictionaryDto> getDictionary() {
        return CacheProvider.get(cacheKey(), () -> {
                    List<T1> items = repository.getAll();
                    return items;
                })
                .stream()
                .map(m -> map(m))
                .collect(Collectors.toList());
    }

    public Optional<DictionaryDto> findByCodeWithLocal(String code) {
        if (CacheProvider.contains(cacheKeyWithLocal())) {
            List<T1> list = CacheProvider.get(cacheKeyWithLocal());
            return list.stream()
                    .filter(m -> m.getCode().equals(code))
                    .map(m -> mapWithLocal(m))
                    .findFirst();
        } else {
            getDictionaryWithLocal();
            Optional<T1> item = repository.findByCode(code);
            if (!item.isPresent()) Optional.ofNullable(null);
            return Optional.of(mapWithLocal(item.get()));
        }
    }

    public Optional<DictionaryDto> findByCode(String code) {
        if (CacheProvider.contains(cacheKey())) {
            List<T1> list = CacheProvider.get(cacheKey());
            return list.stream()
                    .filter(m -> m.getCode().equals(code))
                    .map(m -> map(m))
                    .findFirst();
        } else {
            getDictionary();
            Optional<T1> item = repository.findByCode(code);
            if (!item.isPresent()) Optional.ofNullable(null);
            return Optional.of(map(item.get()));
        }
    }

    public DictionaryDto getByIdWithLocal(Long id) {
        if (CacheProvider.contains(cacheKeyWithLocal())) {
            List<T1> list = CacheProvider.get(cacheKeyWithLocal());
            return list.stream()
                    .filter(m -> m.getId().equals(id))
                    .map(m -> mapWithLocal(m))
                    .findFirst().get();
        } else {
            getDictionaryWithLocal();
            T1 item = (T1) repository.read(id);
            return mapWithLocal(item);
        }
    }

    public DictionaryDto getById(Long id) {
        if (CacheProvider.contains(cacheKey())) {
            List<T1> list = CacheProvider.get(cacheKey());
            return list.stream()
                    .filter(m -> m.getId().equals(id))
                    .map(m -> map(m))
                    .findFirst().get();
        } else {
            getDictionary();
            T1 item = (T1) repository.read(id);
            return map(item);
        }
    }

    public abstract String cacheKey();

    public abstract String cacheKeyWithLocal();

    protected abstract DictionaryDto mapWithLocal(T1 m);

    protected abstract DictionaryDto map(T1 m);
}

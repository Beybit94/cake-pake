package kz.cake.web.service.base;

import kz.cake.web.entity.Local;
import kz.cake.web.entity.base.BaseDictionary;
import kz.cake.web.helpers.CacheProvider;
import kz.cake.web.model.DictionaryDto;
import kz.cake.web.repository.base.DictionaryRepository;
import kz.cake.web.service.LocalService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class DictionaryService<T1 extends BaseDictionary, T2 extends DictionaryRepository> extends BaseService<T1, T2> {
    private final LocalService localService;

    public DictionaryService() {
        this.localService = new LocalService();
    }

    public abstract String getCacheKey();

    @Override
    public void save(T1 entity) {
        super.save(entity);
        CacheProvider.remove(getCacheKey());
    }

    public List<DictionaryDto> getDictionaryWithLocal() {
        return CacheProvider.get(getCacheKey(), () -> {
                    List<T1> items = repository.getAll();
                    return items;
                })
                .stream()
                .map(m -> mapWithLocal(m))
                .collect(Collectors.toList());
    }

    public List<DictionaryDto> getDictionary() {
        return CacheProvider.get(getCacheKey(), () -> {
                    List<T1> items = repository.getAll();
                    return items;
                })
                .stream()
                .map(m -> map(m))
                .collect(Collectors.toList());
    }

    public Optional<DictionaryDto> findByCodeWithLocal(String code) {
        if (CacheProvider.contains(getCacheKey())) {
            List<T1> list = CacheProvider.get(getCacheKey());
            return list.stream()
                    .filter(m -> m.getCode().equals(code))
                    .map(m -> mapWithLocal(m))
                    .findFirst();
        } else {
            Optional<T1> item = repository.findByCode(code);
            if (!item.isPresent()) Optional.ofNullable(null);
            return Optional.of(mapWithLocal(item.get()));
        }
    }

    public Optional<DictionaryDto> findByCode(String code) {
        if (CacheProvider.contains(getCacheKey())) {
            List<DictionaryDto> list = CacheProvider.get(getCacheKey());
            return list.stream().filter(m -> m.getCode().equals(code)).findFirst();
        } else {
            Optional<T1> item = repository.findByCode(code);
            if (!item.isPresent()) Optional.ofNullable(null);
            return Optional.of(map(item.get()));
        }
    }

    public DictionaryDto getByIdWithLocal(Long id) {
        if (CacheProvider.contains(getCacheKey())) {
            List<T1> list = CacheProvider.get(getCacheKey());
            return list.stream()
                    .filter(m -> m.getId().equals(id))
                    .map(m -> mapWithLocal(m))
                    .findFirst().get();
        } else {
            T1 item = (T1) repository.getById(id);
            return mapWithLocal(item);
        }
    }

    public DictionaryDto getById(Long id) {
        if (CacheProvider.contains(getCacheKey())) {
            List<T1> list = CacheProvider.get(getCacheKey());
            return list.stream()
                    .filter(m -> m.getId().equals(id))
                    .map(m -> mapWithLocal(m))
                    .findFirst().get();
        } else {
            T1 item = (T1) repository.getById(id);
            return map(item);
        }
    }

    private DictionaryDto mapWithLocal(T1 m) {
        Local local = localService.getByCode(m.getCode());
        DictionaryDto dictionaryDto = new DictionaryDto();
        dictionaryDto.setId((Long) m.getId());
        dictionaryDto.setCode(m.getCode());
        dictionaryDto.setText(local.getMessage());
        dictionaryDto.setActive(m.isActive());
        return dictionaryDto;
    }

    private DictionaryDto map(T1 m) {
        DictionaryDto dictionaryDto = new DictionaryDto();
        dictionaryDto.setId((Long) m.getId());
        dictionaryDto.setCode(m.getCode());
        dictionaryDto.setActive(m.isActive());
        return dictionaryDto;
    }
}

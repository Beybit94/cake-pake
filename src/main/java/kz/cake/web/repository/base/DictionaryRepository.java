package kz.cake.web.repository.base;

import kz.cake.web.entity.base.BaseDictionary;

import java.util.Optional;

public abstract class DictionaryRepository<T extends BaseDictionary> extends BaseRepository<T>{
    public abstract Optional<T> findByCode(String code);
    public abstract T getById(Long id);
}

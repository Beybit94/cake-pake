package kz.cake.web.service;

import kz.cake.web.entity.base.Base;
import kz.cake.web.repository.base.BaseRepository;

import java.util.List;

public abstract class BaseService<T1 extends Base, T2 extends BaseRepository> {
    private final T2 repository;

    protected BaseService(T2 repository) {
        this.repository = repository;
    }

    public void create(T1 entity) {
        repository.create(entity);
    }

    public T1 read(T1 entity) {
        return (T1) repository.read(entity);
    }

    public void update(T1 t) {
        repository.update(t);
    }

    public void delete(T1 entity) {
        repository.delete(entity);
    }
    
    public List<T1> getAll() {
        return repository.getAll();
    }
}

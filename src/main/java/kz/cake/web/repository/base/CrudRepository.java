package kz.cake.web.repository.base;

import kz.cake.web.entity.base.Base;

import java.util.List;

public interface CrudRepository<T extends Base> {
    void create(T entity);
    T read();
    void update(T entity);
    void delete(T entity);
    List<T> getAll();
}

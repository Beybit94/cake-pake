package kz.cake.web.repository.base;

import kz.cake.web.entity.base.Base;

import java.util.List;

public interface CrudRepository<T extends Base> {
    T create(T t);
    T read(int id);
    T update(T t);
    void delete(int id);
    List<T> getAll();
}

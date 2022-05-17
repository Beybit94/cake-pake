package kz.cake.web.repository;

import java.util.List;

public interface CrudRepository<T> {
    T create(T t);
    T read(int id);
    T update(T t);
    void delete(int id);
    List<T> getAll();
}

package kz.cake.web.repository.base;

import kz.cake.web.entity.base.Base;
import kz.cake.web.exceptions.CustomValidationException;

import java.util.List;

public interface CrudRepository<T extends Base> {
    void create(T entity);
    T read(Long id);
    void update(T entity);
    void delete(T entity) throws CustomValidationException;
    List<T> getAll();
}

package kz.cake.web.repository.base;

import kz.cake.web.entity.base.Base;
import kz.cake.web.exceptions.CustomValidationException;

import java.sql.SQLException;
import java.util.List;

public interface CrudRepository<T extends Base> {
    T create(T entity) throws SQLException, IllegalAccessException;
    T read(Long id);
    T update(T entity) throws SQLException, IllegalAccessException;
    void delete(T entity) throws CustomValidationException;
    List<T> getAll();
}

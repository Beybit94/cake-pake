package kz.cake.web.service.base;

import kz.cake.web.entity.base.Base;
import kz.cake.web.repository.base.BaseRepository;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public abstract class BaseService<T1 extends Base, T2 extends BaseRepository> {
    public T2 repository;

    public void save(T1 entity) {
        if (entity.getId() == null || (Long) entity.getId() == 0) {
            repository.create(entity);
        } else {
            repository.update(entity);
        }
    }

    public T1 read() {
        return (T1) repository.read();
    }

    public void delete(T1 entity) {
        repository.delete(entity);
    }

    public List<T1> getAll() {
        return repository.getAll();
    }
}

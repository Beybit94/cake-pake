package kz.cake.web.service;

import kz.cake.web.entity.Local;
import kz.cake.web.repository.LocalRepository;

public class LocalService extends BaseService<Local, LocalRepository> {
    public LocalService() {
        this.repository = new LocalRepository();
    }

    public Local getByCode(String code){
        return repository.getByCode(code);
    }
}

package kz.cake.web.service;

import kz.cake.web.entity.system.Local;
import kz.cake.web.repository.LocalRepository;

public class LocalService extends BaseService<Local, LocalRepository> {
    public LocalService() {
        this.repository = new LocalRepository();
    }
}

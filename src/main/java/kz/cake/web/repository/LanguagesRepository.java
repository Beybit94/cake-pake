package kz.cake.web.repository;

import kz.cake.web.entity.Languages;
import kz.cake.web.repository.base.BaseRepository;

public class LanguagesRepository extends BaseRepository<Languages> {
    public LanguagesRepository() {
        supplier = () -> new Languages();
    }

}

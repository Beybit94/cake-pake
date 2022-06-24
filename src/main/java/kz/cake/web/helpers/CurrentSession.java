package kz.cake.web.helpers;

import kz.cake.web.model.CurrentUserDto;

public class CurrentSession {
    public static final CurrentSession Instance = new CurrentSession();
    private CurrentUserDto currentUser;
    private Long currentLanguageId;

    public void setCurrentUser(CurrentUserDto currentUser) {
        this.currentUser = currentUser;
    }

    public void setCurrentLanguageId(Long id){
        currentLanguageId = id;
    }

    public CurrentUserDto getCurrentUser() {
        return currentUser;
    }

    public Long getCurrentLanguageId() {
        return currentLanguageId;
    }

    public void clear(){
        currentUser = null;
        currentLanguageId = null;
    }
}

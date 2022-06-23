package kz.cake.web.model;

public class RoleDto {
    private Long id;
    private boolean active;
    private String code;
    private String text;

    public void setId(Long id) {
        this.id = id;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Long getId() {
        return id;
    }

    public boolean isActive() {
        return active;
    }

    public String getCode() {
        return code;
    }

    public String getText() {
        return text;
    }
}

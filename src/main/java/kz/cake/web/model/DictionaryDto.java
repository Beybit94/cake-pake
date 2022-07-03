package kz.cake.web.model;

public class DictionaryDto {
    protected Long id;
    protected boolean active;
    protected String code;
    protected String text;


    public DictionaryDto() {
    }

    public DictionaryDto(Long id, String code, boolean active) {
        this.id = id;
        this.code = code;
        this.active = active;
    }


    public Long getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public boolean isActive() {
        return active;
    }

    public String getText() {
        return text;
    }

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

    @Override
    public String toString() {
        return "DictionaryDto{" +
                "id=" + id +
                ", active=" + active +
                ", code='" + code + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}

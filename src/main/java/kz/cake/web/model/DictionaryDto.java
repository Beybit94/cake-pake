package kz.cake.web.model;

public class DictionaryDto {
    private Long id;
    private Long parent;
    private boolean active;
    private String code;
    private String text;
    private String parentCode;
    private String parentText;

    public DictionaryDto() {
    }

    public DictionaryDto(Long id, String code, boolean active) {
        this.id = id;
        this.code = code;
        this.active = active;
    }

    public DictionaryDto(Long id, Long parent, String code, boolean active) {
        this.id = id;
        this.parent = parent;
        this.code = code;
        this.active = active;
    }

    public Long getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public Long getParent() {
        return parent;
    }

    public boolean isActive() {
        return active;
    }

    public String getText() {
        return text;
    }

    public String getParentCode() {
        return parentCode;
    }

    public String getParentText() {
        return parentText;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setParent(Long parent) {
        this.parent = parent;
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

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    public void setParentText(String parentText) {
        this.parentText = parentText;
    }
}

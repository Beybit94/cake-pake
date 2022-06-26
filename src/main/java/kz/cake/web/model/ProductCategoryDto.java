package kz.cake.web.model;

public class ProductCategoryDto extends DictionaryDto {
    private Long parent;
    private String parentCode;
    private String parentText;

    public ProductCategoryDto() {
    }

    public ProductCategoryDto(Long id, Long parent, String code, boolean active) {
        this.id = id;
        this.parent = parent;
        this.code = code;
        this.active = active;
    }

    public Long getParent() {
        return parent;
    }

    public void setParent(Long parent) {
        this.parent = parent;
    }

    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    public String getParentText() {
        return parentText;
    }

    public void setParentText(String parentText) {
        this.parentText = parentText;
    }
}

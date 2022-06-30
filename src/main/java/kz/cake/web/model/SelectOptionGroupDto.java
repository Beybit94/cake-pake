package kz.cake.web.model;

import java.util.ArrayList;
import java.util.List;

public class SelectOptionGroupDto {
    private Long id;
    private String text;
    private List<SelectOptionGroupDto> children = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<SelectOptionGroupDto> getChildren() {
        return children;
    }

    public void setChildren(List<SelectOptionGroupDto> children) {
        this.children = children;
    }
}

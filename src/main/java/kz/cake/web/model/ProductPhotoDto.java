package kz.cake.web.model;

import java.io.InputStream;

public class ProductPhotoDto {
    private Long id;
    private String name;
    private String path;
    private InputStream fileContent;

    public ProductPhotoDto() {
    }

    public ProductPhotoDto(Long id, String path) {
        this.id = id;
        this.path = path;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public InputStream getFileContent() {
        return fileContent;
    }

    public void setFileContent(InputStream fileContent) {
        this.fileContent = fileContent;
    }
}

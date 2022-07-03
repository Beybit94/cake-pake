package kz.cake.web.service;

import kz.cake.web.entity.ProductPhoto;
import kz.cake.web.exceptions.CustomValidationException;
import kz.cake.web.model.PhotoDto;
import kz.cake.web.repository.ProductPhotoRepository;
import kz.cake.web.service.base.BaseService;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ProductPhotoService extends BaseService<ProductPhoto, ProductPhotoRepository> {
    private final Map<String, String> env = System.getenv();
    private final String imagePath;

    public ProductPhotoService() {
        this.repository = new ProductPhotoRepository();
        imagePath = env.get("images_path");
    }

    public ProductPhoto save(PhotoDto photo, Long productId) throws IOException, SQLException, IllegalAccessException {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DATE);

        String path = imagePath + "/" + year + "/" + month + "/" + day;
        File theDir = new File(path);
        if (!theDir.exists()) {
            theDir.mkdirs();
        }

        String fullPath = path + "/" + calendar.getTime().getTime() + ".jpeg";
        try (InputStream input = photo.getFileContent()) {
            Files.write(Path.of(fullPath), input.readAllBytes());
        }

        return super.save(new ProductPhoto.Builder()
                .productId(productId)
                .image(fullPath.replace(imagePath, ""))
                .build());
    }

    public List<ProductPhoto> getAllByProduct(Long productId) {
        return this.repository.getAllByProduct(productId);
    }

    @Override
    public void delete(ProductPhoto entity) throws CustomValidationException {
        String path = imagePath + entity.getImage();
        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }

        repository.delete(entity);
    }
}

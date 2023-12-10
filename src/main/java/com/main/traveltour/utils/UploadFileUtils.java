package com.main.traveltour.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

public class UploadFileUtils {

    public static String uploadFile(MultipartFile file) {
        if (file.isEmpty()) {
            return "Vui lòng chọn 1 file để upload";
        }

        Path path = Paths.get("src/main/resources/static/upload/");
        if (!path.toFile().exists()) {
            path.toFile().mkdirs();
        }
        try {
            InputStream inputStream = file.getInputStream();
            Files.copy(inputStream, path.resolve(Objects.requireNonNull(file.getOriginalFilename())), StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "IMG01_" + file.getOriginalFilename();
    }
}

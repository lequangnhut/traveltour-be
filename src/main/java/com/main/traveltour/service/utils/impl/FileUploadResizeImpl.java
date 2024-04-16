package com.main.traveltour.service.utils.impl;

import com.cloudinary.Cloudinary;
import com.main.traveltour.service.utils.FileUploadResize;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class FileUploadResizeImpl implements FileUploadResize {
    private final Cloudinary cloudinary;

    public FileUploadResizeImpl(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    @Override
    public String uploadFileResize(MultipartFile multipartFile) throws IOException {
        byte[] resizedImage = resizeImage(multipartFile.getBytes(), 400, 300, 1);

        return cloudinary.uploader()
                .upload(resizedImage,
                        Map.of("public_id", UUID.randomUUID().toString()))
                .get("url")
                .toString();
    }

    @Override
    public List<String> uploadFileResizeList(List<MultipartFile> multipartFiles) throws IOException {
        List<String> uploadedUrls = new ArrayList<>();

        for (MultipartFile file : multipartFiles) {
            byte[] imageData = file.getBytes();
            if (imageData == null) {
                continue;
            }

            byte[] resizedImage = resizeImage(imageData, 400, 300, 1);

            String uploadedUrl = cloudinary.uploader()
                    .upload(resizedImage,
                            Map.of("public_id", UUID.randomUUID().toString()))
                    .get("url")
                    .toString();

            uploadedUrls.add(uploadedUrl);
        }

        return uploadedUrls;
    }


    @Override
    public String uploadFileResizeAndReducedQuality(MultipartFile multipartFile) throws IOException {
        byte[] resizedImage = resizeImage(multipartFile.getBytes(), 400, 300, 0.5);

        return cloudinary.uploader()
                .upload(resizedImage,
                        Map.of("public_id", UUID.randomUUID().toString()))
                .get("url")
                .toString();
    }

    private byte[] resizeImage(byte[] imageData, int width, int height, double quality) throws IOException {
        // Đọc hình ảnh vào BufferedImage
        BufferedImage originalImage = ImageIO.read(new ByteArrayInputStream(imageData));

        // Giảm kích thước và chất lượng hình ảnh
        BufferedImage resizedImage = Thumbnails.of(originalImage)
                .size(width, height) // Kích thước mới
                .outputQuality(quality) // Chất lượng mới
                .asBufferedImage();

        // Chuyển đổi BufferedImage thành byte[]
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(resizedImage, "jpg", baos);
        return baos.toByteArray();
    }
}

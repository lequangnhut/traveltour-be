package com.main.traveltour.service.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface FileUploadResize {
    String uploadFileResize(MultipartFile multipartFile) throws IOException;
    List<String> uploadFileResizeList(List<MultipartFile> multipartFiles) throws IOException;
    String uploadFileResizeAndReducedQuality(MultipartFile multipartFile) throws IOException;
}

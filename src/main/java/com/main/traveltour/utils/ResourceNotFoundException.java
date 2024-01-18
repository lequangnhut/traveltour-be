package com.main.traveltour.utils;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


// Định nghĩa một Exception để xử lý trường hợp tài nguyên không được tìm thấy,
// và đặt mã HTTP Status Code là 404 (NOT_FOUND) khi Exception này được ném ra.
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException{
    private static  final long serialVersionUID = 1L;

    public ResourceNotFoundException(String message) {
        super(message);
    }
}

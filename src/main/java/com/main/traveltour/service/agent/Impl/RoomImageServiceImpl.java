package com.main.traveltour.service.agent.Impl;

import com.main.traveltour.entity.RoomImages;
import com.main.traveltour.entity.RoomTypes;
import com.main.traveltour.repository.RoomImagesRepository;
import com.main.traveltour.service.agent.RoomImageService;
import com.main.traveltour.service.utils.FileUploadResize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class RoomImageServiceImpl implements RoomImageService {

    @Autowired
    RoomImagesRepository roomImagesRepository;
    @Autowired
    private FileUploadResize fileUploadResize;

    @Override
    public RoomImages save(RoomImages roomImages) {
        return roomImagesRepository.save(roomImages);
    }

    @Override
    public List<RoomImages> getAllRoomsImagesByRoomId(String roomId) {
        return roomImagesRepository.findAllByRoomTypeId(roomId);
    }

    @Override
    public void deleteAllByIds(List<Integer> ids) {
        roomImagesRepository.deleteAllByIdIn(ids);
    }

    @Override
    public void saveAllImages(List<RoomImages> roomImages) {
        roomImagesRepository.saveAll(roomImages);
    }

    @Override
    public void saveAndDeleteImage(List<MultipartFile> listImages, List<Integer> imageId, String roomTypeId) {
        if(!listImages.isEmpty()) {
            List<RoomImages> roomImages = new ArrayList<>();
            for (var image : listImages) {
                try {
                    roomImages.add(RoomImages.builder()
                            .roomTypeImg(fileUploadResize.uploadFileResize(image))
                            .roomTypeImg(roomTypeId)
                            .build());
                } catch (IOException e) {
                    throw new RuntimeException("{\"message: \" \"Hình ảnh không hợp lệ vui lòng thử lại!\"}");
                }
            }
            roomImagesRepository.saveAll(roomImages);
        } else if(!imageId.isEmpty()) {
            roomImagesRepository.deleteAllByIdIn(imageId);
        } else {
            throw new IllegalStateException("{ \"message: \" \"Không có gì thay đôi!\" }");
        }
    }
}

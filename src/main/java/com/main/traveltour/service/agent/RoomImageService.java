package com.main.traveltour.service.agent;

import com.main.traveltour.entity.RoomImages;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface RoomImageService {

    RoomImages save(RoomImages roomImages);

    List<RoomImages> getAllRoomsImagesByRoomId(String roomId);

    void deleteAllByIds(List<Integer> ids);

    void saveAllImages (List<RoomImages> roomImages);

    void saveAndDeleteImage(List<MultipartFile> listImages, List<Integer> imageId, String roomTypeId);
}

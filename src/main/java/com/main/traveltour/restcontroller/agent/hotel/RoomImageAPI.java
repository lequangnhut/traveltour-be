package com.main.traveltour.restcontroller.agent.hotel;

import com.main.traveltour.entity.ResponseObject;
import com.main.traveltour.entity.RoomImages;
import com.main.traveltour.entity.RoomTypes;
import com.main.traveltour.service.agent.RoomImageService;
import com.main.traveltour.service.agent.RoomTypeService;
import com.main.traveltour.service.utils.FileUploadResize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api/v1")
public class RoomImageAPI {

    @Autowired
    private RoomImageService roomImageService;

    @Autowired
    private FileUploadResize fileUploadResize;

    @Autowired
    private DataSourceTransactionManager transactionManager;

    @Autowired
    private RoomTypeService roomTypeService;

    @GetMapping("agent/room-images/getAllImagesByRoomId")
    public ResponseObject getAllImagesByRoomId(
            @RequestParam("roomId") String roomId) {
        List<RoomImages> roomImages = roomImageService.getAllRoomsImagesByRoomId(roomId);

        if (roomImages.isEmpty()) {
            return new ResponseObject("404", "Không tìm thấy dữ liệu", null);
        } else {
            return new ResponseObject("200", "OK", roomImages);
        }
    }

    /**
     * Xóa hình ảnh của phòng
     * @param roomTypeId id của phòng
     * @param listImageDelete danh sách id của hình ảnh cần xóa
     * @return thông báo kết quả
     */
    @DeleteMapping("agent/room-images/deleteImageRoomType")
    public ResponseObject deleteImageRoomType(
            @RequestParam("roomTypeId") String roomTypeId,
            @RequestPart("listImageDelete") List<Integer> listImageDelete
    ) {
        TransactionStatus transactionStatus = null;
        try {
            transactionStatus = transactionManager.getTransaction(new DefaultTransactionDefinition());
            if(listImageDelete != null && !listImageDelete.isEmpty()) {
                roomImageService.deleteAllByIds(listImageDelete);
            }

            transactionManager.commit(transactionStatus);

            return new ResponseObject("200", "Xóa hình ảnh thành công", null);
        } catch (Exception e) {
            transactionManager.rollback(transactionStatus);
            return new ResponseObject("500", "Đã xảy ra lỗi khi thực hiện xóa hình ảnh", null);
        }
    }

    @DeleteMapping("agent/room-images/deleteImageRoomTypes")
    public ResponseEntity<String> deleteImageRoomTypes(
            @RequestParam(required = false) List<MultipartFile> listImages,
            @RequestParam(required = false) List<Integer> imageId,
            @RequestParam(required = false) String roomTypeId
    ) {
        roomImageService.saveAndDeleteImage(listImages, imageId, roomTypeId);
        return null;
    }
    /**
     * Thêm hình ảnh cho phòng
     * @param roomTypeId id của phòng
     * @param listRoomTypeImg danh sách hình ảnh cần thêm
     * @return thông báo kết quả
     * @throws IOException
     */
    @PostMapping("agent/room-images/addImageRoomType")
    public ResponseObject addImageRoomType(
            @RequestParam("roomTypeId") String roomTypeId,
            @RequestPart("listRoomTypeImg") List<MultipartFile> listRoomTypeImg
    ) throws IOException {
        TransactionStatus transactionStatus = null;
        try {
            transactionStatus = transactionManager.getTransaction(new DefaultTransactionDefinition());

            Optional<RoomTypes> roomTypes = roomTypeService.findRoomTypeById(roomTypeId);
            if (roomTypes.isPresent()) {
                RoomTypes roomType = roomTypes.get();

                for (MultipartFile roomTypeImage : listRoomTypeImg) {
                    RoomImages roomImages = new RoomImages();
                    roomImages.setRoomTypeId(roomTypeId);
                    roomImages.setRoomTypeImg(fileUploadResize.uploadFileResizeAndReducedQuality(roomTypeImage));
                    roomImages.setRoomTypesByRoomTypeId(roomType);
                    roomImageService.save(roomImages);
                }

                transactionManager.commit(transactionStatus);

                return new ResponseObject("200", "Thêm hình ảnh thành công", null);
            } else {
                return new ResponseObject("404", "Không tìm thấy phòng hiện tại!", null);
            }
        } catch (Exception e) {
            transactionManager.rollback(transactionStatus);
            return new ResponseObject("500", "Đã xảy ra lỗi khi thực hiện thay đổi hình ảnh", null);
        }
    }

}

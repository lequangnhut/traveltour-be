package com.main.traveltour.restcontroller.agent.hotel;

import com.main.traveltour.entity.ResponseObject;
import com.main.traveltour.entity.RoomImages;
import com.main.traveltour.entity.RoomTypes;
import com.main.traveltour.service.agent.RoomImageService;
import com.main.traveltour.service.agent.RoomTypeService;
import com.main.traveltour.service.utils.FileUploadResize;
import org.springframework.beans.factory.annotation.Autowired;
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
     * Phương thức cập nhật thông tin ảnh phòng
     * @param roomTypeId mã loại phòng
     * @param listImageDelete danh sách ảnh cần xóa
     * @param listRoomTypeImg danh sách ảnh cần thêm
     * @return trả về thông báo
     * @throws IOException lỗi khi thực hiện thêm ảnh
     */
    @PutMapping("agent/room-images/saveImageRoomType")
    public ResponseObject saveImageRoomType(
            @RequestParam("roomTypeId") String roomTypeId,
            @RequestPart("listImageDelete") List<Integer> listImageDelete,
            @RequestPart("listRoomTypeImg") List<MultipartFile> listRoomTypeImg
    ) throws IOException {
        TransactionStatus transactionStatus = null;
        try {
            transactionStatus = transactionManager.getTransaction(new DefaultTransactionDefinition());
            if(listImageDelete != null && !listImageDelete.isEmpty()) {
                roomImageService.deleteAllByIds(listImageDelete);
            }

            // Thêm ảnh mới

            Optional<RoomTypes> roomTypes = roomTypeService.findRoomTypeById(roomTypeId);
            if (roomTypes.isPresent()) {
                RoomTypes roomType = roomTypes.get();

                // Lặp qua từng ảnh trong danh sách và thêm vào danh sách roomImagesById
                for (MultipartFile roomTypeImage : listRoomTypeImg) {
                    RoomImages roomImages = new RoomImages();
                    roomImages.setRoomTypeId(roomTypeId);
                    roomImages.setRoomTypeImg(fileUploadResize.uploadFileResizeAndReducedQuality(roomTypeImage));
                    roomImages.setRoomTypesByRoomTypeId(roomType);
                    roomImageService.save(roomImages);
                }

                transactionManager.commit(transactionStatus);

                return new ResponseObject("200", "Thay đổi hình ảnh thành công", null);
            } else {
                return new ResponseObject("404", "Không tìm thấy phòng hiện tại!", null);
            }
        } catch (Exception e) {
            transactionManager.rollback(transactionStatus);
            return new ResponseObject("500", "Đã xảy ra lỗi khi thực hiện thay đổi hình ảnh", null);
        }
    }

}

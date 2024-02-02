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

    @PutMapping("agent/room-images/saveImageRoomType")
    public ResponseObject saveImageRoomType(
            @RequestPart("roomTypeId") String roomTypeId,
            @RequestPart("listImageDelete") List<Integer> listImageDelete,
            @RequestPart("listRoomTypeImg") List<MultipartFile> listRoomTypeImg
    ) throws IOException {
        TransactionStatus transactionStatus = null;

        boolean error = false;
        try {
            transactionStatus = transactionManager.getTransaction(new DefaultTransactionDefinition());
            roomImageService.deleteAllByIds(listImageDelete);

            transactionManager.commit(transactionStatus);
        } catch (Exception e) {
            transactionManager.rollback(transactionStatus);
            error = true;
        }


        if (error == false) {
            for (var roomTypeImage : listRoomTypeImg) {
                RoomImages roomImages = new RoomImages();
                roomImages.setRoomTypeId(roomTypeId);
                System.out.println(roomTypeId);
                roomImages.setRoomTypeImg(fileUploadResize.uploadFileResizeAndReducedQuality(roomTypeImage));
                roomImageService.save(roomImages);

            }
            return new ResponseObject("200", "Thay đổi hình ảnh thành công", null);
        } else {
            return new ResponseObject("500", "Đã xảy ra lỗi khi thực hiện thay đổi hình ảnh", null);
        }
    }
}

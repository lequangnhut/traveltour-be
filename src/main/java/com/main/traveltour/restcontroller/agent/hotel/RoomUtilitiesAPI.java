package com.main.traveltour.restcontroller.agent.hotel;

import com.main.traveltour.entity.ResponseObject;
import com.main.traveltour.entity.RoomTypes;
import com.main.traveltour.entity.RoomUtilities;
import com.main.traveltour.service.agent.RoomTypeService;
import com.main.traveltour.service.agent.RoomUtilitiesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api/v1")
public class RoomUtilitiesAPI {
    @Autowired
    private RoomUtilitiesService roomUtilitiesService;

    @Autowired
    private RoomTypeService roomTypeService;

    @GetMapping("agent/room-utilities/get-all-room-utilities")
    public ResponseObject getAllRoomUtilities() {
        List<RoomUtilities> roomUtilitiesList = roomUtilitiesService.findAllRoomUtils();

        if(roomUtilitiesList.isEmpty()){
            return new ResponseObject("404", "Không tìm thấy dữ liệu", null);
        }else{
            return new ResponseObject("200", "OK", roomUtilitiesList);
        }
    }

    @PutMapping("agent/room-utilities/update-room-utilities")
    public ResponseObject updateRoomUtilities(
            @RequestParam("roomTypeId") String roomTypeId,
            @RequestPart List<Integer> roomUtilitiesList) {

        Optional<RoomTypes> roomTypes = roomTypeService.findRoomTypeById(roomTypeId);

        if(roomTypes.isPresent()){
            List<RoomUtilities> roomUtilities = roomUtilitiesList.stream()
                    .map(roomUtilitiesService::findByRoomUtilitiesId)
                    .collect(Collectors.toList());

            roomTypes.get().setRoomUtilities(roomUtilities);
            roomTypeService.save(roomTypes.get());

            return new ResponseObject("200", "Cập nhật dịch vụ thành công", null);
        }else{
            return new ResponseObject("404", "Không tìm thấy loại phòng", null);
        }
    }
}

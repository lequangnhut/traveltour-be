package com.main.traveltour.restcontroller.agent.hotel;

import com.main.traveltour.entity.ResponseObject;
import com.main.traveltour.entity.RoomUtilities;
import com.main.traveltour.service.agent.RoomUtilitiesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api/v1")
public class RoomUtilitiesAPI {
    @Autowired
    private RoomUtilitiesService roomUtilitiesService;

    @GetMapping("agent/room-utilities/get-all-room-utilities")
    public ResponseObject getAllRoomUtilities() {
        List<RoomUtilities> roomUtilitiesList = roomUtilitiesService.findAllRoomUtils();

        if(roomUtilitiesList.isEmpty()){
            return new ResponseObject("404", "Không tìm thấy dữ liệu", null);
        }else{
            return new ResponseObject("200", "OK", roomUtilitiesList);
        }
    }
}

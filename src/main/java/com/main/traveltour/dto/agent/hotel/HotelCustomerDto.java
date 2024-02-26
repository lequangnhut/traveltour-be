package com.main.traveltour.dto.agent.hotel;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.main.traveltour.entity.*;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class HotelCustomerDto {
    private String id;
    private String hotelName;
    private String urlWebsite;
    private String phone;
    private Integer floorNumber;
    private String province;
    private String district;
    private String ward;
    private String address;
    private Timestamp dateCreated;
    private Timestamp dateDeleted;
    private Boolean isAccepted;
    private Boolean isActive;
    private Boolean isDelete;
    private String hotelAvatar;
    private int hotelTypeId;
    private int agenciesId;
    private Collection<RoomTypes> roomTypesById;
    private Collection<HotelImages> hotelImagesById;
    private HotelTypes hotelTypesByHotelTypeId;
    private Agencies agenciesByAgenciesId;
    private List<PlaceUtilities> placeUtilities = new ArrayList<>();

    public void removeDeletedRoomTypes() {
        if (roomTypesById != null) {
            // Tạo một bản sao của roomTypesById để tránh ConcurrentModificationException
            Collection<RoomTypes> updatedRoomTypes = new ArrayList<>(roomTypesById);

            for (RoomTypes roomType : roomTypesById) {
                if (roomType.getIsDeleted() != null && roomType.getIsDeleted()) {
                    updatedRoomTypes.remove(roomType);
                }
            }

            roomTypesById = updatedRoomTypes;
        }
    }

    public void filterRoomTypes(Boolean includeBreakfast, Boolean includeFreeCancellation) {
        if (includeBreakfast == null && includeFreeCancellation == null) {
            return; // Không có yêu cầu lọc, không cần thực hiện bất kỳ điều gì
        }

        if (roomTypesById != null) {
            Collection<RoomTypes> filteredRoomTypes = new ArrayList<>();

            for (RoomTypes roomType : roomTypesById) {
                boolean meetsConditions = true;

                // Kiểm tra includeBreakfast và trường breakfastIncluded của phòng
                if (includeBreakfast != null && !roomType.getBreakfastIncluded().equals(includeBreakfast)) {
                    meetsConditions = false;
                }

                // Kiểm tra includeFreeCancellation và trường freeCancellation của phòng
                if (includeFreeCancellation != null && !roomType.getFreeCancellation().equals(includeFreeCancellation)) {
                    meetsConditions = false;
                }

                if (meetsConditions) {
                    filteredRoomTypes.add(roomType);
                }
            }
            roomTypesById = filteredRoomTypes;
        }
    }


}

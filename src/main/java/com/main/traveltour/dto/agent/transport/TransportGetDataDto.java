package com.main.traveltour.dto.agent.transport;

import com.main.traveltour.entity.TransportUtilities;
import com.main.traveltour.entity.TransportationBrands;
import com.main.traveltour.entity.TransportationImage;
import com.main.traveltour.entity.TransportationTypes;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;

/**
 * DTO for {@link com.main.traveltour.entity.Transportations}
 */
@Data
public class TransportGetDataDto implements Serializable {

    String id;

    String transportationBrandId;

    Integer transportationTypeId;

    String licensePlate;

    String transportationImg;

    Integer amountSeat;

    Timestamp dateCreated;

    Boolean isActive;

    Boolean isTransportBed;

    Integer columnSeat;

    Collection<TransportationImage> transportationImagesById;

    List<TransportUtilities> transportUtilities;

    TransportationBrands transportationBrandsByTransportationBrandId;

    TransportationTypes transportationTypesByTransportationTypeId;
}
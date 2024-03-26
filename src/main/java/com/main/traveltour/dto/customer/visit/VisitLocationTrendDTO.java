package com.main.traveltour.dto.customer.visit;

import com.main.traveltour.entity.VisitLocationTickets;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Collection;

@Data
public class VisitLocationTrendDTO {
    String visitLocationId;
    String visitLocationName;
    String visitLocationImage;
    long visitCount;
    Collection<VisitLocationTickets> visitLocationTicketsById;

    public VisitLocationTrendDTO(String visitLocationId, String visitLocationName, String visitLocationImage, long visitCount) {
        this.visitLocationId = visitLocationId;
        this.visitLocationName = visitLocationName;
        this.visitLocationImage = visitLocationImage;
        this.visitCount = visitCount;
    }
}

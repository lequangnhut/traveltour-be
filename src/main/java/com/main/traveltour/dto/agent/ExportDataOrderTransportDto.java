package com.main.traveltour.dto.agent;

import com.main.traveltour.entity.TransportationSchedules;
import lombok.Data;

import java.io.Serializable;

@Data
public class ExportDataOrderTransportDto implements Serializable {

    TransportationSchedules transportationSchedules;
}

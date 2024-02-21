package com.main.traveltour.service.agent;

import com.main.traveltour.entity.TransportationImage;

import java.util.List;

public interface TransportationImageService {

    List<TransportationImage> findByTransportId(String transportId);

    TransportationImage save(TransportationImage transportationImage);

    void delete(String transportId);
}

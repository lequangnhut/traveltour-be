package com.main.traveltour.service.agent.Impl;

import com.main.traveltour.entity.TransportationImage;
import com.main.traveltour.repository.TransportationImageRepository;
import com.main.traveltour.service.agent.TransportationImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransportationImageServiceImpl implements TransportationImageService {

    @Autowired
    private TransportationImageRepository transportationImageRepository;

    @Override
    public List<TransportationImage> findByTransportId(String transportId) {
        return transportationImageRepository.findAllByTransportationId(transportId);
    }

    @Override
    public TransportationImage save(TransportationImage transportationImage) {
        return transportationImageRepository.save(transportationImage);
    }

    @Override
    public void delete(String transportId) {
        List<TransportationImage> transportationImages = findByTransportId(transportId);
        transportationImageRepository.deleteAll(transportationImages);
    }
}

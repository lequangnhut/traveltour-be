package com.main.traveltour.service.agent.Impl;

import com.main.traveltour.entity.TransportationBrands;
import com.main.traveltour.repository.TransportationBrandsRepository;
import com.main.traveltour.service.agent.TransportationBrandsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

@Service
public class TransportationBrandsServiceImpl implements TransportationBrandsService {

    @Autowired
    private TransportationBrandsRepository transportationBrandsRepository;

    @Override
    public String findMaxCode() {
        return transportationBrandsRepository.findMaxCode();
    }

    @Override
    public List<TransportationBrands> findAllByAgencyId(int agencyId) {
        return transportationBrandsRepository.findAllByAgenciesIdAndIsActiveTrue(agencyId);
    }

    @Override
    public Page<TransportationBrands> findAllCus(Pageable pageable) {
        return transportationBrandsRepository.findAllCustomer(pageable);
    }

    @Override
    public TransportationBrands findByAgencyId(int agencyId) {
        return transportationBrandsRepository.findByAgenciesId(agencyId);
    }

    @Override
    public TransportationBrands findByTransportBrandId(String transportBrandId) {
        return transportationBrandsRepository.findById(transportBrandId);
    }

    @Override
    public Page<TransportationBrands> findAllCustomerWithFilter(String searchTerm,
                                                                BigDecimal price,
                                                                String fromLocation,
                                                                String toLocation,
                                                                Date checkInDateFiller,
                                                                List<Integer> mediaTypeList,
                                                                List<String> listOfVehicleManufacturers,
                                                                Pageable pageable) {
        return transportationBrandsRepository.findAllCustomerWithFilter(searchTerm, price, fromLocation, toLocation, checkInDateFiller, mediaTypeList, listOfVehicleManufacturers, pageable);
    }

    @Override
    public List<TransportationBrands> findAllCustomerDataList() {
        return transportationBrandsRepository.findAllCustomerDataList();
    }

    @Override
    public TransportationBrands save(TransportationBrands transportationBrands) {
        return transportationBrandsRepository.save(transportationBrands);
    }

    @Override
    public Optional<TransportationBrands> findById(String id) {
        return Optional.ofNullable(transportationBrandsRepository.findById(id));
    }
}

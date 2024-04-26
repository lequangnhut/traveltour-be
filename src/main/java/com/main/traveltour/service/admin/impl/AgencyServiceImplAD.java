package com.main.traveltour.service.admin.impl;

import com.main.traveltour.entity.Agencies;
import com.main.traveltour.repository.AgenciesRepository;
import com.main.traveltour.service.admin.AgencyServiceAD;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AgencyServiceImplAD implements AgencyServiceAD {

    @Autowired
    private AgenciesRepository agenciesRepository;

    @Override
    public Long countAgency() {
        return agenciesRepository.countAgencies();
    }

    @Override
    public Long countAllWaiting() {
        return agenciesRepository.countByIsAcceptedIsOne();
    }

    @Override
    public Agencies findById(int id) {
        return agenciesRepository.findById(id);
    }

    @Override
    public Agencies checkPhone(String phone) {
        return agenciesRepository.findByPhone(phone);
    }

    @Override
    public Agencies checkTax(String tax) {
        return agenciesRepository.findByTaxId(tax);
    }

    @Override
    public Agencies save(Agencies agencies) {
        return agenciesRepository.save(agencies);
    }

    @Override
    public List<Agencies> findByIdAgencyId(int id) {
        return agenciesRepository.findAllById(id);
    }

    @Override
    public Page<Agencies> findAllAgenciesByAccepted(Pageable pageable, Boolean isActive) {
        return agenciesRepository.findAllAgenciesByIsActiveAD(pageable, isActive);
    }

    @Override
    public Page<Agencies> findAllAgenciesByAcceptedWithSearch(String searchTerm, Pageable pageable, Boolean isActive) {
        return agenciesRepository.findAllAgenciesByIsActiveWithSearchAD(searchTerm, pageable, isActive);
    }

    @Override
    public Page<Agencies> findAllAgenciesWaitingByIsAccepted(Pageable pageable, Integer isAccepted) {
        return agenciesRepository.findAllAgenciesByIsAccepted(pageable, isAccepted);
    }

    @Override
    public Page<Agencies> findAllAgenciesWaitingByIsAcceptedWithSearch(String searchTerm, Pageable pageable, Integer isAccepted) {
        return agenciesRepository.findAllAgenciesByIsAcceptedWithSearchAD(searchTerm, pageable, isAccepted);
    }

    @Override
    public List<Agencies> findFiveAgenciesNewest() {
        return agenciesRepository.find5AgenciesNewest();
    }
}

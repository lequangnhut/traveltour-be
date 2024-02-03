package com.main.traveltour.service.admin.impl;

import com.main.traveltour.entity.Agencies;
import com.main.traveltour.repository.AgenciesRepository;
import com.main.traveltour.service.admin.AgencyServiceAD;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class AgencyServiceImplAD implements AgencyServiceAD {

    @Autowired
    private AgenciesRepository agenciesRepository;

    @Override
    public Long countAllWaiting() {
        return agenciesRepository.countByIsAcceptedIsOne();
    }

    @Override
    public Page<Agencies> findAllAccepted(Pageable pageable) {
        return agenciesRepository.findByIsAcceptedIsTwoTrue(pageable);
    }

    @Override
    public Page<Agencies> findAllAcceptedButFalse(Pageable pageable) {
        return agenciesRepository.findByIsAcceptedIsTwoFalse(pageable);
    }

    @Override
    public Page<Agencies> findAllWaiting(Pageable pageable) {
        return agenciesRepository.findByIsAcceptedIsOne(pageable);
    }

    @Override
    public Page<Agencies> findAllDenied(Pageable pageable) {
        return agenciesRepository.findByIsAcceptedIsThreeTrue(pageable);
    }

    @Override
    public Page<Agencies> findAllWithSearch(int isAccepted, String searchTerm, Pageable pageable) {
        return agenciesRepository.findByIsAcceptedEqualsAndNameAgencyContainingIgnoreCase(isAccepted, searchTerm, pageable);
    }

    @Override
    public Page<Agencies> findOneTrueWithSearch(String searchTerm, Pageable pageable) {
        return agenciesRepository.findByNameAcceptOneTrue(searchTerm, pageable);
    }

    @Override
    public Page<Agencies> findTwoTrueWithSearch(String searchTerm, Pageable pageable) {
        return agenciesRepository.findByNameAcceptTwoTrue(searchTerm, pageable);
    }

    @Override
    public Page<Agencies> findTwoFalseWithSearch(String searchTerm, Pageable pageable) {
        return agenciesRepository.findByNameAcceptTwoFalse(searchTerm, pageable);
    }

    @Override
    public Page<Agencies> findThreeTrueWithSearch(String searchTerm, Pageable pageable) {
        return agenciesRepository.findByNameAcceptThreeTrue(searchTerm, pageable);
    }

    @Override
    public Agencies findbyId(int id) {
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

}

package com.main.traveltour.service.staff.staff;

import com.main.traveltour.entity.TourDetails;
import com.main.traveltour.repository.TourDetailsRepository;
import com.main.traveltour.service.staff.TourDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TourDetailsServiceImpl implements TourDetailsService {

    @Autowired
    private TourDetailsRepository tourDetailsRepository;

    @Override
    public String getMaxCodeTourDetailId() {
        return tourDetailsRepository.getMaxCodeTourDetailId();
    }

    @Override
    public List<TourDetails> findAll() {
        return tourDetailsRepository.findAll();
    }

    @Override
    public Page<TourDetails> findAll(Pageable pageable) {
        return tourDetailsRepository.findAll(pageable);
    }

    @Override
    public Page<TourDetails> findAllWithSearch(String searchTerm, Pageable pageable) {
        return tourDetailsRepository.findTourDetailsByTourNameOrFromLocationOrToLocationContainingIgnoreCase(searchTerm, pageable);
    }

    @Override
    public Optional<TourDetails> findById(String id) {
        return tourDetailsRepository.findById(id);
    }

    @Override
    public TourDetails getById(String id) {
        return tourDetailsRepository.getById(id);
    }

    @Override
    public TourDetails save(TourDetails tourDetails) {
        return tourDetailsRepository.save(tourDetails);
    }

    @Override
    public void delete(TourDetails tourDetails) {
        tourDetailsRepository.delete(tourDetails);
    }


}

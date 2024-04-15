package com.main.traveltour.repository;

import com.main.traveltour.entity.RoomBeds;
import com.main.traveltour.entity.TransportUtilities;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransportUtilitiesRepository extends JpaRepository<TransportUtilities, Integer> {

    TransportUtilities findById(int transUtilityId);

    Page<TransportUtilities> findByTitleContainingIgnoreCase(String searchTerm, Pageable pageable);

    TransportUtilities findByTitle (String name);

    @Query(value="select tu.id, tu.title, tu.description, tu.icon from transport_utilities tu " +
            "join transport_add_utilities tau on tu.id = tau.transportation_utilities_id " +
            "where tu.id = :id", nativeQuery = true)
    List<TransportUtilities> findAllByUtilityId(int id);

    TransportUtilities deleteById(int id);

    @Query(value="select tu.* from transport_utilities tu " +
            "join transport_add_utilities tau on tu.id = tau.transportation_utilities_id " +
            "where tau.transportation_id = :id", nativeQuery = true)
    List<TransportUtilities> findAllByTransportationId (String id);
}
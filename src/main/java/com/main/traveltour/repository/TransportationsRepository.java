package com.main.traveltour.repository;

import com.main.traveltour.entity.RoomTypes;
import com.main.traveltour.entity.TransportationBrands;
import com.main.traveltour.entity.Transportations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransportationsRepository extends JpaRepository<Transportations, Integer> {
    @Query("SELECT MAX(t.id) FROM Transportations t")
    String fixMaxCode();

    Optional<Transportations> findById(String id);

    Transportations findByLicensePlate(String licensePlate);

    List<Transportations> findAllByTransportationBrandId(String transportBrandId);

    @Query("SELECT t FROM Transportations t " +
            "WHERE t.transportationBrandsByTransportationBrandId.id = :brandId")
    Page<Transportations> findAllTransport(@Param("brandId") String brandId, Pageable pageable);

    @Query("SELECT t FROM Transportations t " +
            "WHERE LOWER(CAST(t.amountSeat AS STRING)) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(t.licensePlate) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(t.id) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "AND t.transportationBrandsByTransportationBrandId.id = :brandId")
    Page<Transportations> findByTransportWithSearch(@Param("brandId") String brandId, @Param("searchTerm") String searchTerm, Pageable pageable);

    List<Transportations> findAllByTransportationTypeId(int id);

    @Query("SELECT tr FROM Transportations tr " +
            "WHERE (tr.isActive = :isActive) and " +
            "(tr.transportationBrandId = :brandId)")
    Page<Transportations> findAllPostByBrand(@Param("isActive") Boolean isActive, @Param("brandId") String brandId, Pageable pageable);

    @Query("SELECT tr FROM Transportations tr " +
            "WHERE (tr.isActive = :isActive) and " +
            "(tr.transportationBrandId = :brandId)" +
            " and LOWER(tr.licensePlate) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<Transportations> findAllPostByBrandAndName(@Param("isActive") Boolean isActive, @Param("brandId") String brandId, Pageable pageable, String searchTerm);

    @Query("SELECT tr FROM Transportations tr WHERE tr.id = :id")
    Transportations findByTransId(String id);

}
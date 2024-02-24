package com.main.traveltour.repository;

import com.main.traveltour.entity.Agencies;
import com.main.traveltour.entity.TourTypes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AgenciesRepository extends JpaRepository<Agencies, Integer> {

    Agencies findByUserId(int userId);

    Agencies findById(int agencyId);

    Agencies findByPhone(String phone);

    Agencies findByTaxId(String taxId);

    //Tìm page doanh nghiệp có trạng thái chấp nhận CHỜ phê duyệt và đang hoạt động
    @Query("SELECT a FROM Agencies a WHERE a.isAccepted = 1 and a.isActive = true")
    Page<Agencies> findByIsAcceptedIsOne(Pageable pageable);

    //Tìm page doanh nghiệp có trạng thái chấp nhận CHỜ phê duyệt và đang hoạt động và tìm bằng tên
    @Query("SELECT a FROM Agencies a WHERE (a.isAccepted = 1) and (a.isActive = true) and LOWER(a.nameAgency) " +
            "LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<Agencies> findByNameAcceptOneTrue(String searchTerm, Pageable pageable);

    //Tìm page doanh nghiệp có trạng thái chấp nhận ĐÃ phê duyệt và đang hoạt động
    @Query("SELECT a FROM Agencies a WHERE a.isAccepted = 2 and a.isActive = true")
    Page<Agencies> findByIsAcceptedIsTwoTrue(Pageable pageable);

    //Tìm page doanh nghiệp có trạng thái chấp nhận ĐÃ phê duyệt và đang hoạt động và tìm bằng tên
    @Query("SELECT a FROM Agencies a WHERE (a.isAccepted = 2) and (a.isActive = true) and LOWER(a.nameAgency) " +
            "LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<Agencies> findByNameAcceptTwoTrue(String searchTerm, Pageable pageable);

    //Tìm page doanh nghiệp có trạng thái chấp nhận ĐÃ phê duyệt và NGƯNG hoạt động
    @Query("SELECT a FROM Agencies a WHERE a.isAccepted = 2 and a.isActive = false")
    Page<Agencies> findByIsAcceptedIsTwoFalse(Pageable pageable);

    //Tìm page doanh nghiệp có trạng thái chấp nhận ĐÃ phê duyệt và NGƯNG hoạt động và tìm bằng tên
    @Query("SELECT a FROM Agencies a WHERE (a.isAccepted = 2) and (a.isActive = false) and LOWER(a.nameAgency) " +
            "LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<Agencies> findByNameAcceptTwoFalse(String searchTerm, Pageable pageable);

    @Query("SELECT a FROM Agencies a WHERE a.isAccepted = 3 and a.isActive = true")
    Page<Agencies> findByIsAcceptedIsThreeTrue(Pageable pageable);

    //Tìm page doanh nghiệp có trạng thái chấp nhận ĐÃ phê duyệt và đang hoạt động và tìm bằng tên
    @Query("SELECT a FROM Agencies a WHERE (a.isAccepted = 3) and (a.isActive = true) and LOWER(a.nameAgency) " +
            "LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<Agencies> findByNameAcceptThreeTrue(String searchTerm, Pageable pageable);

    @Query("SELECT COUNT(a) FROM Agencies a WHERE a.isAccepted = 1 and a.isActive = true")
    Long countByIsAcceptedIsOne();

    Page<Agencies> findByIsAcceptedEqualsAndNameAgencyContainingIgnoreCase(int isAccepted, String searchTerm, Pageable pageable);

    List<Agencies> findAllById(int id);
}
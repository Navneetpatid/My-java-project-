package com.janaushadhi.adminservice.repository;

import com.janaushadhi.adminservice.entity.LocateDistributer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DistributorRepository extends JpaRepository <LocateDistributer,Long>{


    Optional<LocateDistributer> findByCodeOrEmail(String code, String email);

    @Query(value = "SELECT * FROM locate_distributer bg WHERE " +
            "bg.status != 2 AND (" +
            "(LOWER(bg.name_of_firm) LIKE LOWER(concat('%',:searchText,'%'))) " +
            "OR (LOWER(bg.email) LIKE LOWER(concat('%',:searchText,'%'))) "+
            "OR (LOWER(bg.distributor_address) LIKE LOWER(concat('%',:searchText,'%'))) "+
            "OR (LOWER(bg.contact_number) LIKE LOWER(concat('%',:searchText,'%'))) "+
            "OR (LOWER(bg.state_id) LIKE LOWER(concat('%',:searchText,'%'))) "+
            "OR (LOWER(bg.district_id) LIKE LOWER(concat('%',:searchText,'%'))) "+
            "OR (LOWER(bg.pin_code) LIKE LOWER(concat('%',:searchText,'%'))) "+
            "OR (LOWER(bg.code) LIKE LOWER(concat('%',:searchText,'%')))", nativeQuery = true)
    List<LocateDistributer> findAllByUserNameContainingIgnoreCaseOrEmailContainingIgnoreCase(@Param("searchText") String searchText);

    @Query(value = "SELECT * FROM locate_distributer t " +
            "WHERE t.status != 2 " +
            "ORDER BY " +
            "CASE WHEN :columnName = 'id' THEN t.id END ASC," +
            "CASE WHEN :columnName = 'name_of_firm' THEN t.name_of_firm END ASC," +
            "CASE WHEN :columnName = 'state_id' THEN t.state_id END ASC," +
            "CASE WHEN :columnName = 'district_id' THEN t.district_id END ASC," +
            "CASE WHEN :columnName = 'contact_number' THEN t.contact_number END ASC," +
            "CASE WHEN :columnName = 'email' THEN t.email END ASC," +
            "CASE WHEN :columnName = 'distributor_address' THEN t.distributor_address END ASC," +
            "CASE WHEN :columnName = 'pin_code' THEN t.pin_code END ASC," +
            "CASE WHEN :columnName = 'code' THEN t.code END ASC", nativeQuery = true)
    List<LocateDistributer> searchAndOrderByASC(@Param("columnName") String columnName);


    @Query(value = "SELECT * FROM locate_distributer t " +
            "WHERE t.status != 2 " +
            "ORDER BY " +
            "CASE WHEN :columnName = 'id' THEN t.id END DESC," +
            "CASE WHEN :columnName = 'contact_number' THEN t.contact_number END DESC," +
            "CASE WHEN :columnName = 'distributor_address' THEN t.distributor_address END DESC," +
            "CASE WHEN :columnName = 'email' THEN t.email END DESC," +
            "CASE WHEN :columnName = 'name_of_firm' THEN t.name_of_firm END DESC," +
            "CASE WHEN :columnName = 'pin_code' THEN t.pin_code END DESC," +
            "CASE WHEN :columnName = 'code' THEN t.code END DESC," +
            "CASE WHEN :columnName = 'district_id' THEN t.district_id END DESC," +
            "CASE WHEN :columnName = 'state_id' THEN t.state_id END DESC ", nativeQuery = true)
    List<LocateDistributer> searchAndOrderByDESC(@Param("columnName") String columnName);

    @Query(value = "SELECT * FROM locate_distributer bg WHERE " +
            " (LOWER(bg.name_of_firm) LIKE LOWER(concat('%',:searchText,'%'))) " +
            " OR (LOWER(bg.email) LIKE LOWER(concat('%',:searchText,'%'))) " +
            " OR (LOWER(bg.state_id) LIKE LOWER(concat('%',:searchText,'%'))) " +
            " OR (LOWER(bg.district_id) LIKE LOWER(concat('%',:searchText,'%'))) " +
            " OR (LOWER(bg.code) LIKE LOWER(concat('%',:searchText,'%'))) " +
            " OR (LOWER(bg.contact_number) LIKE LOWER(concat('%',:searchText,'%'))) " +
            " OR (LOWER(bg.distributor_address) LIKE LOWER(concat('%',:searchText,'%'))) " +
            " OR (LOWER(bg.pin_code) LIKE LOWER(concat('%',:searchText,'%'))) " +
            "AND bg.status != 2 " +
            " ORDER BY " +
            " CASE WHEN :columnName = 'id' THEN bg.id END ASC," +
            " CASE WHEN :columnName = 'name_of_firm' THEN bg.name_of_firm END ASC," +
            " CASE WHEN :columnName = 'distributor_address' THEN bg.distributor_address END ASC," +
            " CASE WHEN :columnName = 'email' THEN bg.email END ASC," +
            " CASE WHEN :columnName = 'contact_number' THEN bg.contact_number END ASC," +
            " CASE WHEN :columnName = 'pin_code' THEN bg.pin_code END ASC," +
            " CASE WHEN :columnName = 'code' THEN bg.code END ASC," +
            " CASE WHEN :columnName = 'state_id' THEN bg.state_id END ASC," +
            " CASE WHEN :columnName = 'district_id' THEN bg.district_id END ASC", nativeQuery = true)
    List<LocateDistributer> findASC(@Param("searchText") String searchText, @Param("columnName") String columnName);

    @Query(value = "SELECT * FROM locate_distributer bg WHERE " +
            " (LOWER(bg.name_of_firm) LIKE LOWER(concat('%',:searchText,'%'))) " +
            " OR (LOWER(bg.email) LIKE LOWER(concat('%',:searchText,'%'))) " +
            " OR (LOWER(bg.distributor_address) LIKE LOWER(concat('%',:searchText,'%'))) " +
            " OR (LOWER(bg.contact_number) LIKE LOWER(concat('%',:searchText,'%'))) " +
            " OR (LOWER(bg.state_id) LIKE LOWER(concat('%',:searchText,'%'))) " +
            " OR (LOWER(bg.pin_code) LIKE LOWER(concat('%',:searchText,'%'))) " +
            " OR (LOWER(bg.code) LIKE LOWER(concat('%',:searchText,'%'))) " +
            " OR (LOWER(bg.district_id) LIKE LOWER(concat('%',:searchText,'%'))) " +
            "AND bg.status != 2 " +
            " ORDER BY " +
            " CASE WHEN :columnName = 'id' THEN bg.id END DESC," +
            " CASE WHEN :columnName = 'name_of_firm' THEN bg.name_of_firm END DESC," +
            " CASE WHEN :columnName = 'contact_number' THEN bg.contact_number END DESC," +
            " CASE WHEN :columnName = 'email' THEN bg.email END DESC," +
            " CASE WHEN :columnName = 'distributor_address' THEN bg.distributor_address END DESC," +
            " CASE WHEN :columnName = 'pin_code' THEN bg.pin_code END DESC," +
            " CASE WHEN :columnName = 'code' THEN bg.code END DESC," +
            " CASE WHEN :columnName = 'state_id' THEN bg.state_id END DESC," +
            " CASE WHEN :columnName = 'district_id' THEN bg.district_id END DESC", nativeQuery = true)
    List<LocateDistributer> findDESC(@Param("searchText") String searchText,@Param("columnName") String columnName);

    @Query(value = "SELECT * FROM locate_distributer bg WHERE " +
            "LOWER(bg.name_of_firm) LIKE LOWER(concat('%', :searchText, '%')) " +
            "OR LOWER(bg.state_id) LIKE LOWER(concat('%', :searchText, '%')) " +
            "OR LOWER(bg.district_id) LIKE LOWER(concat('%', :searchText, '%')) " +
            "OR LOWER(bg.contact_number) LIKE LOWER(concat('%', :searchText, '%')) " +
            "OR LOWER(bg.pin_code) LIKE LOWER(concat('%', :searchText, '%')) " +
            "OR LOWER(bg.distributor_address) LIKE LOWER(concat('%', :searchText, '%')) " +
            "OR LOWER(bg.code) LIKE LOWER(concat('%', :searchText, '%')) " +
            "OR LOWER(bg.email) LIKE LOWER(concat('%', :searchText, '%'))"+
            "AND bg.status != 2",
             nativeQuery = true)
    Page<LocateDistributer> findAllByUserName(@Param("searchText") String searchText, Pageable pageable);

    @Query(value = "SELECT * FROM locate_distributer bg WHERE " +
            "(LOWER(bg.name_of_firm) LIKE LOWER(concat('%',:searchText,'%'))) " +
            "OR (LOWER(bg.contact_number) LIKE LOWER(concat('%',:searchText,'%'))) " +
            "OR (LOWER(bg.email) LIKE LOWER(concat('%',:searchText,'%'))) " +
            "OR (LOWER(bg.state_id) LIKE LOWER(concat('%',:searchText,'%'))) " +
            "OR (LOWER(bg.district_id) LIKE LOWER(concat('%',:searchText,'%'))) " +
            "OR (LOWER(bg.distributor_address) LIKE LOWER(concat('%',:searchText,'%'))) " +
            "OR (LOWER(bg.pin_code) LIKE LOWER(concat('%',:searchText,'%'))) " +
            "OR (LOWER(bg.code) LIKE LOWER(concat('%',:searchText,'%'))) " +
            "AND bg.status != 2 " +
            "ORDER BY " +
            "CASE WHEN :columnName = 'id' THEN bg.id END ASC," +
            "CASE WHEN :columnName = 'name_of_firm' THEN bg.name_of_firm END ASC," +
            "CASE WHEN :columnName = 'contact_number' THEN bg.contact_number END ASC," +
            "CASE WHEN :columnName = 'email' THEN bg.email END ASC," +
            "CASE WHEN :columnName = 'state_id' THEN bg.state_id END ASC," +
            "CASE WHEN :columnName = 'code' THEN bg.state_id END ASC," +
            "CASE WHEN :columnName = 'district_id' THEN bg.district_id END ASC," +
            "CASE WHEN :columnName = 'pin_code' THEN bg.pin_code END ASC", nativeQuery = true)
    Page<LocateDistributer> findASC( @Param("searchText") String searchText,@Param("columnName") String columnName, Pageable pageable);
    @Query(value = "SELECT * FROM locate_distributer t " +
            "WHERE t.status != 2 " +
            "ORDER BY " +
            "CASE WHEN :columnName = 'id' THEN t.id END DESC," +
            "CASE WHEN :columnName = 'name_of_firm' THEN t.name_of_firm END DESC," +
            "CASE WHEN :columnName = 'distributor_address' THEN t.distributor_address END DESC," +
            "CASE WHEN :columnName = 'email' THEN t.email END DESC," +
            "CASE WHEN :columnName = 'state_id' THEN t.state_id END DESC," +
            "CASE WHEN :columnName = 'pin_code' THEN t.pin_code END DESC," +
            "CASE WHEN :columnName = 'district_id' THEN t.district_id END DESC," +
            "CASE WHEN :columnName = 'code' THEN t.code END DESC ", nativeQuery = true)
    Page<LocateDistributer> searchAndOrderByDESC(@Param("columnName") String columnName, Pageable pageable);
    @Query(value = "SELECT * FROM locate_distributer t " +
            "WHERE t.status != 2 " +
            "ORDER BY " +
            "CASE WHEN :columnName = 'id' THEN t.id END ASC," +
            "CASE WHEN :columnName = 'name_of_firm' THEN t.name_of_firm END ASC," +
            "CASE WHEN :columnName = 'contact_number' THEN t.contact_number END ASC," +
            "CASE WHEN :columnName = 'email' THEN t.email END ASC," +
            "CASE WHEN :columnName = 'state_id' THEN t.state_id END ASC," +
            "CASE WHEN :columnName = 'district_id' THEN t.district_id END ASC," +
            "CASE WHEN :columnName = 'distributor_address' THEN t.distributor_address END ASC," +
            "CASE WHEN :columnName = 'code' THEN t.code END ASC," +
            "CASE WHEN :columnName = 'pin_code' THEN t.pin_code END ASC", nativeQuery = true)
    Page<LocateDistributer> searchAndOrderByASC(@Param("columnName") String columnName, Pageable pageable);

    @Query(value = "SELECT * FROM locate_distributer bg WHERE " +
            "(LOWER(bg.name_of_firm) LIKE LOWER(concat('%',:searchText,'%'))) " +
            "OR (LOWER(bg.contact_number) LIKE LOWER(concat('%',:searchText,'%'))) " +
            "OR (LOWER(bg.distributor_address) LIKE LOWER(concat('%',:searchText,'%'))) " +
            "OR (LOWER(bg.code) LIKE LOWER(concat('%',:searchText,'%'))) " +
            "OR (LOWER(bg.state_id) LIKE LOWER(concat('%',:searchText,'%'))) " +
            "OR (LOWER(bg.district_id) LIKE LOWER(concat('%',:searchText,'%'))) " +
            "OR (LOWER(bg.email) LIKE LOWER(concat('%',:searchText,'%'))) " +
            "OR (LOWER(bg.pin_code) LIKE LOWER(concat('%',:searchText,'%'))) " +
            "AND bg.status != 2 " +
            "ORDER BY " +
            "CASE WHEN :columnName = 'id' THEN bg.id END DESC," +
            "CASE WHEN :columnName = 'name_of_firm' THEN bg.name_of_firm END DESC," +
            "CASE WHEN :columnName = 'contact_number' THEN bg.contact_number END DESC," +
            "CASE WHEN :columnName = 'email' THEN bg.email END DESC," +
            "CASE WHEN :columnName = 'state_id' THEN bg.state_id END DESC," +
            "CASE WHEN :columnName = 'district_id' THEN bg.district_id END DESC," +
            "CASE WHEN :columnName = 'distributor_address' THEN bg.distributor_address END DESC," +
            "CASE WHEN :columnName = 'code' THEN bg.code END DESC," +
            "CASE WHEN :columnName = 'pin_code' THEN bg.pin_code END DESC", nativeQuery = true)
    Page<LocateDistributer> findDESC(@Param("searchText") String searchText, @Param("columnName") String columnName, Pageable pageable);


    List<LocateDistributer> findByDistrictId(Long districtId);

    Page<LocateDistributer> findAllByStateIdAndDistrictId(Long stateId, Long districtId, Pageable pageable);

    Page<LocateDistributer> findByStateId(Long stateId, Pageable pageable);

    List<LocateDistributer> findAllByStateIdAndDistrictId(Long stateId, Long districtId, Sort sort);

    List<LocateDistributer> findByStateId(Long stateId, Sort sort);

	List<LocateDistributer> findAllByStatusNot(Short status);

    List<LocateDistributer> findAllByStatus(Short status);

    Page<LocateDistributer> findAllByStatusNot(Short status, Pageable pageable);

    Page<LocateDistributer> findAllByStatus(Short status, Pageable pageable);


    List<LocateDistributer> findAllByStateIdAndDistrictIdAndPinCode(Long stateId, Long districtId, Long pinCode, Sort sort);

    List<LocateDistributer> findAllByStateIdAndPinCode(Long stateId, Long pinCode, Sort sort);
    Page<LocateDistributer> findAllByStateIdAndDistrictIdAndPinCode(Long stateId, Long districtId, Long pinCode, Pageable pageable);

    Page<LocateDistributer> findAllByStateIdAndPinCode(Long stateId, Long pinCode, Pageable pageable);

    Page<LocateDistributer> findByPinCode(Long pinCode, Pageable pageable);

   // List<LocateDistributer> findByPinCode(Long pinCode, Sort sort);

    List<LocateDistributer> findAllByPinCode(Long pinCode, Sort sort);

    List<LocateDistributer> findAllByStateId(Long stateId, Sort sort);
}

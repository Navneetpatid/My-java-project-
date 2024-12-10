package com.janaushadhi.adminservice.repository;

import com.janaushadhi.adminservice.entity.AddKendra;
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
public interface AddKendraRepository extends JpaRepository<AddKendra,Long> {
    Optional<AddKendra> findByStoreCode(String storeCode);

    @Query(value = "SELECT * FROM admin_add_kendra bg WHERE " +
            "(LOWER(bg.contact_person) LIKE LOWER(concat('%', :searchText, '%')) " +
            "OR LOWER(bg.state_id) LIKE LOWER(concat('%', :searchText, '%')) " +
            "OR LOWER(bg.district_id) LIKE LOWER(concat('%', :searchText, '%')) " +
            "OR LOWER(bg.kendra_address) LIKE LOWER(concat('%', :searchText, '%')) " +
            "OR LOWER(bg.store_code) LIKE LOWER(concat('%', :searchText, '%')) " +
            "OR LOWER(bg.pin_code) LIKE LOWER(concat('%', :searchText, '%'))) " +
            "AND bg.status != 2",
            nativeQuery = true)
    List<AddKendra> findAllBySearchText(@Param("searchText") String searchText);

    @Query(value = "SELECT * FROM admin_add_kendra t " +
            "WHERE t.status != 2 " +
            "ORDER BY " +
            "CASE :columnName " +
            "  WHEN 'id' THEN t.id " +
            "  WHEN 'contact_person' THEN t.contact_person " +
            "  WHEN 'state_id' THEN t.state_id " +
            "  WHEN 'district_id' THEN t.district_id " +
            "  WHEN 'kendra_address' THEN t.kendra_address " +
            "  WHEN 'store_code' THEN t.store_code " +
            "  WHEN 'pin_code' THEN t.pin_code " +
            "END ASC", nativeQuery = true)
    List<AddKendra> searchAndOrderByASC(@Param("columnName") String columnName);



    @Query(value = "SELECT * FROM admin_add_kendra t " +
            "WHERE t.status != 2 " +
            "ORDER BY " +
            "CASE :columnName " +
            "  WHEN 'id' THEN t.id " +
            "  WHEN 'contact_person' THEN t.contact_person " +
            "  WHEN 'state_id' THEN t.state_id " +
            "  WHEN 'district_id' THEN t.district_id " +
            "  WHEN 'kendra_address' THEN t.kendra_address " +
            "  WHEN 'store_code' THEN t.store_code " +
            "  WHEN 'pin_code' THEN t.pin_code " +
            "END DESC", nativeQuery = true)
    List<AddKendra> searchAndOrderByDESC(@Param("columnName") String columnName);


    @Query(value = "SELECT * FROM admin_add_kendra bg WHERE " +
            " (LOWER(bg.contact_person) LIKE LOWER(concat('%', :searchText, '%')) " +
            " OR LOWER(bg.state_id) LIKE LOWER(concat('%', :searchText, '%')) " +
            " OR LOWER(bg.district_id) LIKE LOWER(concat('%', :searchText, '%')) " +
            " OR LOWER(bg.kendra_address) LIKE LOWER(concat('%', :searchText, '%')) " +
            " OR LOWER(bg.store_code) LIKE LOWER(concat('%', :searchText, '%')) " +
            " OR LOWER(bg.pin_code) LIKE LOWER(concat('%', :searchText, '%'))) " +
            " AND bg.status != 2 " +
            " ORDER BY " +
            " CASE :columnName " +
            "   WHEN 'id' THEN bg.id " +
            "   WHEN 'contact_person' THEN bg.contact_person " +
            "   WHEN 'state_id' THEN bg.state_id " +
            "   WHEN 'district_id' THEN bg.district_id " +
            "   WHEN 'kendra_address' THEN bg.kendra_address " +
            "   WHEN 'store_code' THEN bg.store_code " +
            "   WHEN 'pin_code' THEN bg.pin_code " +
            " END ASC", nativeQuery = true)
    List<AddKendra> findASC(@Param("searchText") String searchText, @Param("columnName") String columnName);


    @Query(value = "SELECT * FROM admin_add_kendra bg WHERE " +
            " (LOWER(bg.contact_person) LIKE LOWER(concat('%', :searchText, '%')) " +
            " OR LOWER(bg.state_id) LIKE LOWER(concat('%', :searchText, '%')) " +
            " OR LOWER(bg.district_id) LIKE LOWER(concat('%', :searchText, '%')) " +
            " OR LOWER(bg.kendra_address) LIKE LOWER(concat('%', :searchText, '%')) " +
            " OR LOWER(bg.store_code) LIKE LOWER(concat('%', :searchText, '%')) " +
            " OR LOWER(bg.pin_code) LIKE LOWER(concat('%', :searchText, '%'))) " +
            " AND bg.status != 2 " +
            " ORDER BY " +
            " CASE :columnName " +
            "   WHEN 'id' THEN bg.id " +
            "   WHEN 'contact_person' THEN bg.contact_person " +
            "   WHEN 'state_id' THEN bg.state_id " +
            "   WHEN 'district_id' THEN bg.district_id " +
            "   WHEN 'kendra_address' THEN bg.kendra_address " +
            "   WHEN 'store_code' THEN bg.store_code " +
            "   WHEN 'pin_code' THEN bg.pin_code " +
            " END DESC", nativeQuery = true)
    List<AddKendra> findDESC(@Param("searchText") String searchText, @Param("columnName") String columnName);


    @Query(value = "SELECT * FROM admin_add_kendra bg WHERE " +
            "(LOWER(bg.contact_person) LIKE LOWER(concat('%', :searchText, '%')) " +
            "OR LOWER(bg.state_id) LIKE LOWER(concat('%', :searchText, '%')) " +
            "OR LOWER(bg.district_id) LIKE LOWER(concat('%', :searchText, '%')) " +
            "OR LOWER(bg.kendra_address) LIKE LOWER(concat('%', :searchText, '%')) " +
            "OR LOWER(bg.store_code) LIKE LOWER(concat('%', :searchText, '%')) " +
            "OR LOWER(bg.pin_code) LIKE LOWER(concat('%', :searchText, '%'))) " +
            "AND bg.status != 2",
            nativeQuery = true)
    Page<AddKendra> findAllByUserName(@Param("searchText") String searchText, Pageable pageable);

    @Query(value = "SELECT * FROM admin_add_kendra bg WHERE " +
            " (LOWER(bg.contact_person) LIKE LOWER(concat('%', :searchText, '%')) " +
            " OR LOWER(bg.state_id) LIKE LOWER(concat('%', :searchText, '%')) " +
            " OR LOWER(bg.district_id) LIKE LOWER(concat('%', :searchText, '%')) " +
            " OR LOWER(bg.kendra_address) LIKE LOWER(concat('%', :searchText, '%')) " +
            " OR LOWER(bg.store_code) LIKE LOWER(concat('%', :searchText, '%')) " +
            " OR LOWER(bg.pin_code) LIKE LOWER(concat('%', :searchText, '%'))) " +
            " AND bg.status != 2 " +
            " ORDER BY " +
            " CASE :columnName " +
            "   WHEN 'id' THEN bg.id " +
            "   WHEN 'contact_person' THEN bg.contact_person " +
            "   WHEN 'state_id' THEN bg.state_id " +
            "   WHEN 'district_id' THEN bg.district_id " +
            "   WHEN 'kendra_address' THEN bg.kendra_address " +
            "   WHEN 'store_code' THEN bg.store_code " +
            "   WHEN 'pin_code' THEN bg.pin_code " +
            " END ASC", nativeQuery = true)
    Page<AddKendra> findASC( @Param("searchText") String searchText,@Param("columnName") String columnName, Pageable pageable);
    @Query(value = "SELECT * FROM admin_add_kendra t " +
            "WHERE t.status != 2 " +
            "ORDER BY " +
            "CASE WHEN :columnName = 'id' THEN t.id END DESC," +
            "CASE WHEN :columnName = 'contact_person' THEN t.contact_person END DESC," +
            "CASE WHEN :columnName = 'state_id' THEN t.state_id END DESC," +
            "CASE WHEN :columnName = 'district_id' THEN t.district_id END DESC," +
            "CASE WHEN :columnName = 'kendra_address' THEN t.kendra_address END DESC," +
            "CASE WHEN :columnName = 'store_code' THEN t.store_code END DESC," +
            "CASE WHEN :columnName = 'pin_code' THEN t.pin_code END DESC ", nativeQuery = true)
    Page<AddKendra> searchAndOrderByDESC(@Param("columnName") String columnName, Pageable pageable);
    @Query(value = "SELECT * FROM admin_add_kendra t " +
            "WHERE t.status != 2 " +
            "ORDER BY " +
            "CASE WHEN :columnName = 'id' THEN t.id END ASC," +
            "CASE WHEN :columnName = 'contact_person' THEN t.contact_person END ASC," +
            "CASE WHEN :columnName = 'state_id' THEN t.state_id END ASC," +
            "CASE WHEN :columnName = 'district_id' THEN t.district_id END ASC," +
            "CASE WHEN :columnName = 'kendra_address' THEN t.kendra_address END ASC," +
            "CASE WHEN :columnName = 'store_code' THEN t.store_code END ASC," +
            "CASE WHEN :columnName = 'pin_code' THEN t.pin_code END ASC", nativeQuery = true)
    Page<AddKendra> searchAndOrderByASC(@Param("columnName") String columnName, Pageable pageable);
    @Query(value = "SELECT * FROM admin_add_kendra bg WHERE " +
            " (LOWER(bg.contact_person) LIKE LOWER(concat('%', :searchText, '%')) " +
            " OR LOWER(bg.state_id) LIKE LOWER(concat('%', :searchText, '%')) " +
            " OR LOWER(bg.district_id) LIKE LOWER(concat('%', :searchText, '%')) " +
            " OR LOWER(bg.kendra_address) LIKE LOWER(concat('%', :searchText, '%')) " +
            " OR LOWER(bg.store_code) LIKE LOWER(concat('%', :searchText, '%')) " +
            " OR LOWER(bg.pin_code) LIKE LOWER(concat('%', :searchText, '%'))) " +
            " AND bg.status != 2 " +
            " ORDER BY " +
            " CASE :columnName " +
            "   WHEN 'id' THEN bg.id " +
            "   WHEN 'contact_person' THEN bg.contact_person " +
            "   WHEN 'state_id' THEN bg.state_id " +
            "   WHEN 'district_id' THEN bg.district_id " +
            "   WHEN 'kendra_address' THEN bg.kendra_address " +
            "   WHEN 'store_code' THEN bg.store_code " +
            "   WHEN 'pin_code' THEN bg.pin_code " +
            " END DESC", nativeQuery = true)
    Page<AddKendra> findDESC(@Param("searchText") String searchText, @Param("columnName") String columnName, Pageable pageable);


    Page<AddKendra> findAllByStateIdAndDistrictId(Long stateId, Long districtId, Pageable pageable);
    List<AddKendra> findAllByStateIdAndDistrictId(Long stateId, Long districtId, Sort sort);
    List<AddKendra> findAllByStateId(Long stateId, Sort sort);
    Page<AddKendra> findAllByStateId(Long stateId, Pageable pageable);


    @Query(value = "SELECT * FROM admin_add_kendra bg WHERE bg.status != 2", nativeQuery = true)
    Page<AddKendra> findAllWhereStatusNotTwo(Pageable pageable);


    @Query(value = "SELECT * FROM admin_add_kendra bg WHERE bg.status != 2", nativeQuery = true)
    List<AddKendra> findAllWhereStatusNotTwo();

    List<AddKendra> findAllByStatus(Short status);

    Page<AddKendra> findAllByStatus(Short status, Pageable pageable);

    List<AddKendra> findAllByStateIdAndDistrictIdAndPinCode(Long stateId, Long districtId, Long pinCode, Sort sort);


    List<AddKendra> findAllByStateIdAndPinCode(Long stateId, Long pinCode, Sort sort);

    Page<AddKendra> findAllByStateIdAndDistrictIdAndPinCode(Long stateId, Long districtId, Long pinCode, Pageable pageable);

    Page<AddKendra> findAllByStateIdAndPinCode(Long stateId, Long pinCode, Pageable pageable);

    Page<AddKendra> findAllByPinCode(Long pinCode, Pageable pageable);

    List<AddKendra> findAllByPinCode(Long pinCode, Sort sort);
}

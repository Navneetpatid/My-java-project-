package com.janaushadhi.adminservice.repository;

import com.janaushadhi.adminservice.entity.RevocationOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RevocationRepository extends JpaRepository<RevocationOrder,Long> {


    @Query(value = "SELECT * FROM revocation_order bg WHERE " +
            "bg.status != 2 AND (" +
            "(LOWER(bg.drug_code) LIKE LOWER(concat('%',:searchText,'%'))) " +
            "OR (LOWER(bg.drug_name) LIKE LOWER(concat('%',:searchText,'%'))) "+
            "OR (LOWER(bg.black_list_date) LIKE LOWER(concat('%',:searchText,'%'))) "+
            "OR (LOWER(bg.reason) LIKE LOWER(concat('%',:searchText,'%'))) "+
            "OR (LOWER(bg.manufactured_by) LIKE LOWER(concat('%',:searchText,'%')))", nativeQuery = true)
    List<RevocationOrder> findAllByUserNameContainingIgnoreCaseOrEmailContainingIgnoreCase(@Param("searchText") String searchText);

    @Query(value = "SELECT * FROM revocation_order t " +
            "WHERE t.status != 2 " +
            "ORDER BY " +
            "CASE WHEN :columnName = 'id' THEN t.id END ASC," +
            "CASE WHEN :columnName = 'drug_code' THEN t.drug_code END ASC," +
            "CASE WHEN :columnName = 'drug_name' THEN t.drug_name END ASC," +
            "CASE WHEN :columnName = 'black_list_date' THEN t.black_list_date END ASC," +
            "CASE WHEN :columnName = 'manufactured_by' THEN t.manufactured_by END ASC," +
            "CASE WHEN :columnName = 'reason' THEN t.reason END ASC", nativeQuery = true)
    List<RevocationOrder> searchAndOrderByASC(@Param("columnName") String columnName);


    @Query(value = "SELECT * FROM revocation_order t " +
            "WHERE t.status != 2 " +
            "ORDER BY " +
            "CASE WHEN :columnName = 'id' THEN t.id END DESC," +
            "CASE WHEN :columnName = 'drug_code' THEN t.drug_code END DESC," +
            "CASE WHEN :columnName = 'drug_name' THEN t.drug_name END DESC," +
            "CASE WHEN :columnName = 'reason' THEN t.reason END DESC," +
            "CASE WHEN :columnName = 'manufactured_by' THEN t.manufactured_by END DESC," +
            "CASE WHEN :columnName = 'black_list_date' THEN t.black_list_date END DESC ", nativeQuery = true)
    List<RevocationOrder> searchAndOrderByDESC(@Param("columnName") String columnName);

    @Query(value = "SELECT * FROM revocation_order bg WHERE " +
            "(LOWER(bg.drug_name) LIKE LOWER(CONCAT('%', :searchText, '%')) " +
            "OR LOWER(bg.drug_code) LIKE LOWER(CONCAT('%', :searchText, '%')) " +
            "OR LOWER(bg.black_list_date) LIKE LOWER(CONCAT('%', :searchText, '%')) " +
            "OR LOWER(bg.reason) LIKE LOWER(CONCAT('%', :searchText, '%')) " +
            "OR LOWER(bg.manufactured_by) LIKE LOWER(CONCAT('%', :searchText, '%'))) " +
            "AND bg.status != 2 " +
            "ORDER BY " +
            "CASE WHEN :columnName = 'id' THEN bg.id END ASC, " +
            "CASE WHEN :columnName = 'drug_name' THEN bg.drug_name END ASC, " +
            "CASE WHEN :columnName = 'drug_code' THEN bg.drug_code END ASC, " +
            "CASE WHEN :columnName = 'black_list_date' THEN bg.black_list_date END ASC, " +
            "CASE WHEN :columnName = 'reason' THEN bg.reason END ASC, " +
            "CASE WHEN :columnName = 'manufactured_by' THEN bg.manufactured_by END ASC",
            nativeQuery = true)
    List<RevocationOrder> findASC(@Param("searchText") String searchText, @Param("columnName") String columnName);


    @Query(value = "SELECT * FROM revocation_order bg WHERE " +
            "(LOWER(bg.drug_name) LIKE LOWER(CONCAT('%', :searchText, '%')) " +
            "OR LOWER(bg.drug_code) LIKE LOWER(CONCAT('%', :searchText, '%')) " +
            "OR LOWER(bg.black_list_date) LIKE LOWER(CONCAT('%', :searchText, '%')) " +
            "OR LOWER(bg.reason) LIKE LOWER(CONCAT('%', :searchText, '%')) " +
            "OR LOWER(bg.manufactured_by) LIKE LOWER(CONCAT('%', :searchText, '%'))) " +
            "AND bg.status != 2 " +
            "ORDER BY " +
            "CASE WHEN :columnName = 'id' THEN bg.id END DESC, " +
            "CASE WHEN :columnName = 'drug_name' THEN bg.drug_name END DESC, " +
            "CASE WHEN :columnName = 'drug_code' THEN bg.drug_code END DESC, " +
            "CASE WHEN :columnName = 'black_list_date' THEN bg.black_list_date END DESC, " +
            "CASE WHEN :columnName = 'reason' THEN bg.reason END DESC, " +
            "CASE WHEN :columnName = 'manufactured_by' THEN bg.manufactured_by END DESC",
            nativeQuery = true)
    List<RevocationOrder> findDESC(@Param("searchText") String searchText,@Param("columnName") String columnName);

    @Query(value = "SELECT * FROM revocation_order bg WHERE " +
            "bg.status != 2 AND (" +
            "LOWER(bg.drug_name) LIKE LOWER(concat('%', :searchText, '%')) " +
            "OR LOWER(bg.drug_code) LIKE LOWER(concat('%', :searchText, '%')) " +
            "OR LOWER(bg.black_list_date) LIKE LOWER(concat('%', :searchText, '%')) " +
            "OR LOWER(bg.reason) LIKE LOWER(concat('%', :searchText, '%')) " +
            "OR LOWER(bg.manufactured_by) LIKE LOWER(concat('%', :searchText, '%'))", nativeQuery = true)
    Page<RevocationOrder> findAllByUserName(@Param("searchText") String searchText, Pageable pageable);

    @Query(value = "SELECT * FROM revocation_order bg WHERE " +
            "(LOWER(bg.drug_name) LIKE LOWER(CONCAT('%', :searchText, '%')) " +
            "OR LOWER(bg.drug_code) LIKE LOWER(CONCAT('%', :searchText, '%')) " +
            "OR LOWER(bg.black_list_date) LIKE LOWER(CONCAT('%', :searchText, '%')) " +
            "OR LOWER(bg.reason) LIKE LOWER(CONCAT('%', :searchText, '%')) " +
            "OR LOWER(bg.manufactured_by) LIKE LOWER(CONCAT('%', :searchText, '%'))) " +
            "AND bg.status != 2 " +
            "ORDER BY " +
            "CASE WHEN :columnName = 'id' THEN bg.id END ASC, " +
            "CASE WHEN :columnName = 'drug_name' THEN bg.drug_name END ASC, " +
            "CASE WHEN :columnName = 'drug_code' THEN bg.drug_code END ASC, " +
            "CASE WHEN :columnName = 'black_list_date' THEN bg.black_list_date END ASC, " +
            "CASE WHEN :columnName = 'reason' THEN bg.reason END ASC, " +
            "CASE WHEN :columnName = 'manufactured_by' THEN bg.manufactured_by END ASC",
            nativeQuery = true)
    Page<RevocationOrder> findASC( @Param("searchText") String searchText,@Param("columnName") String columnName, Pageable pageable);
    @Query(value = "SELECT * FROM revocation_order t " +
            "WHERE t.status != 2 " +
            "ORDER BY " +
            "CASE WHEN :columnName = 'id' THEN t.id END DESC," +
            "CASE WHEN :columnName = 'drug_name' THEN t.drug_name END DESC," +
            "CASE WHEN :columnName = 'drug_code' THEN t.drug_code END DESC," +
            "CASE WHEN :columnName = 'black_list_date' THEN t.black_list_date END DESC," +
            "CASE WHEN :columnName = 'reason' THEN t.reason END DESC," +
            "CASE WHEN :columnName = 'manufactured_by' THEN t.manufactured_by END DESC ", nativeQuery = true)
    Page<RevocationOrder> searchAndOrderByDESC(@Param("columnName") String columnName, Pageable pageable);
    @Query(value = "SELECT * FROM revocation_order t " +
            "WHERE t.status != 2 " +
            "ORDER BY " +
            "CASE WHEN :columnName = 'id' THEN t.id END ASC," +
            "CASE WHEN :columnName = 'drug_name' THEN t.drug_name END ASC," +
            "CASE WHEN :columnName = 'drug_code' THEN t.drug_code END ASC," +
            "CASE WHEN :columnName = 'black_list_date' THEN t.black_list_date END ASC," +
            "CASE WHEN :columnName = 'reason' THEN t.reason END ASC," +
            "CASE WHEN :columnName = 'manufactured_by' THEN t.manufactured_by END ASC", nativeQuery = true)
    Page<RevocationOrder> searchAndOrderByASC(@Param("columnName") String columnName, Pageable pageable);

    @Query(value = "SELECT * FROM revocation_order bg WHERE " +
            "(LOWER(bg.drug_name) LIKE LOWER(CONCAT('%', :searchText, '%')) " +
            "OR LOWER(bg.drug_code) LIKE LOWER(CONCAT('%', :searchText, '%')) " +
            "OR LOWER(bg.black_list_date) LIKE LOWER(CONCAT('%', :searchText, '%')) " +
            "OR LOWER(bg.reason) LIKE LOWER(CONCAT('%', :searchText, '%')) " +
            "OR LOWER(bg.manufactured_by) LIKE LOWER(CONCAT('%', :searchText, '%'))) " +
            "AND bg.status != 2 " +
            "ORDER BY " +
            "CASE WHEN :columnName = 'id' THEN bg.id END DESC, " +
            "CASE WHEN :columnName = 'drug_name' THEN bg.drug_name END DESC, " +
            "CASE WHEN :columnName = 'drug_code' THEN bg.drug_code END DESC, " +
            "CASE WHEN :columnName = 'black_list_date' THEN bg.black_list_date END DESC, " +
            "CASE WHEN :columnName = 'reason' THEN bg.reason END DESC, " +
            "CASE WHEN :columnName = 'manufactured_by' THEN bg.manufactured_by END DESC",
            nativeQuery = true)
    Page<RevocationOrder> findDESC(@Param("searchText") String searchText, @Param("columnName") String columnName, Pageable pageable);

    List<RevocationOrder> findAllByStatus(Short status);
    List<RevocationOrder> findAllByStatusNot(Short status);
    Page<RevocationOrder> findAllByStatus(Short status, Pageable pageable);
    Page<RevocationOrder> findAllByStatusNot(Short status, Pageable pageable);
}

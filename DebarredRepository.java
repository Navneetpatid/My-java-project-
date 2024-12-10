package com.janaushadhi.adminservice.repository;

import com.janaushadhi.adminservice.entity.DebarredList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DebarredRepository  extends JpaRepository<DebarredList,Long> {


    @Query(value = "SELECT * FROM debarred_list bg WHERE " +
            "bg.status != 2 AND (" +
            "(LOWER(bg.drug_code) LIKE LOWER(concat('%',:searchText,'%'))) " +
            "OR (LOWER(bg.drug_name) LIKE LOWER(concat('%',:searchText,'%'))) "+
            "OR (LOWER(bg.to_date) LIKE LOWER(concat('%',:searchText,'%'))) "+
            "OR (LOWER(bg.from_date) LIKE LOWER(concat('%',:searchText,'%'))) "+
            "OR (LOWER(bg.reason) LIKE LOWER(concat('%',:searchText,'%'))) "+
            "OR (LOWER(bg.manufactured_by) LIKE LOWER(concat('%',:searchText,'%')))", nativeQuery = true)
    List<DebarredList> findAllByUserNameContainingIgnoreCaseOrEmailContainingIgnoreCase(@Param("searchText") String searchText);

    @Query(value = "SELECT * FROM debarred_list t " +
            "WHERE t.status != 2 " +
            "ORDER BY " +
            "CASE WHEN :columnName = 'id' THEN t.id END ASC," +
            "CASE WHEN :columnName = 'drug_code' THEN t.drug_code END ASC," +
            "CASE WHEN :columnName = 'drug_name' THEN t.drug_name END ASC," +
            "CASE WHEN :columnName = 'to_date' THEN t.to_date END ASC," +
            "CASE WHEN :columnName = 'from_date' THEN t.from_date END ASC," +
            "CASE WHEN :columnName = 'manufactured_by' THEN t.manufactured_by END ASC," +
            "CASE WHEN :columnName = 'reason' THEN t.reason END ASC", nativeQuery = true)
    List<DebarredList> searchAndOrderByASC(@Param("columnName") String columnName);


    @Query(value = "SELECT * FROM debarred_list t " +
            "WHERE t.status != 2 " +
            "ORDER BY " +
            "CASE WHEN :columnName = 'id' THEN t.id END DESC," +
            "CASE WHEN :columnName = 'drug_code' THEN t.drug_code END DESC," +
            "CASE WHEN :columnName = 'drug_name' THEN t.drug_name END DESC," +
            "CASE WHEN :columnName = 'reason' THEN t.reason END DESC," +
            "CASE WHEN :columnName = 'manufactured_by' THEN t.manufactured_by END DESC," +
            "CASE WHEN :columnName = 'to_date' THEN t.to_date END DESC," +
            "CASE WHEN :columnName = 'from_date' THEN t.from_date END DESC ", nativeQuery = true)
    List<DebarredList> searchAndOrderByDESC(@Param("columnName") String columnName);

    @Query(value = "SELECT * FROM debarred_list bg WHERE " +
            " (LOWER(bg.drug_name) LIKE LOWER(concat('%',:searchText,'%'))) " +
            " OR (LOWER(bg.drug_code) LIKE LOWER(concat('%',:searchText,'%'))) " +
            " OR (LOWER(bg.to_date) LIKE LOWER(concat('%',:searchText,'%'))) " +
            " OR (LOWER(bg.from_date) LIKE LOWER(concat('%',:searchText,'%'))) " +
            " OR (LOWER(bg.reason) LIKE LOWER(concat('%',:searchText,'%'))) " +
            " OR (LOWER(bg.manufactured_by) LIKE LOWER(concat('%',:searchText,'%'))) " +
            "AND bg.status != 2 " +
            " ORDER BY " +
            " CASE WHEN :columnName = 'id' THEN bg.id END ASC," +
            " CASE WHEN :columnName = 'drug_name' THEN bg.drug_name END ASC," +
            " CASE WHEN :columnName = 'drug_code' THEN bg.drug_code END ASC," +
            " CASE WHEN :columnName = 'to_date' THEN bg.to_date END ASC," +
            " CASE WHEN :columnName = 'from_date' THEN bg.from_date END ASC," +
            " CASE WHEN :columnName = 'reason' THEN bg.reason END ASC," +
            " CASE WHEN :columnName = 'manufactured_by' THEN bg.manufactured_by END ASC", nativeQuery = true)
    List<DebarredList> findASC(@Param("searchText") String searchText, @Param("columnName") String columnName);

    @Query(value = "SELECT * FROM debarred_list bg WHERE " +
            " (LOWER(bg.drug_name) LIKE LOWER(concat('%',:searchText,'%'))) " +
            " OR (LOWER(bg.drug_code) LIKE LOWER(concat('%',:searchText,'%'))) " +
            " OR (LOWER(bg.to_date) LIKE LOWER(concat('%',:searchText,'%'))) " +
            " OR (LOWER(bg.from_date) LIKE LOWER(concat('%',:searchText,'%'))) " +
            " OR (LOWER(bg.reason) LIKE LOWER(concat('%',:searchText,'%'))) " +
            " OR (LOWER(bg.manufactured_by) LIKE LOWER(concat('%',:searchText,'%'))) " +
            "AND bg.status != 2 " +
            " ORDER BY " +
            " CASE WHEN :columnName = 'id' THEN bg.id END DESC," +
            " CASE WHEN :columnName = 'drug_name' THEN bg.drug_name END DESC," +
            " CASE WHEN :columnName = 'drug_code' THEN bg.drug_code END DESC," +
            " CASE WHEN :columnName = 'to_date' THEN bg.to_date END DESC," +
            " CASE WHEN :columnName = 'from_date' THEN bg.from_date END DESC," +
            " CASE WHEN :columnName = 'reason' THEN bg.reason END DESC," +
            " CASE WHEN :columnName = 'manufactured_by' THEN bg.manufactured_by END DESC", nativeQuery = true)
    List<DebarredList> findDESC(@Param("searchText") String searchText,@Param("columnName") String columnName);

    @Query(value = "SELECT * FROM debarred_list bg WHERE " +
            "bg.status != 2 AND (" +
            "(LOWER(bg.drug_code) LIKE LOWER(concat('%',:searchText,'%'))) " +
            "OR (LOWER(bg.drug_name) LIKE LOWER(concat('%',:searchText,'%'))) "+
            "OR (LOWER(bg.to_date) LIKE LOWER(concat('%',:searchText,'%'))) "+
            "OR (LOWER(bg.from_date) LIKE LOWER(concat('%',:searchText,'%'))) "+
            "OR (LOWER(bg.reason) LIKE LOWER(concat('%',:searchText,'%'))) "+
            "OR (LOWER(bg.manufactured_by) LIKE LOWER(concat('%',:searchText,'%')))", nativeQuery = true)
    Page<DebarredList> findAllByUserName(@Param("searchText") String searchText, Pageable pageable);

    @Query(value = "SELECT * FROM debarred_list bg WHERE " +
            "(LOWER(bg.drug_name) LIKE LOWER(concat('%',:searchText,'%'))) " +
            "OR (LOWER(bg.drug_code) LIKE LOWER(concat('%',:searchText,'%'))) " +
            "OR (LOWER(bg.to_date) LIKE LOWER(concat('%',:searchText,'%'))) " +
            "OR (LOWER(bg.from_date) LIKE LOWER(concat('%',:searchText,'%'))) " +
            "OR (LOWER(bg.reason) LIKE LOWER(concat('%',:searchText,'%'))) " +
            "OR (LOWER(bg.manufactured_by) LIKE LOWER(concat('%',:searchText,'%'))) " +
            "AND bg.status != 2 " +
            "ORDER BY " +
            "CASE WHEN :columnName = 'id' THEN bg.id END ASC," +
            "CASE WHEN :columnName = 'drug_name' THEN bg.drug_name END ASC," +
            "CASE WHEN :columnName = 'drug_code' THEN bg.drug_code END ASC," +
            "CASE WHEN :columnName = 'to_date' THEN bg.to_date END ASC," +
            "CASE WHEN :columnName = 'from_date' THEN bg.from_date END ASC," +
            "CASE WHEN :columnName = 'reason' THEN bg.reason END ASC," +
            "CASE WHEN :columnName = 'manufactured_by' THEN bg.manufactured_by END ASC", nativeQuery = true)
    Page<DebarredList> findASC( @Param("searchText") String searchText,@Param("columnName") String columnName, Pageable pageable);
    @Query(value = "SELECT * FROM debarred_list t " +
            "WHERE t.status != 2 " +
            "ORDER BY " +
            "CASE WHEN :columnName = 'id' THEN t.id END DESC," +
            "CASE WHEN :columnName = 'drug_name' THEN t.drug_name END DESC," +
            "CASE WHEN :columnName = 'drug_code' THEN t.drug_code END DESC," +
            "CASE WHEN :columnName = 'to_date' THEN t.to_date END DESC," +
            "CASE WHEN :columnName = 'from_date' THEN t.from_date END DESC," +
            "CASE WHEN :columnName = 'reason' THEN t.reason END DESC," +
            "CASE WHEN :columnName = 'manufactured_by' THEN t.manufactured_by END DESC ", nativeQuery = true)
    Page<DebarredList> searchAndOrderByDESC(@Param("columnName") String columnName, Pageable pageable);
    @Query(value = "SELECT * FROM debarred_list t " +
            "WHERE t.status != 2 " +
            "ORDER BY " +
            "CASE WHEN :columnName = 'id' THEN t.id END ASC," +
            "CASE WHEN :columnName = 'drug_name' THEN t.drug_name END ASC," +
            "CASE WHEN :columnName = 'drug_code' THEN t.drug_code END ASC," +
            "CASE WHEN :columnName = 'to_date' THEN t.to_date END ASC," +
            "CASE WHEN :columnName = 'from_date' THEN t.from_date END ASC," +
            "CASE WHEN :columnName = 'reason' THEN t.reason END ASC," +
            "CASE WHEN :columnName = 'manufactured_by' THEN t.manufactured_by END ASC", nativeQuery = true)
    Page<DebarredList> searchAndOrderByASC(@Param("columnName") String columnName, Pageable pageable);

    @Query(value = "SELECT * FROM debarred_list bg WHERE " +
            "(LOWER(bg.drug_name) LIKE LOWER(concat('%',:searchText,'%'))) " +
            "OR (LOWER(bg.drug_code) LIKE LOWER(concat('%',:searchText,'%'))) " +
            "OR (LOWER(bg.to_date) LIKE LOWER(concat('%',:searchText,'%'))) " +
            "OR (LOWER(bg.from_date) LIKE LOWER(concat('%',:searchText,'%'))) " +
            "OR (LOWER(bg.reason) LIKE LOWER(concat('%',:searchText,'%'))) " +
            "OR (LOWER(bg.manufactured_by) LIKE LOWER(concat('%',:searchText,'%'))) " +
            "AND bg.status != 2 " +
            "ORDER BY " +
            "CASE WHEN :columnName = 'id' THEN bg.id END DESC," +
            "CASE WHEN :columnName = 'drug_name' THEN bg.drug_name END DESC," +
            "CASE WHEN :columnName = 'drug_code' THEN bg.drug_code END DESC," +
            "CASE WHEN :columnName = 'to_date' THEN bg.to_date END DESC," +
            "CASE WHEN :columnName = 'from_date' THEN bg.from_date END DESC," +
            "CASE WHEN :columnName = 'reason' THEN bg.reason END DESC," +
            "CASE WHEN :columnName = 'manufactured_by' THEN bg.manufactured_by END DESC", nativeQuery = true)
    Page<DebarredList> findDESC(@Param("searchText") String searchText, @Param("columnName") String columnName, Pageable pageable);

    List<DebarredList> findAllByStatus(Short status);

    Page<DebarredList> findAllByStatus(Short status, Pageable pageable);

    List<DebarredList> findAllByStatusNot(Short status);

    Page<DebarredList> findAllByStatusNot(Short status, Pageable pageable);
}

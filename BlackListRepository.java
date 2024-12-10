package com.janaushadhi.adminservice.repository;

import com.janaushadhi.adminservice.entity.BlackList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlackListRepository extends JpaRepository<BlackList,Long> {


    @Query(value = "SELECT * FROM black_list bg WHERE " +
            "bg.status != 2 AND (" +
            "(LOWER(bg.drug_code) LIKE LOWER(CONCAT('%', :searchText, '%'))) " +
            "OR (LOWER(bg.drug_name) LIKE LOWER(CONCAT('%', :searchText, '%'))) " +
            "OR (LOWER(bg.to_date) LIKE LOWER(CONCAT('%', :searchText, '%'))) " +
            "OR (LOWER(bg.from_date) LIKE LOWER(CONCAT('%', :searchText, '%'))) " +
            "OR (LOWER(bg.reason) LIKE LOWER(CONCAT('%', :searchText, '%'))) " +
            "OR (LOWER(bg.manufactured_by) LIKE LOWER(CONCAT('%', :searchText, '%'))))",
            nativeQuery = true)
    List<BlackList> findAllByUserNameContainingIgnoreCaseOrEmailContainingIgnoreCase(@Param("searchText") String searchText);

    @Query(value = "SELECT * FROM black_list t " +
            "WHERE t.status != 2 " +
            "ORDER BY " +
            "CASE WHEN :columnName = 'id' THEN t.id END ASC," +
            "CASE WHEN :columnName = 'drug_code' THEN t.drug_code END ASC," +
            "CASE WHEN :columnName = 'drug_name' THEN t.drug_name END ASC," +
            "CASE WHEN :columnName = 'to_date' THEN t.to_date END ASC," +
            "CASE WHEN :columnName = 'from_date' THEN t.from_date END ASC," +
            "CASE WHEN :columnName = 'manufactured_by' THEN t.manufactured_by END ASC," +
            "CASE WHEN :columnName = 'reason' THEN t.reason END ASC", nativeQuery = true)
    List<BlackList> searchAndOrderByASC(@Param("columnName") String columnName);


    @Query(value = "SELECT * FROM black_list t " +
            "WHERE t.status != 2 " +
            "ORDER BY " +
            "CASE WHEN :columnName = 'id' THEN t.id END DESC," +
            "CASE WHEN :columnName = 'drug_code' THEN t.drug_code END DESC," +
            "CASE WHEN :columnName = 'drug_name' THEN t.drug_name END DESC," +
            "CASE WHEN :columnName = 'reason' THEN t.reason END DESC," +
            "CASE WHEN :columnName = 'manufactured_by' THEN t.manufactured_by END DESC," +
            "CASE WHEN :columnName = 'to_date' THEN t.to_date END DESC," +
            "CASE WHEN :columnName = 'from_date' THEN t.from_date END DESC ", nativeQuery = true)
    List<BlackList> searchAndOrderByDESC(@Param("columnName") String columnName);

    @Query(value = "SELECT * FROM black_list bg WHERE " +
            "(LOWER(bg.drug_name) LIKE LOWER(CONCAT('%', :searchText, '%')) " +
            "OR LOWER(bg.drug_code) LIKE LOWER(CONCAT('%', :searchText, '%')) " +
            "OR LOWER(bg.to_date) LIKE LOWER(CONCAT('%', :searchText, '%')) " +
            "OR LOWER(bg.from_date) LIKE LOWER(CONCAT('%', :searchText, '%')) " +
            "OR LOWER(bg.reason) LIKE LOWER(CONCAT('%', :searchText, '%')) " +
            "OR LOWER(bg.manufactured_by) LIKE LOWER(CONCAT('%', :searchText, '%'))) " +
            "AND bg.status != 2 " +
            "ORDER BY " +
            "CASE WHEN :columnName = 'id' THEN bg.id END ASC, " +
            "CASE WHEN :columnName = 'drug_name' THEN bg.drug_name END ASC, " +
            "CASE WHEN :columnName = 'drug_code' THEN bg.drug_code END ASC, " +
            "CASE WHEN :columnName = 'to_date' THEN bg.to_date END ASC, " +
            "CASE WHEN :columnName = 'from_date' THEN bg.from_date END ASC, " +
            "CASE WHEN :columnName = 'reason' THEN bg.reason END ASC, " +
            "CASE WHEN :columnName = 'manufactured_by' THEN bg.manufactured_by END ASC",
            nativeQuery = true)
    List<BlackList> findASC(@Param("searchText") String searchText, @Param("columnName") String columnName);

    @Query(value = "SELECT * FROM black_list bg WHERE " +
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
    List<BlackList> findDESC(@Param("searchText") String searchText,@Param("columnName") String columnName);

    @Query(value = "SELECT * FROM black_list bg WHERE " +
            "bg.status != 2 AND (" +
            "(LOWER(bg.drug_code) LIKE LOWER(CONCAT('%', :searchText, '%'))) " +
            "OR (LOWER(bg.drug_name) LIKE LOWER(CONCAT('%', :searchText, '%'))) " +
            "OR (LOWER(bg.to_date) LIKE LOWER(CONCAT('%', :searchText, '%'))) " +
            "OR (LOWER(bg.from_date) LIKE LOWER(CONCAT('%', :searchText, '%'))) " +
            "OR (LOWER(bg.reason) LIKE LOWER(CONCAT('%', :searchText, '%'))) " +
            "OR (LOWER(bg.manufactured_by) LIKE LOWER(CONCAT('%', :searchText, '%'))))",
            nativeQuery = true)
    Page<BlackList> findAllByUserName(@Param("searchText") String searchText, Pageable pageable);


    @Query(value = "SELECT * FROM black_list bg WHERE " +
            "(LOWER(bg.drug_name) LIKE LOWER(CONCAT('%', :searchText, '%')) " +
            "OR LOWER(bg.drug_code) LIKE LOWER(CONCAT('%', :searchText, '%')) " +
            "OR LOWER(bg.to_date) LIKE LOWER(CONCAT('%', :searchText, '%')) " +
            "OR LOWER(bg.from_date) LIKE LOWER(CONCAT('%', :searchText, '%')) " +
            "OR LOWER(bg.reason) LIKE LOWER(CONCAT('%', :searchText, '%')) " +
            "OR LOWER(bg.manufactured_by) LIKE LOWER(CONCAT('%', :searchText, '%'))) " +
            "AND bg.status != 2 " +
            "ORDER BY " +
            "CASE WHEN :columnName = 'id' THEN bg.id END ASC, " +
            "CASE WHEN :columnName = 'drug_name' THEN bg.drug_name END ASC, " +
            "CASE WHEN :columnName = 'drug_code' THEN bg.drug_code END ASC, " +
            "CASE WHEN :columnName = 'to_date' THEN bg.to_date END ASC, " +
            "CASE WHEN :columnName = 'from_date' THEN bg.from_date END ASC, " +
            "CASE WHEN :columnName = 'reason' THEN bg.reason END ASC, " +
            "CASE WHEN :columnName = 'manufactured_by' THEN bg.manufactured_by END ASC",
            nativeQuery = true)
    Page<BlackList> findASC( @Param("searchText") String searchText,@Param("columnName") String columnName, Pageable pageable);
    @Query(value = "SELECT * FROM black_list t " +
            "WHERE t.status != 2 " +
            "ORDER BY " +
            "CASE WHEN :columnName = 'id' THEN t.id END DESC," +
            "CASE WHEN :columnName = 'drug_name' THEN t.drug_name END DESC," +
            "CASE WHEN :columnName = 'drug_code' THEN t.drug_code END DESC," +
            "CASE WHEN :columnName = 'to_date' THEN t.to_date END DESC," +
            "CASE WHEN :columnName = 'from_date' THEN t.from_date END DESC," +
            "CASE WHEN :columnName = 'reason' THEN t.reason END DESC," +
            "CASE WHEN :columnName = 'manufactured_by' THEN t.manufactured_by END DESC ", nativeQuery = true)
    Page<BlackList> searchAndOrderByDESC(@Param("columnName") String columnName, Pageable pageable);
    @Query(value = "SELECT * FROM black_list t " +
            "WHERE t.status != 2 " +
            "ORDER BY " +
            "CASE WHEN :columnName = 'id' THEN t.id END ASC," +
            "CASE WHEN :columnName = 'drug_name' THEN t.drug_name END ASC," +
            "CASE WHEN :columnName = 'drug_code' THEN t.drug_code END ASC," +
            "CASE WHEN :columnName = 'to_date' THEN t.to_date END ASC," +
            "CASE WHEN :columnName = 'from_date' THEN t.from_date END ASC," +
            "CASE WHEN :columnName = 'reason' THEN t.reason END ASC," +
            "CASE WHEN :columnName = 'manufactured_by' THEN t.manufactured_by END ASC", nativeQuery = true)
    Page<BlackList> searchAndOrderByASC(@Param("columnName") String columnName, Pageable pageable);

    @Query(value = "SELECT * FROM black_list bg WHERE " +
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
    Page<BlackList> findDESC(@Param("searchText") String searchText, @Param("columnName") String columnName, Pageable pageable);

    List<BlackList> findAllByStatus(Short status);

    Page<BlackList> findAllByStatus(Short status, Pageable pageable);

    Page<BlackList> findAllByStatusNot(Short status, Pageable pageable);

    List<BlackList> findAllByStatusNot(Short status);
}

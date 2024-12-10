package com.janaushadhi.adminservice.repository;

import com.janaushadhi.adminservice.entity.AwardedTender;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AwardedTenderRepository extends JpaRepository <AwardedTender,Long>{



    @Query(value = "SELECT * FROM awarded_tender bg WHERE " +
            "bg.status != 2 AND (" +
            "(LOWER(bg.id) LIKE LOWER(concat('%',:searchText,'%'))) " +
            "OR (LOWER(bg.department) LIKE LOWER(concat('%',:searchText,'%'))) "+
            "OR (LOWER(bg.year_of_purchase) LIKE LOWER(concat('%',:searchText,'%')))", nativeQuery = true)
    List<AwardedTender> findAllByUserNameContainingIgnoreCaseOrEmailContainingIgnoreCase(@Param("searchText") String searchText);

    @Query(value = "SELECT * FROM awarded_tender t " +
            "WHERE t.status != 2 " +
            "ORDER BY " +
            "CASE WHEN :columnName = 'id' THEN t.id END ASC," +
            "CASE WHEN :columnName = 'department' THEN t.department END ASC," +
            "CASE WHEN :columnName = 'year_of_purchase' THEN t.year_of_purchase END ASC", nativeQuery = true)
    List<AwardedTender> searchAndOrderByASC( @Param("columnName") String columnName);
    @Query(value = "SELECT * FROM awarded_tender t " +
            "WHERE t.status != 2 " +
            "ORDER BY " +
            "CASE WHEN :columnName = 'id' THEN t.id END DESC," +
            "CASE WHEN :columnName = 'department' THEN t.department END DESC," +
            "CASE WHEN :columnName = 'year_of_purchase' THEN t.year_of_purchase END DESC", nativeQuery = true)
    List<AwardedTender> searchAndOrderByDESC( @Param("columnName") String columnName);

    @Query(value = "SELECT * FROM awarded_tender bg WHERE " +
            " (LOWER(bg.id) LIKE LOWER(concat('%',:searchText,'%'))) " +
            " OR (LOWER(bg.year_of_purchase) LIKE LOWER(concat('%',:searchText,'%'))) " +
            " OR (LOWER(bg.department) LIKE LOWER(concat('%',:searchText,'%'))) " +
            "AND bg.status != 2 " +
            " ORDER BY " +
            " CASE WHEN :columnName = 'id' THEN bg.id END ASC," +
            " CASE WHEN :columnName = 'department' THEN bg.department END ASC," +
            " CASE WHEN :columnName = 'year_of_purchase' THEN bg.year_of_purchase END ASC", nativeQuery = true)
    List<AwardedTender> findASC(@Param("searchText") String searchText, @Param("columnName") String columnName);
    @Query(value = "SELECT * FROM awarded_tender bg WHERE " +
            " (LOWER(bg.id) LIKE LOWER(concat('%',:searchText,'%'))) " +
            " OR (LOWER(bg.year_of_purchase) LIKE LOWER(concat('%',:searchText,'%'))) " +
            " OR (LOWER(bg.department) LIKE LOWER(concat('%',:searchText,'%'))) " +
            "AND bg.status != 2 " +
            " ORDER BY " +
            " CASE WHEN :columnName = 'id' THEN bg.id END DESC," +
            " CASE WHEN :columnName = 'department' THEN bg.department END DESC," +
            " CASE WHEN :columnName = 'year_of_purchase' THEN bg.year_of_purchase END DESC", nativeQuery = true)
    List<AwardedTender> findDESC(@Param("searchText") String searchText,@Param("columnName") String columnName);
    @Query(value = "SELECT * FROM awarded_tender bg WHERE " +
            "bg.status != 2 AND (" +
            "(LOWER(bg.id) LIKE LOWER(concat('%',:searchText,'%'))) " +
            "OR (LOWER(bg.department) LIKE LOWER(concat('%',:searchText,'%'))) "+
            "OR (LOWER(bg.year_of_purchase) LIKE LOWER(concat('%',:searchText,'%')))", nativeQuery = true)
    Page<AwardedTender> findAllByUserName(@Param("searchText") String searchText, Pageable pageable);
    @Query(value = "SELECT * FROM awarded_tender bg WHERE " +
            " (LOWER(bg.id) LIKE LOWER(concat('%',:searchText,'%'))) " +
            " OR (LOWER(bg.year_of_purchase) LIKE LOWER(concat('%',:searchText,'%'))) " +
            " OR (LOWER(bg.department) LIKE LOWER(concat('%',:searchText,'%'))) " +
            "AND bg.status != 2 " +
            " ORDER BY " +
            " CASE WHEN :columnName = 'id' THEN bg.id END ASC," +
            " CASE WHEN :columnName = 'department' THEN bg.department END ASC," +
            " CASE WHEN :columnName = 'year_of_purchase' THEN bg.year_of_purchase END ASC", nativeQuery = true)
    Page<AwardedTender> findASC( @Param("searchText") String searchText,@Param("columnName") String columnName, Pageable pageable);
    @Query(value = "SELECT * FROM awarded_tender t " +
            "WHERE t.status != 2 " +
            "ORDER BY " +
            "CASE WHEN :columnName = 'id' THEN t.id END DESC," +
            "CASE WHEN :columnName = 'department' THEN t.department END DESC," +
            "CASE WHEN :columnName = 'year_of_purchase' THEN t.year_of_purchase END DESC", nativeQuery = true)
    Page<AwardedTender> searchAndOrderByDESC(@Param("columnName") String columnName, Pageable pageable);
    @Query(value = "SELECT * FROM awarded_tender t " +
            "WHERE t.status != 2 " +
            "ORDER BY " +
            "CASE WHEN :columnName = 'id' THEN t.id END ASC," +
            "CASE WHEN :columnName = 'department' THEN t.department END ASC," +
            "CASE WHEN :columnName = 'year_of_purchase' THEN t.year_of_purchase END ASC", nativeQuery = true)
    Page<AwardedTender> searchAndOrderByASC(@Param("columnName") String columnName, Pageable pageable);
    @Query(value = "SELECT * FROM awarded_tender bg WHERE " +
            " (LOWER(bg.id) LIKE LOWER(concat('%',:searchText,'%'))) " +
            " OR (LOWER(bg.year_of_purchase) LIKE LOWER(concat('%',:searchText,'%'))) " +
            " OR (LOWER(bg.department) LIKE LOWER(concat('%',:searchText,'%'))) " +
            "AND bg.status != 2 " +
            " ORDER BY " +
            " CASE WHEN :columnName = 'id' THEN bg.id END DESC," +
            " CASE WHEN :columnName = 'department' THEN bg.department END DESC," +
            " CASE WHEN :columnName = 'year_of_purchase' THEN bg.year_of_purchase END DESC", nativeQuery = true)
    Page<AwardedTender> findDESC(@Param("searchText") String searchText, @Param("columnName") String columnName, Pageable pageable);

    Page<AwardedTender> findAllByStatus(Short status, Pageable pageable);

    List<AwardedTender> findAllByStatus(Short status);

    List<AwardedTender> findAllByStatusNot(Short status);

    Page<AwardedTender> findAllByStatusNot(Short status, Pageable pageable);
}



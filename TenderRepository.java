package com.janaushadhi.adminservice.repository;

import com.janaushadhi.adminservice.entity.Tender;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TenderRepository extends JpaRepository<Tender,Long> {


    @Query(value = "SELECT * FROM tender bg WHERE bg.status != 2 AND (" +
            "LOWER(bg.tender_title) LIKE LOWER(CONCAT('%', :searchText, '%')) " +
            "OR LOWER(bg.id) LIKE LOWER(CONCAT('%', :searchText, '%')) " +
            "OR LOWER(bg.publish_date) LIKE LOWER(CONCAT('%', :searchText, '%')) " +
            "OR LOWER(bg.closing_date) LIKE LOWER(CONCAT('%', :searchText, '%'))) ", nativeQuery = true)
    List<Tender> findAllBySearchTextContainingIgnoreCase(@Param("searchText") String searchText);

    @Query(value = "SELECT * FROM tender t " +
            "WHERE t.status != 2 " +
            "ORDER BY " +
            "CASE WHEN :columnName = 'id' THEN t.id END ASC," +
            "CASE WHEN :columnName = 'tender_title' THEN t.tender_title END ASC," +
            "CASE WHEN :columnName = 'publish_date' THEN t.publish_date END ASC," +
            "CASE WHEN :columnName = 'closing_date' THEN t.closing_date END ASC", nativeQuery = true)
    List<Tender> searchAndOrderByASC( @Param("columnName") String columnName);
    @Query(value = "SELECT * FROM tender t " +
            "WHERE t.status != 2 " +
            "ORDER BY " +
            "CASE WHEN :columnName = 'id' THEN t.id END DESC," +
            "CASE WHEN :columnName = 'tender_title' THEN t.tender_title END DESC," +
            "CASE WHEN :columnName = 'publish_date' THEN t.publish_date END DESC," +
            "CASE WHEN :columnName = 'closing_date' THEN t.closing_date END DESC", nativeQuery = true)
    List<Tender> searchAndOrderByDESC( @Param("columnName") String columnName);
    @Query(value = "SELECT * FROM tender bg WHERE " +
            "(LOWER(bg.tender_title) LIKE LOWER(CONCAT('%', :searchText, '%')) " +
            "OR LOWER(bg.publish_date) LIKE LOWER(CONCAT('%', :searchText, '%')) " +
            "OR LOWER(bg.id) LIKE LOWER(CONCAT('%', :searchText, '%')) " +
            "OR LOWER(bg.closing_date) LIKE LOWER(CONCAT('%', :searchText, '%'))) " +
            "AND bg.status != 2 " +
            "ORDER BY " +
            " CASE WHEN :columnName = 'id' THEN bg.id END ASC," +
            " CASE WHEN :columnName = 'tender_title' THEN bg.tender_title END ASC," +
            " CASE WHEN :columnName = 'publish_date' THEN bg.publish_date END ASC," +
            " CASE WHEN :columnName = 'closing_date' THEN bg.closing_date END ASC", nativeQuery = true)
    List<Tender> findASC(@Param("searchText") String searchText, @Param("columnName") String columnName);
    @Query(value = "SELECT * FROM tender bg WHERE " +
            "(LOWER(bg.tender_title) LIKE LOWER(CONCAT('%', :searchText, '%')) " +
            "OR LOWER(bg.publish_date) LIKE LOWER(CONCAT('%', :searchText, '%')) " +
            "OR LOWER(bg.id) LIKE LOWER(CONCAT('%', :searchText, '%')) " +
            "OR LOWER(bg.closing_date) LIKE LOWER(CONCAT('%', :searchText, '%'))) " +
            "AND bg.status != 2 " +
            "ORDER BY " +
            " CASE WHEN :columnName = 'id' THEN bg.id END DESC," +
            " CASE WHEN :columnName = 'tender_title' THEN bg.tender_title END DESC," +
            " CASE WHEN :columnName = 'publish_date' THEN bg.publish_date END DESC," +
            " CASE WHEN :columnName = 'closing_date' THEN bg.closing_date END DESC", nativeQuery = true)
    List<Tender> findDESC(@Param("searchText") String searchText,@Param("columnName") String columnName);
    @Query(value = "SELECT * FROM tender bg WHERE " +
            "AND bg.status != 2 " +
            "(LOWER(bg.tender_title) like LOWER(concat('%',?1,'%'))) " +
            "or (LOWER(bg.publish_date) like LOWER(concat('%',?1,'%'))) "+
            "or (LOWER(bg.id) like LOWER(concat('%',?1,'%'))) "+
            "or (LOWER(bg.closing_date) like LOWER(concat('%',?1,'%')))", nativeQuery = true)
    Page<Tender> findAllByUserName( @Param("searchText") String searchText, Pageable pageable);
    @Query(value = "SELECT * FROM tender bg WHERE " +
            "(LOWER(bg.tender_title) LIKE LOWER(CONCAT('%', :searchText, '%')) " +
            "OR LOWER(bg.publish_date) LIKE LOWER(CONCAT('%', :searchText, '%')) " +
            "OR LOWER(bg.id) LIKE LOWER(CONCAT('%', :searchText, '%')) " +
            "OR LOWER(bg.closing_date) LIKE LOWER(CONCAT('%', :searchText, '%'))) " +
            "AND bg.status != 2 " +
            "ORDER BY " +
            " CASE WHEN :columnName = 'id' THEN bg.id END ASC," +
            " CASE WHEN :columnName = 'tender_title' THEN bg.tender_title END ASC," +
            " CASE WHEN :columnName = 'publish_date' THEN bg.publish_date END ASC," +
            " CASE WHEN :columnName = 'closing_date' THEN bg.closing_date END ASC", nativeQuery = true)
    Page<Tender> findASC( @Param("searchText") String searchText,@Param("columnName") String columnName, Pageable pageable);
    @Query(value = "SELECT * FROM tender t " +
            "WHERE t.status != 2 " +
            "ORDER BY " +
            "CASE WHEN :columnName = 'id' THEN t.id END DESC," +
            "CASE WHEN :columnName = 'tender_title' THEN t.tender_title END DESC," +
            "CASE WHEN :columnName = 'publish_date' THEN t.publish_date END DESC," +
            "CASE WHEN :columnName = 'closing_date' THEN t.closing_date END DESC", nativeQuery = true)
    Page<Tender> searchAndOrderByDESC(@Param("columnName") String columnName, Pageable pageable);
    @Query(value = "SELECT * FROM tender t " +
            "WHERE t.status != 2 " +
            "ORDER BY " +
            "CASE WHEN :columnName = 'id' THEN t.id END ASC," +
            "CASE WHEN :columnName = 'tender_title' THEN t.tender_title END ASC," +
            "CASE WHEN :columnName = 'publish_date' THEN t.publish_date END ASC," +
            "CASE WHEN :columnName = 'closing_date' THEN t.closing_date END ASC", nativeQuery = true)
    Page<Tender> searchAndOrderByASC(@Param("columnName") String columnName, Pageable pageable);
    @Query(value = "SELECT * FROM tender bg WHERE " +
            "(LOWER(bg.tender_title) LIKE LOWER(CONCAT('%', :searchText, '%')) " +
            "OR LOWER(bg.publish_date) LIKE LOWER(CONCAT('%', :searchText, '%')) " +
            "OR LOWER(bg.id) LIKE LOWER(CONCAT('%', :searchText, '%')) " +
            "OR LOWER(bg.closing_date) LIKE LOWER(CONCAT('%', :searchText, '%'))) " +
            "AND bg.status != 2 " +
            "ORDER BY " +
            " CASE WHEN :columnName = 'id' THEN bg.id END DESC," +
            " CASE WHEN :columnName = 'tender_title' THEN bg.tender_title END DESC," +
            " CASE WHEN :columnName = 'publish_date' THEN bg.publish_date END DESC," +
            " CASE WHEN :columnName = 'closing_date' THEN bg.closing_date END DESC", nativeQuery = true)
    Page<Tender> findDESC(@Param("searchText") String searchText, @Param("columnName") String columnName, Pageable pageable);

    List<Tender> findAllByStatus(Short status);

    Page<Tender> findAllByStatus(Short status, Pageable pageable);

    Page<Tender> findAllByStatusNot(Short status, Pageable pageable);

    List<Tender> findAllByStatusNot(Short status);
}

package com.janaushadhi.adminservice.repository;

import com.janaushadhi.adminservice.entity.ContactDetail;
import com.janaushadhi.adminservice.entity.EventGallery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepositiory extends JpaRepository<EventGallery,Long> {
    @Query(value = "SELECT * FROM event_gallery bg WHERE " +
            "bg.status != 2 AND (" +
            "LOWER(bg.id) LIKE LOWER(CONCAT('%', :searchText, '%')) " +
            "OR LOWER(bg.event_title) LIKE LOWER(CONCAT('%', :searchText, '%')) " +
            "OR LOWER(bg.event_date) LIKE LOWER(CONCAT('%', :searchText, '%')) " +
            "OR LOWER(bg.event_category) LIKE LOWER(CONCAT('%', :searchText, '%')))",
            nativeQuery = true)
    List<EventGallery> findAllBySearchText(@Param("searchText") String searchText);






    @Query(value = "SELECT * FROM event_gallery t " +
            "WHERE t.status != 2 " +
            "ORDER BY " +
            "CASE WHEN :columnName = 'id' THEN t.id END ASC," +
            "CASE WHEN :columnName = 'event_title' THEN t.event_title END ASC," +
            "CASE WHEN :columnName = 'event_category' THEN t.event_category END ASC," +
            "CASE WHEN :columnName = 'event_date' THEN t.event_date END ASC", nativeQuery = true)
    List<EventGallery> searchAndOrderByASC(@Param("columnName") String columnName);


    @Query(value = "SELECT * FROM event_gallery t " +
            "WHERE t.status != 2 " +
            "ORDER BY " +
            "CASE WHEN :columnName = 'id' THEN t.id END DESC," +
            "CASE WHEN :columnName = 'event_title' THEN t.event_title END DESC," +
            "CASE WHEN :columnName = 'event_category' THEN t.event_category END DESC," +
            "CASE WHEN :columnName = 'event_date' THEN t.event_date END DESC ", nativeQuery = true)
    List<EventGallery> searchAndOrderByDESC(@Param("columnName") String columnName);

    @Query(value = "SELECT * FROM event_gallery bg WHERE " +
            "(LOWER(bg.id) LIKE LOWER(CONCAT('%', :searchText, '%')) " +
            "OR LOWER(bg.event_date) LIKE LOWER(CONCAT('%', :searchText, '%')) " +
            "OR LOWER(bg.event_category) LIKE LOWER(CONCAT('%', :searchText, '%')) " +
            "OR LOWER(bg.event_title) LIKE LOWER(CONCAT('%', :searchText, '%'))) " +
            "AND bg.status != 2 " +
            "ORDER BY " +
            "CASE WHEN :columnName = 'id' THEN bg.id END ASC, " +
            "CASE WHEN :columnName = 'event_date' THEN bg.event_date END ASC, " +
            "CASE WHEN :columnName = 'event_category' THEN bg.event_category END ASC, " +
            "CASE WHEN :columnName = 'event_title' THEN bg.event_title END ASC",
            nativeQuery = true)
    List<EventGallery> findASC(@Param("searchText") String searchText, @Param("columnName") String columnName);

    @Query(value = "SELECT * FROM event_gallery bg WHERE " +
            "(LOWER(bg.id) LIKE LOWER(CONCAT('%', :searchText, '%')) " +
            "OR LOWER(bg.event_category) LIKE LOWER(CONCAT('%', :searchText, '%')) "+
            "OR LOWER(bg.event_date) LIKE LOWER(CONCAT('%', :searchText, '%')) " +
            "OR LOWER(bg.event_title) LIKE LOWER(CONCAT('%', :searchText, '%'))) " +
            "AND bg.status != 2 " +
            "ORDER BY " +
            "CASE :columnName " +
            "WHEN 'id' THEN bg.id " +
            "WHEN 'event_title' THEN bg.event_title " +
            "WHEN 'event_category' THEN bg.event_category " +
            "WHEN 'event_date' THEN bg.event_date " +
            "END DESC",
            nativeQuery = true)
    List<EventGallery> findDESC(@Param("searchText") String searchText,@Param("columnName") String columnName);

    @Query(value = "SELECT * FROM event_gallery bg WHERE " +
            "LOWER(bg.id) LIKE LOWER(CONCAT('%', :searchText, '%')) " +
            "OR LOWER(bg.event_title) LIKE LOWER(CONCAT('%', :searchText, '%')) " +
            "OR LOWER(bg.event_category) LIKE LOWER(CONCAT('%', :searchText, '%')) " +
            "OR LOWER(bg.event_date) LIKE LOWER(CONCAT('%', :searchText, '%')) " +
            "AND bg.status != 2",
            nativeQuery = true)
    Page<EventGallery> findAllByUserName(@Param("searchText") String searchText, Pageable pageable);

    @Query(value = "SELECT * FROM event_gallery bg WHERE " +
            "(LOWER(bg.id) LIKE LOWER(CONCAT('%', :searchText, '%')) " +
            "OR LOWER(bg.event_date) LIKE LOWER(CONCAT('%', :searchText, '%')) " +
            "OR LOWER(bg.event_category) LIKE LOWER(CONCAT('%', :searchText, '%')) " +
            "OR LOWER(bg.event_title) LIKE LOWER(CONCAT('%', :searchText, '%'))) " +
            "AND bg.status != 2 " +
            "ORDER BY " +
            "CASE WHEN :columnName = 'id' THEN bg.id END ASC, " +
            "CASE WHEN :columnName = 'event_date' THEN bg.event_date END ASC, " +
            "CASE WHEN :columnName = 'event_category' THEN bg.event_category END ASC, " +
            "CASE WHEN :columnName = 'event_title' THEN bg.event_title END ASC",
            nativeQuery = true)
    Page<EventGallery> findASC(@Param("searchText") String searchText, @Param("columnName") String columnName, Pageable pageable);

    @Query(value = "SELECT * FROM event_gallery t " +
            "WHERE t.status != 2 " +
            "ORDER BY " +
            "CASE WHEN :columnName = 'id' THEN t.id END DESC," +
            "CASE WHEN :columnName = 'event_title' THEN t.event_title END DESC," +
            "CASE WHEN :columnName = 'event_category' THEN t.event_category END DESC," +
            "CASE WHEN :columnName = 'event_date' THEN t.event_date END DESC ", nativeQuery = true)
    Page<EventGallery> searchAndOrderByDESC(@Param("columnName") String columnName, Pageable pageable);
    @Query(value = "SELECT * FROM event_gallery t " +
            "WHERE t.status != 2 " +
            "ORDER BY " +
            "CASE WHEN :columnName = 'id' THEN t.id END ASC," +
            "CASE WHEN :columnName = 'event_title' THEN t.event_title END ASC," +
            "CASE WHEN :columnName = 'event_category' THEN t.event_category END ASC," +
            "CASE WHEN :columnName = 'event_date' THEN t.event_date END ASC", nativeQuery = true)
    Page<EventGallery> searchAndOrderByASC(@Param("columnName") String columnName, Pageable pageable);
    @Query(value = "SELECT * FROM event_gallery bg WHERE " +
            "(LOWER(bg.id) LIKE LOWER(CONCAT('%', :searchText, '%')) " +
            "OR LOWER(bg.event_category) LIKE LOWER(CONCAT('%', :searchText, '%')) "+
            "OR LOWER(bg.event_date) LIKE LOWER(CONCAT('%', :searchText, '%')) " +
            "OR LOWER(bg.event_title) LIKE LOWER(CONCAT('%', :searchText, '%'))) " +
            "AND bg.status != 2 " +
            "ORDER BY " +
            "CASE :columnName " +
            "WHEN 'id' THEN bg.id " +
            "WHEN 'event_title' THEN bg.event_title " +
            "WHEN 'event_category' THEN bg.event_category " +
            "WHEN 'event_date' THEN bg.event_date " +
            "END DESC",
            nativeQuery = true)
    Page<EventGallery> findDESC(@Param("searchText") String searchText, @Param("columnName") String columnName, Pageable pageable);

    Page<EventGallery> findAllByStatusNot(Short status, Pageable pageable);

    List<EventGallery> findAllByStatusNot(Short status);

    List<EventGallery> findAllByStatus(Short status);

    Page<EventGallery> findAllByStatus(Short status, Pageable pageable);
}

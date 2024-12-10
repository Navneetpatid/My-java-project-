package com.janaushadhi.adminservice.repository;

import com.janaushadhi.adminservice.entity.ContactDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContactRepository extends JpaRepository<ContactDetail,Long> {


    @Query(value = "SELECT * FROM contact_detail bg WHERE " +
            "bg.status != 2 AND (" +
            "LOWER(bg.name) LIKE LOWER(CONCAT('%', :searchText, '%')) " +
            "OR LOWER(bg.email_id) LIKE LOWER(CONCAT('%', :searchText, '%')) " +
            "OR LOWER(bg.department) LIKE LOWER(CONCAT('%', :searchText, '%')) " +
            "OR LOWER(bg.contact_no) LIKE LOWER(CONCAT('%', :searchText, '%')) " +
            "OR LOWER(bg.intercom_no) LIKE LOWER(CONCAT('%', :searchText, '%')) " +
            "OR LOWER(bg.designation) LIKE LOWER(CONCAT('%', :searchText, '%')))",
            nativeQuery = true)
    List<ContactDetail> findAllBySearchText(@Param("searchText") String searchText);






    @Query(value = "SELECT * FROM contact_detail t " +
            "WHERE t.status != 2 " +
            "ORDER BY " +
            "CASE WHEN :columnName = 'id' THEN t.id END ASC," +
            "CASE WHEN :columnName = 'name' THEN t.name END ASC," +
            "CASE WHEN :columnName = 'contact_no' THEN t.contact_no END ASC," +
            "CASE WHEN :columnName = 'email_id' THEN t.email_id END ASC," +
            "CASE WHEN :columnName = 'department' THEN t.department END ASC," +
            "CASE WHEN :columnName = 'designation' THEN t.designation END ASC," +
            "CASE WHEN :columnName = 'intercom_no' THEN t.intercom_no END ASC", nativeQuery = true)
    List<ContactDetail> searchAndOrderByASC(@Param("columnName") String columnName);


    @Query(value = "SELECT * FROM contact_detail t " +
            "WHERE t.status != 2 " +
            "ORDER BY " +
            "CASE WHEN :columnName = 'id' THEN t.id END DESC," +
            "CASE WHEN :columnName = 'contact_no' THEN t.contact_no END DESC," +
            "CASE WHEN :columnName = 'department' THEN t.department END DESC," +
            "CASE WHEN :columnName = 'email_id' THEN t.email_id END DESC," +
            "CASE WHEN :columnName = 'name' THEN t.name END DESC," +
            "CASE WHEN :columnName = 'designation' THEN t.designation END DESC," +
            "CASE WHEN :columnName = 'intercom_no' THEN t.intercom_no END DESC ", nativeQuery = true)
    List<ContactDetail> searchAndOrderByDESC(@Param("columnName") String columnName);

    @Query(value = "SELECT * FROM contact_detail bg WHERE " +
            "(LOWER(bg.name) LIKE LOWER(CONCAT('%', :searchText, '%')) " +
            "OR LOWER(bg.contact_no) LIKE LOWER(CONCAT('%', :searchText, '%')) " +
            "OR LOWER(bg.email_id) LIKE LOWER(CONCAT('%', :searchText, '%')) " +
            "OR LOWER(bg.designation) LIKE LOWER(CONCAT('%', :searchText, '%')) " +
            "OR LOWER(bg.department) LIKE LOWER(CONCAT('%', :searchText, '%')) " +
            "OR LOWER(bg.intercom_no) LIKE LOWER(CONCAT('%', :searchText, '%'))) " +
            "AND bg.status != 2 " +
            "ORDER BY " +
            "CASE WHEN :columnName = 'id' THEN bg.id END ASC, " +
            "CASE WHEN :columnName = 'name' THEN bg.name END ASC, " +
            "CASE WHEN :columnName = 'contact_no' THEN bg.contact_no END ASC, " +
            "CASE WHEN :columnName = 'email_id' THEN bg.email_id END ASC, " +
            "CASE WHEN :columnName = 'designation' THEN bg.designation END ASC, " +
            "CASE WHEN :columnName = 'department' THEN bg.department END ASC, " +
            "CASE WHEN :columnName = 'intercom_no' THEN bg.intercom_no END ASC",
            nativeQuery = true)
    List<ContactDetail> findASC(@Param("searchText") String searchText, @Param("columnName") String columnName);

    @Query(value = "SELECT * FROM contact_detail bg WHERE " +
            "(LOWER(bg.name) LIKE LOWER(CONCAT('%', :searchText, '%')) " +
            "OR LOWER(bg.contact_no) LIKE LOWER(CONCAT('%', :searchText, '%')) " +
            "OR LOWER(bg.designation) LIKE LOWER(CONCAT('%', :searchText, '%')) " +
            "OR LOWER(bg.department) LIKE LOWER(CONCAT('%', :searchText, '%')) " +
            "OR LOWER(bg.email_id) LIKE LOWER(CONCAT('%', :searchText, '%')) " +
            "OR LOWER(bg.intercom_no) LIKE LOWER(CONCAT('%', :searchText, '%'))) " +
            "AND bg.status != 2 " +
            "ORDER BY " +
            "CASE :columnName " +
            "WHEN 'id' THEN bg.id " +
            "WHEN 'name' THEN bg.name " +
            "WHEN 'contact_no' THEN bg.contact_no " +
            "WHEN 'email_id' THEN bg.email_id " +
            "WHEN 'designation' THEN bg.designation " +
            "WHEN 'department' THEN bg.department " +
            "WHEN 'intercom_no' THEN bg.intercom_no " +
            "END DESC",
            nativeQuery = true)
    List<ContactDetail> findDESC(@Param("searchText") String searchText,@Param("columnName") String columnName);

    @Query(value = "SELECT * FROM contact_detail bg WHERE " +
            "LOWER(bg.name) LIKE LOWER(CONCAT('%', :searchText, '%')) " +
            "OR LOWER(bg.email_id) LIKE LOWER(CONCAT('%', :searchText, '%')) " +
            "OR LOWER(bg.department) LIKE LOWER(CONCAT('%', :searchText, '%')) " +
            "OR LOWER(bg.contact_no) LIKE LOWER(CONCAT('%', :searchText, '%')) " +
            "OR LOWER(bg.intercom_no) LIKE LOWER(CONCAT('%', :searchText, '%')) " +
            "OR LOWER(bg.designation) LIKE LOWER(CONCAT('%', :searchText, '%'))"+
            "AND bg.status != 2",
            nativeQuery = true)
    Page<ContactDetail> findAllByUserName(@Param("searchText") String searchText, Pageable pageable);

    @Query(value = "SELECT * FROM contact_detail bg WHERE " +
            "(LOWER(bg.name) LIKE LOWER(CONCAT('%', :searchText, '%')) " +
            "OR LOWER(bg.contact_no) LIKE LOWER(CONCAT('%', :searchText, '%')) " +
            "OR LOWER(bg.email_id) LIKE LOWER(CONCAT('%', :searchText, '%')) " +
            "OR LOWER(bg.designation) LIKE LOWER(CONCAT('%', :searchText, '%')) " +
            "OR LOWER(bg.department) LIKE LOWER(CONCAT('%', :searchText, '%')) " +
            "OR LOWER(bg.intercom_no) LIKE LOWER(CONCAT('%', :searchText, '%'))) " +
            "AND bg.status != 2 " +
            "ORDER BY " +
            "CASE WHEN :columnName = 'id' THEN bg.id END ASC, " +
            "CASE WHEN :columnName = 'name' THEN bg.name END ASC, " +
            "CASE WHEN :columnName = 'contact_no' THEN bg.contact_no END ASC, " +
            "CASE WHEN :columnName = 'email_id' THEN bg.email_id END ASC, " +
            "CASE WHEN :columnName = 'designation' THEN bg.designation END ASC, " +
            "CASE WHEN :columnName = 'department' THEN bg.department END ASC, " +
            "CASE WHEN :columnName = 'intercom_no' THEN bg.intercom_no END ASC",
            nativeQuery = true)
    Page<ContactDetail> findASC(@Param("searchText") String searchText, @Param("columnName") String columnName, Pageable pageable);

    @Query(value = "SELECT * FROM contact_detail t " +
            "WHERE t.status != 2 " +
            "ORDER BY " +
            "CASE WHEN :columnName = 'id' THEN t.id END DESC," +
            "CASE WHEN :columnName = 'contact_no' THEN t.contact_no END DESC," +
            "CASE WHEN :columnName = 'designation' THEN t.designation END DESC," +
            "CASE WHEN :columnName = 'email_id' THEN t.email_id END DESC," +
            "CASE WHEN :columnName = 'name' THEN t.name END DESC," +
            "CASE WHEN :columnName = 'department' THEN t.department END DESC," +
            "CASE WHEN :columnName = 'intercom_no' THEN t.intercom_no END DESC ", nativeQuery = true)
    Page<ContactDetail> searchAndOrderByDESC(@Param("columnName") String columnName, Pageable pageable);
    @Query(value = "SELECT * FROM contact_detail t " +
            "WHERE t.status != 2 " +
            "ORDER BY " +
            "CASE WHEN :columnName = 'id' THEN t.id END ASC," +
            "CASE WHEN :columnName = 'name' THEN t.name END ASC," +
            "CASE WHEN :columnName = 'contact_no' THEN t.contact_no END ASC," +
            "CASE WHEN :columnName = 'email_id' THEN t.email_id END ASC," +
            "CASE WHEN :columnName = 'designation' THEN t.designation END ASC," +
            "CASE WHEN :columnName = 'department' THEN t.department END ASC," +
            "CASE WHEN :columnName = 'intercom_no' THEN t.intercom_no END ASC", nativeQuery = true)
    Page<ContactDetail> searchAndOrderByASC(@Param("columnName") String columnName, Pageable pageable);
    @Query(value = "SELECT * FROM contact_detail bg WHERE " +
            "(LOWER(bg.name) LIKE LOWER(CONCAT('%', :searchText, '%')) " +
            "OR LOWER(bg.contact_no) LIKE LOWER(CONCAT('%', :searchText, '%')) " +
            "OR LOWER(bg.designation) LIKE LOWER(CONCAT('%', :searchText, '%')) " +
            "OR LOWER(bg.department) LIKE LOWER(CONCAT('%', :searchText, '%')) " +
            "OR LOWER(bg.email_id) LIKE LOWER(CONCAT('%', :searchText, '%')) " +
            "OR LOWER(bg.intercom_no) LIKE LOWER(CONCAT('%', :searchText, '%'))) " +
            "AND bg.status != 2 " +
            "ORDER BY " +
            "CASE :columnName " +
            "WHEN 'id' THEN bg.id " +
            "WHEN 'name' THEN bg.name " +
            "WHEN 'contact_no' THEN bg.contact_no " +
            "WHEN 'email_id' THEN bg.email_id " +
            "WHEN 'designation' THEN bg.designation " +
            "WHEN 'department' THEN bg.department " +
            "WHEN 'intercom_no' THEN bg.intercom_no " +
            "END DESC",
            nativeQuery = true)
    Page<ContactDetail> findDESC(@Param("searchText") String searchText, @Param("columnName") String columnName, Pageable pageable);


    List<ContactDetail> findAllByDepartmentAndStatusNot(String searchText,Short status);



//    List<ContactDetail> findAllByStatusNot(Short status);
//    Page<ContactDetail> findAllByStatusNot(Short status,Pageable pageable);

    List<ContactDetail> findAllByStatusNot(Short status);

    Page<ContactDetail> findAllByStatusNot( Short status,Pageable pageable);

    Page<ContactDetail> findAllByDepartmentAndStatusNot(String searchText, Pageable pageable, Short status);

    Optional<ContactDetail> findByIdAndStatusNot(Long id, Short status);

    List<ContactDetail> findAllByStatus(Short status);

    Page<ContactDetail> findAllByStatus(Short status, Pageable pageable);
}

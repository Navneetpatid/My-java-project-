package com.janaushadhi.adminservice.repository;


import com.janaushadhi.adminservice.entity.ManagementTeam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ManagementRepository extends JpaRepository<ManagementTeam,Long> {

    @Query(value = "SELECT * FROM management_team bg WHERE " +
            "(LOWER(bg.contact_no) LIKE LOWER(concat('%',:searchText,'%'))) " +
            "OR (LOWER(bg.designation) LIKE LOWER(concat('%',:searchText,'%'))) "+
            "OR (LOWER(bg.name) LIKE LOWER(concat('%',:searchText,'%'))) "+
            "OR (LOWER(bg.mail) LIKE LOWER(concat('%',:searchText,'%'))) "+
            "OR (LOWER(bg.fax) LIKE LOWER(concat('%',:searchText,'%'))) "+
            "OR (LOWER(bg.edu_qualification) LIKE LOWER(concat('%',:searchText,'%')))"+
            "AND bg.status != 2", nativeQuery = true)
    List<ManagementTeam> findAllByUserNameContainingIgnoreCaseOrEmailContainingIgnoreCase(@Param("searchText") String searchText);

    @Query(value = "SELECT * FROM management_team t " +
            "WHERE t.status != 2 " +
            "ORDER BY " +
            "CASE WHEN :columnName = 'id' THEN t.id END ASC," +
            "CASE WHEN :columnName = 'contact_no' THEN t.contact_no END ASC," +
            "CASE WHEN :columnName = 'name' THEN t.name END ASC," +
            "CASE WHEN :columnName = 'mail' THEN t.mail END ASC," +
            "CASE WHEN :columnName = 'fax' THEN t.fax END ASC," +
            "CASE WHEN :columnName = 'designation' THEN t.designation END ASC," +
            "CASE WHEN :columnName = 'edu_qualification' THEN t.edu_qualification END ASC", nativeQuery = true)
    List<ManagementTeam> searchAndOrderByASC(@Param("columnName") String columnName);


    @Query(value = "SELECT * FROM management_team t " +
            "WHERE t.status != 2 " +
            "ORDER BY " +
            "CASE WHEN :columnName = 'id' THEN t.id END DESC," +
            "CASE WHEN :columnName = 'contact_no' THEN t.contact_no END DESC," +
            "CASE WHEN :columnName = 'name' THEN t.name END DESC," +
            "CASE WHEN :columnName = 'mail' THEN t.mail END DESC," +
            "CASE WHEN :columnName = 'fax' THEN t.fax END DESC," +
            "CASE WHEN :columnName = 'designation' THEN t.designation END DESC," +
            "CASE WHEN :columnName = 'edu_qualification' THEN t.edu_qualification END DESC ", nativeQuery = true)
    List<ManagementTeam> searchAndOrderByDESC(@Param("columnName") String columnName);

    @Query(value = "SELECT * FROM management_team bg WHERE " +
            " (LOWER(bg.contact_no) LIKE LOWER(concat('%',:searchText,'%'))) " +
            " OR (LOWER(bg.name) LIKE LOWER(concat('%',:searchText,'%'))) " +
            " OR (LOWER(bg.mail) LIKE LOWER(concat('%',:searchText,'%'))) " +
            " OR (LOWER(bg.fax) LIKE LOWER(concat('%',:searchText,'%'))) " +
            " OR (LOWER(bg.edu_qualification) LIKE LOWER(concat('%',:searchText,'%'))) " +
            " OR (LOWER(bg.designation) LIKE LOWER(concat('%',:searchText,'%'))) " +
            " AND bg.status != 2 " +
            " ORDER BY " +
            " CASE WHEN :columnName = 'id' THEN bg.id END ASC," +
            " CASE WHEN :columnName = 'contact_no' THEN bg.contact_no END ASC," +
            " CASE WHEN :columnName = 'name' THEN bg.name END ASC," +
            " CASE WHEN :columnName = 'mail' THEN bg.mail END ASC," +
            " CASE WHEN :columnName = 'fax' THEN bg.fax END ASC," +
            " CASE WHEN :columnName = 'designation' THEN bg.designation END ASC," +
            " CASE WHEN :columnName = 'edu_qualification' THEN bg.edu_qualification END ASC", nativeQuery = true)
    List<ManagementTeam> findASC(@Param("searchText") String searchText, @Param("columnName") String columnName);

    @Query(value = "SELECT * FROM management_team bg WHERE " +
            " (LOWER(bg.contact_no) LIKE LOWER(concat('%',:searchText,'%'))) " +
            " OR (LOWER(bg.name) LIKE LOWER(concat('%',:searchText,'%'))) " +
            " OR (LOWER(bg.mail) LIKE LOWER(concat('%',:searchText,'%'))) " +
            " OR (LOWER(bg.fax) LIKE LOWER(concat('%',:searchText,'%'))) " +
            " OR (LOWER(bg.designation) LIKE LOWER(concat('%',:searchText,'%'))) " +
            " OR (LOWER(bg.edu_qualification) LIKE LOWER(concat('%',:searchText,'%'))) " +
            " AND bg.status != 2 " +
            " ORDER BY " +
            " CASE WHEN :columnName = 'id' THEN bg.id END DESC," +
            " CASE WHEN :columnName = 'contact_no' THEN bg.contact_no END DESC," +
            " CASE WHEN :columnName = 'name' THEN bg.name END DESC," +
            " CASE WHEN :columnName = 'mail' THEN bg.mail END DESC," +
            " CASE WHEN :columnName = 'fax' THEN bg.fax END DESC," +
            " CASE WHEN :columnName = 'designation' THEN bg.designation END DESC," +
            " CASE WHEN :columnName = 'edu_qualification' THEN bg.edu_qualification END DESC", nativeQuery = true)
    List<ManagementTeam> findDESC(@Param("searchText") String searchText,@Param("columnName") String columnName);

    @Query(value = "SELECT * FROM management_team bg WHERE " +
            "LOWER(bg.contact_no) LIKE LOWER(concat('%', :searchText, '%')) " +
            "OR LOWER(bg.name) LIKE LOWER(concat('%', :searchText, '%')) " +
            "OR LOWER(bg.mail) LIKE LOWER(concat('%', :searchText, '%')) " +
            "OR LOWER(bg.fax) LIKE LOWER(concat('%', :searchText, '%')) " +
            "OR LOWER(bg.edu_qualification) LIKE LOWER(concat('%', :searchText, '%')) " +
            "OR LOWER(bg.designation) LIKE LOWER(concat('%', :searchText, '%'))"+
            "AND bg.status != 2", nativeQuery = true)
    Page<ManagementTeam> findAllByUserName(@Param("searchText") String searchText, Pageable pageable);

    @Query(value = "SELECT * FROM management_team bg WHERE " +
            "(LOWER(bg.contact_no) LIKE LOWER(concat('%',:searchText,'%'))) " +
            "OR (LOWER(bg.name) LIKE LOWER(concat('%',:searchText,'%'))) " +
            "OR (LOWER(bg.mail) LIKE LOWER(concat('%',:searchText,'%'))) " +
            "OR (LOWER(bg.fax) LIKE LOWER(concat('%',:searchText,'%'))) " +
            "OR (LOWER(bg.edu_qualification) LIKE LOWER(concat('%',:searchText,'%'))) " +
            "OR (LOWER(bg.designation) LIKE LOWER(concat('%',:searchText,'%'))) " +
            " AND bg.status != 2 " +
            "ORDER BY " +
            "CASE WHEN :columnName = 'id' THEN bg.id END ASC," +
            "CASE WHEN :columnName = 'contact_no' THEN bg.contact_no END ASC," +
            "CASE WHEN :columnName = 'name' THEN bg.name END ASC," +
            "CASE WHEN :columnName = 'mail' THEN bg.mail END ASC," +
            "CASE WHEN :columnName = 'fax' THEN bg.fax END ASC," +
            "CASE WHEN :columnName = 'designation' THEN bg.designation END ASC," +
            "CASE WHEN :columnName = 'edu_qualification' THEN bg.edu_qualification END ASC", nativeQuery = true)
    Page<ManagementTeam> findASC( @Param("searchText") String searchText,@Param("columnName") String columnName, Pageable pageable);
    @Query(value = "SELECT * FROM management_team t " +
            "WHERE t.status != 2 " +
            "ORDER BY " +
            "CASE WHEN :columnName = 'id' THEN t.id END DESC," +
            "CASE WHEN :columnName = 'contact_no' THEN t.contact_no END DESC," +
            "CASE WHEN :columnName = 'name' THEN t.name END DESC," +
            "CASE WHEN :columnName = 'mail' THEN t.mail END DESC," +
            "CASE WHEN :columnName = 'fax' THEN t.fax END DESC," +
            "CASE WHEN :columnName = 'edu_qualification' THEN t.edu_qualification END DESC," +
            "CASE WHEN :columnName = 'designation' THEN t.designation END DESC ", nativeQuery = true)
    Page<ManagementTeam> searchAndOrderByDESC(@Param("columnName") String columnName, Pageable pageable);
//    @Query(value = "SELECT * FROM management_team t " +
//            "ORDER BY " +
//            "CASE WHEN :columnName = 'id' THEN t.id END ASC," +
//            "CASE WHEN :columnName = 'contact_no' THEN t.contact_no END ASC," +
//            "CASE WHEN :columnName = 'name' THEN t.name END ASC," +
//            "CASE WHEN :columnName = 'mail' THEN t.mail END ASC," +
//            "CASE WHEN :columnName = 'fax' THEN t.fax END ASC," +
//            "CASE WHEN :columnName = 'designation' THEN t.designation END ASC," +
//            "CASE WHEN :columnName = 'edu_qualification' THEN t.edu_qualification END ASC", nativeQuery = true)
//    Page<ManagementTeam> searchAndOrderByASC(@Param("columnName") String columnName, Pageable pageable);

    @Query(value = "SELECT * FROM management_team t " +
            "WHERE t.status != 2 " +
            "ORDER BY " +
            "CASE WHEN :columnName = 'id' THEN t.id END ASC, " +
            "CASE WHEN :columnName = 'contact_no' THEN t.contact_no END ASC, " +
            "CASE WHEN :columnName = 'name' THEN t.name END ASC, " +
            "CASE WHEN :columnName = 'mail' THEN t.mail END ASC, " +
            "CASE WHEN :columnName = 'fax' THEN t.fax END ASC, " +
            "CASE WHEN :columnName = 'designation' THEN t.designation END ASC, " +
            "CASE WHEN :columnName = 'edu_qualification' THEN t.edu_qualification END ASC",
            nativeQuery = true)
    Page<ManagementTeam> searchAndOrderByASC(@Param("columnName") String columnName, Pageable pageable);

    @Query(value = "SELECT * FROM management_team bg WHERE " +
            "(LOWER(bg.contact_no) LIKE LOWER(concat('%',:searchText,'%'))) " +
            "OR (LOWER(bg.name) LIKE LOWER(concat('%',:searchText,'%'))) " +
            "OR (LOWER(bg.mail) LIKE LOWER(concat('%',:searchText,'%'))) " +
            "OR (LOWER(bg.fax) LIKE LOWER(concat('%',:searchText,'%'))) " +
            "OR (LOWER(bg.designation) LIKE LOWER(concat('%',:searchText,'%'))) " +
            "OR (LOWER(bg.edu_qualification) LIKE LOWER(concat('%',:searchText,'%'))) " +
            " AND bg.status != 2 " +
            "ORDER BY " +
            "CASE WHEN :columnName = 'id' THEN bg.id END DESC," +
            "CASE WHEN :columnName = 'contact_no' THEN bg.contact_no END DESC," +
            "CASE WHEN :columnName = 'name' THEN bg.name END DESC," +
            "CASE WHEN :columnName = 'mail' THEN bg.mail END DESC," +
            "CASE WHEN :columnName = 'fax' THEN bg.fax END DESC," +
            "CASE WHEN :columnName = 'designation' THEN bg.designation END DESC," +
            "CASE WHEN :columnName = 'edu_qualification' THEN bg.edu_qualification END DESC", nativeQuery = true)
    Page<ManagementTeam> findDESC(@Param("searchText") String searchText, @Param("columnName") String columnName, Pageable pageable);


    @Query(value = "SELECT * FROM management_team bg WHERE bg.status != 2", nativeQuery = true)
    Page<ManagementTeam> findAllWhereStatusNotTwo( Pageable pageable);


    @Query(value = "SELECT * FROM management_team bg WHERE bg.status != 2", nativeQuery = true)
    List<ManagementTeam> findAllWhereStatusNotTwo();
}

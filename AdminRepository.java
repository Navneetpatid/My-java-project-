package com.janaushadhi.adminservice.repository;

import com.janaushadhi.adminservice.entity.Admin;
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
public interface AdminRepository extends JpaRepository<Admin, Long> {
//    @Query(nativeQuery = true, value = "select * from admin order by user_name  ")
//    List<Admin> findAllOrderByUserNameASC(String columnName, String orderBy);

    Page<Admin> findAllByStatus(Pageable pageable, Short st);
    Page<Admin> findAllByUserNameContainingIgnoreCase(String searchText, Pageable pageable);

    Optional<Admin> findById(String id);

    List<Admin> findByUserName(String searchText, Sort sort);

//    Page<Admin> findAllByStatusAndUserNameContainingIgnoreCase( String searchText, Pageable pageable);


	Optional<Admin> findByEmail(String email);

    //List<Admin> findByUserNameOrEmail(String searchText, Sort sort);

//    @Query(value = "SELECT * FROM admin bg WHERE (bg.user_name like concat('%',?1,'%')" +
//            " or bg.email like concat('%',?1,'%'))", nativeQuery = true)

    @Query(value = "SELECT * FROM admin bg WHERE (" +
            "(LOWER(bg.user_name) LIKE LOWER(concat('%', :searchText, '%'))) " +
            "OR (LOWER(bg.email) LIKE LOWER(concat('%', :searchText, '%'))) " +
            "OR (LOWER(bg.id) LIKE LOWER(concat('%', :searchText, '%'))) " +
            "OR (LOWER(bg.role_name) LIKE LOWER(concat('%', :searchText, '%')))" +
            ") " +
            "AND bg.roleid != 6 " +
            "AND bg.id != :id",
            nativeQuery = true)
    List<Admin> findAllByUserNameContainingIgnoreCaseOrEmailContainingIgnoreCase(@Param("searchText") String searchText,@Param("id") Long id);

    @Query(value = "SELECT * FROM admin bg WHERE (" +
            "(LOWER(bg.user_name) LIKE LOWER(concat('%', :searchText, '%'))) " +
            "OR (LOWER(bg.email) LIKE LOWER(concat('%', :searchText, '%'))) " +
            "OR (LOWER(bg.id) LIKE LOWER(concat('%', :searchText, '%'))) " +
            "OR (LOWER(bg.role_name) LIKE LOWER(concat('%', :searchText, '%')))" +
            ") " +
            "AND bg.roleid != 6 " +
            "AND bg.id != :id",
            nativeQuery = true)
    Page<Admin> findAllByUserName(@Param("searchText") String searchText, @Param("id") long id, Pageable pageable);




    @Query(value = "SELECT * FROM admin bg WHERE " +
            "bg.roleid != 6 " +
            "AND bg.id != :id " +
            "AND (" +
            "LOWER(bg.user_name) LIKE LOWER(CONCAT('%', :searchText, '%')) " +
            "OR LOWER(bg.email) LIKE LOWER(CONCAT('%', :searchText, '%')) " +
            "OR LOWER(bg.id) LIKE LOWER(CONCAT('%', :searchText, '%')) " +
            "OR LOWER(bg.role_name) LIKE LOWER(CONCAT('%', :searchText, '%'))" +
            ") " +
            "ORDER BY " +
            "CASE :columnName " +
            "  WHEN 'id' THEN bg.id " +
            "  WHEN 'user_name' THEN bg.user_name " +
            "  WHEN 'email' THEN bg.email " +
            "  WHEN 'role_name' THEN bg.role_name " +
            "END ASC", nativeQuery = true)
    List<Admin> findASC(@Param("searchText") String searchText, @Param("columnName") String columnName,@Param("id") Long id);

    @Query(value = "SELECT * FROM admin bg WHERE " +
            "bg.roleid != 6 " +
            "AND bg.id != :id " +
            "AND (" +
            "LOWER(bg.user_name) LIKE LOWER(CONCAT('%', :searchText, '%')) " +
            "OR LOWER(bg.email) LIKE LOWER(CONCAT('%', :searchText, '%')) " +
            "OR LOWER(bg.id) LIKE LOWER(CONCAT('%', :searchText, '%')) " +
            "OR LOWER(bg.role_name) LIKE LOWER(CONCAT('%', :searchText, '%'))" +
            ") " +
            "ORDER BY " +
            "CASE :columnName " +
            "  WHEN 'id' THEN bg.id " +
            "  WHEN 'user_name' THEN bg.user_name " +
            "  WHEN 'email' THEN bg.email " +
            "  WHEN 'role_name' THEN bg.role_name " +
            "END DESC", nativeQuery = true)
    List<Admin> findDESC(@Param("searchText") String searchText, @Param("columnName") String columnName,@Param("id") Long id);


//    @Query(value = "SELECT * FROM admin bg WHERE " +
//            "((LOWER(bg.user_name) LIKE LOWER(CONCAT('%', ?1, '%'))) " +
//            "OR (LOWER(bg.email) LIKE LOWER(CONCAT('%', ?1, '%')))) " +
//            "AND bg.status = ?2 "  , nativeQuery = true)
//    List<Admin> findByUserNameOrEmailAndStatus(@Param("searchText") String searchText, @Param("status") Short status);

//    @Query(value = "SELECT * FROM admin ORDER BY :columnName asc  ", nativeQuery = true)
//    List<Admin> findAllBYUserName(String columnName, String  orderBy);


    @Query(value = "SELECT * FROM admin bg " +
            "WHERE bg.roleid != 6 " +
            "AND bg.id != :id " +
            "ORDER BY " +
            "CASE :columnName " +
            "  WHEN 'id' THEN bg.id " +
            "  WHEN 'user_name' THEN bg.user_name " +
            "  WHEN 'email' THEN bg.email " +
            "  WHEN 'role_name' THEN bg.role_name " +
            "END ASC", nativeQuery = true)
    List<Admin> searchAndOrderByASC(@Param("columnName") String columnName, @Param("id")Long id);


    @Query(value = "SELECT * FROM admin bg " +
            "WHERE bg.roleid != 6 " +
            "AND bg.id != :id " +
            "ORDER BY " +
            "CASE :columnName " +
            "  WHEN 'id' THEN bg.id " +
            "  WHEN 'user_name' THEN bg.user_name " +
            "  WHEN 'email' THEN bg.email " +
            "  WHEN 'role_name' THEN bg.role_name " +
            "END DESC", nativeQuery = true)
    List<Admin> searchAndOrderByDESC(@Param("columnName") String columnName,@Param("id")Long id);



    @Query(value = "SELECT * FROM admin bg " +
            "WHERE bg.roleid != 6 " +
            "AND bg.id != :id " +
            "ORDER BY " +
            "CASE :columnName " +
            "  WHEN 'id' THEN bg.id " +
            "  WHEN 'user_name' THEN bg.user_name " +
            "  WHEN 'email' THEN bg.email " +
            "  WHEN 'role_name' THEN bg.role_name " +
            "END ASC", nativeQuery = true)
    Page<Admin> searchAndOrderByASC(@Param("columnName") String columnName, @Param("id") Long id, Pageable pageable);



    @Query(value = "SELECT * FROM admin bg WHERE " +
            "bg.roleid != 6 " +
            "AND bg.id != :id " +
            "AND (" +
            "LOWER(bg.user_name) LIKE LOWER(CONCAT('%', :searchText, '%')) " +
            "OR LOWER(bg.email) LIKE LOWER(CONCAT('%', :searchText, '%')) " +
            "OR LOWER(bg.id) LIKE LOWER(CONCAT('%', :searchText, '%')) " +
            "OR LOWER(bg.role_name) LIKE LOWER(CONCAT('%', :searchText, '%'))" +
            ") " +
            "ORDER BY " +
            "CASE :columnName " +
            "  WHEN 'id' THEN bg.id " +
            "  WHEN 'user_name' THEN bg.user_name " +
            "  WHEN 'email' THEN bg.email " +
            "  WHEN 'role_name' THEN bg.role_name " +
            "END ASC", nativeQuery = true)
    Page<Admin> findASC(@Param("searchText") String searchText, @Param("columnName") String columnName, @Param("id") Long id, Pageable pageable);




    @Query(value = "SELECT * FROM admin bg WHERE " +
            "bg.roleid != 6 " +
            "AND bg.id != :id " +
            "AND (" +
            "LOWER(bg.user_name) LIKE LOWER(CONCAT('%', :searchText, '%')) " +
            "OR LOWER(bg.email) LIKE LOWER(CONCAT('%', :searchText, '%')) " +
            "OR LOWER(bg.id) LIKE LOWER(CONCAT('%', :searchText, '%')) " +
            "OR LOWER(bg.role_name) LIKE LOWER(CONCAT('%', :searchText, '%'))" +
            ") " +
            "ORDER BY " +
            "CASE :columnName " +
            "  WHEN 'id' THEN bg.id " +
            "  WHEN 'user_name' THEN bg.user_name " +
            "  WHEN 'email' THEN bg.email " +
            "  WHEN 'role_name' THEN bg.role_name " +
            "END DESC", nativeQuery = true)
    Page<Admin> findDESC(@Param("searchText") String searchText, @Param("columnName") String columnName,  @Param("id") Long id,Pageable pageable );










    @Query(value = "SELECT * FROM admin bg " +
            "WHERE bg.roleid != 6 " +
            "AND bg.id != :id " +
            "ORDER BY " +
            "CASE :columnName " +
            "  WHEN 'id' THEN bg.id " +
            "  WHEN 'user_name' THEN bg.user_name " +
            "  WHEN 'email' THEN bg.email " +
            "  WHEN 'role_name' THEN bg.role_name " +
            "END DESC", nativeQuery = true)
    Page<Admin> searchAndOrderByDESC(@Param("columnName") String columnName, @Param("id") Long id, Pageable pageable);





    List<Admin> findAllByIdNot(Long adminId);



    Page<Admin> findAllByIdNotAndRoleidNot(Long adminId, int roleid, Pageable pageable);

    //---
//    @Query(value = "SELECT bg.* FROM admin bg inner join roles rl on bg.roleid=rl.id " +
//            "WHERE (LOWER(bg.user_name) like LOWER(concat('%',?1,'%'))) " +
//            "or (LOWER(bg.email) like LOWER(concat('%',?1,'%')))" +
//            "or (LOWER(rl.role) like LOWER(concat('%',?1,'%')))" +
//            "AND bg.status =?2 ", nativeQuery = true)
//    List<Admin> findByUserNameOrEmailAndStatus1(  @Param("searchText") String searchText,@Param("status") Short status);

}

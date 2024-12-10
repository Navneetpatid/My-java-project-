package com.janaushadhi.adminservice.repository;


import com.janaushadhi.adminservice.entity.UploadImage;
import feign.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UploadImageRepository extends JpaRepository<UploadImage,Long> {

    @Query(value = "SELECT * FROM upload_image where Status !=2 ORDER BY id DESC", nativeQuery = true)
    List<UploadImage> findAllNewProductroductAndStatusNot();

    @Query(value = "SELECT * FROM upload_image ui WHERE Status !=2 "+ "ORDER BY " +
            "CASE WHEN :columnName = 'id' THEN ui.id END ASC," +
            "CASE WHEN :columnName = 'name' THEN ui.name END ASC," +
            "CASE WHEN :columnName = 'designation' THEN ui.designation END ASC," +
            "CASE WHEN :columnName = 'description' THEN ui.description END ASC,", nativeQuery = true)
    List<UploadImage>  getAndOrderByASC(@Param("columnName") String columnName);

    @Query(value = "SELECT * FROM upload_image ui WHERE Status !=2 "+"ORDER BY " +
            "CASE WHEN :columnName = 'id' THEN ui.id END DESC," +
            "CASE WHEN :columnName = 'name' THEN ui.name END DESC," +
            "CASE WHEN :columnName = 'designation' THEN ui.designation END DESC," +
            "CASE WHEN :columnName = 'description' THEN ui.description END DESC,", nativeQuery = true)
    List<UploadImage> getAndOrderByDESC(@Param("columnName")  String columnName);

    //----------------------------------------------------------------------

    @Query(value = "SELECT * FROM upload_image where Status !=2 ORDER BY id DESC", nativeQuery = true)
    Page<UploadImage> findAllAndStatus(Pageable pageable);

//    @Query(value = "SELECT * FROM upload_image ui WHERE Status !=2 "+"ORDER BY " +
//            "CASE WHEN :columnName = 'id' THEN ui.id END ASC," +
//            "CASE WHEN :columnName = 'name' THEN ui.name END ASC," +
//            "CASE WHEN :columnName = 'designation' THEN ui.designation END ASC," +
//            "CASE WHEN :columnName = 'description' THEN ui.description END ASC,", nativeQuery = true)
    @Query(value = "SELECT * FROM upload_image ui WHERE Status != 2 " +
            "ORDER BY " +
            "CASE WHEN :columnName = 'id' THEN ui.id END ASC, " +
            "CASE WHEN :columnName = 'name' THEN ui.name END ASC, " +
            "CASE WHEN :columnName = 'designation' THEN ui.designation END ASC, " +
            "CASE WHEN :columnName = 'description' THEN ui.description END ASC, " +
            "ui.id ASC", // Add a fallback order by clause to avoid syntax error
    countQuery = "SELECT COUNT(*) FROM upload_image ui WHERE Status != 2",
    nativeQuery = true)
    Page<UploadImage> getAndOrderByPageASC(@Param("columnName")  String columnName, Pageable pageable);

//    @Query(value = "SELECT * FROM upload_image ui WHERE Status !=2 "+"ORDER BY " +
//            "CASE WHEN :columnName = 'id' THEN ui.id END DESC," +
//            "CASE WHEN :columnName = 'name' THEN ui.name END DESC," +
//            "CASE WHEN :columnName = 'designation' THEN ui.designation END DESC," +
//            "CASE WHEN :columnName = 'description' THEN ui.description END DESC,", nativeQuery = true)
    @Query(value = "SELECT * FROM upload_image ui WHERE Status != 2 " +
            "ORDER BY " +
            "CASE WHEN :columnName = 'id' THEN ui.id END DESC, " +
            "CASE WHEN :columnName = 'name' THEN ui.name END DESC, " +
            "CASE WHEN :columnName = 'designation' THEN ui.designation END DESC, " +
            "CASE WHEN :columnName = 'description' THEN ui.description END DESC, " +
            "ui.id ASC", // Add a fallback order by clause to avoid syntax error
    countQuery = "SELECT COUNT(*) FROM upload_image ui WHERE Status != 2",
    nativeQuery = true)
    Page<UploadImage> getAndOrderByPageDESC(Pageable pageable,@Param("columnName")  String columnName);
    //---------------------------------Delete-------------------------------------
    Optional<UploadImage> findByIdAndStatusNot(Long id, short i);
    
    @Query(value = "SELECT * FROM upload_image ui WHERE Status !=2 And (ui.name like concat('%',:searchText,'%') or ui.category like concat('%',:searchText,'%') or ui.designation like concat('%',:searchText,'%') or ui.description like concat('%',:searchText,'%'))"+ 
    		" ORDER BY " +
            "CASE WHEN :columnName = 'id' THEN ui.id END ASC," +
            "CASE WHEN :columnName = 'name' THEN ui.name END ASC," +
            "CASE WHEN :columnName = 'designation' THEN ui.designation END ASC," +
            "CASE WHEN :columnName = 'description' THEN ui.description END ASC,", nativeQuery = true)
   // ORDER BY ui.id=:columnName ASC ,ui.name=:columnName ASC,ui.designation=:columnName ASC,ui.description=:columnName ASC", nativeQuery = true)
	List<UploadImage> getSearchTextAndOrderByASC(@Param("searchText")String searchText,@Param("columnName") String columnName);

    @Query(value = "SELECT * FROM upload_image ui WHERE Status !=2 And (ui.name like concat('%',:searchText,'%') or ui.category like concat('%',:searchText,'%') or ui.designation like concat('%',:searchText,'%') or ui.description like concat('%',:searchText,'%')) "+" ORDER BY " +
            "CASE WHEN :columnName = 'id' THEN ui.id END DESC," +
            "CASE WHEN :columnName = 'name' THEN ui.name END DESC," +
            "CASE WHEN :columnName = 'designation' THEN ui.designation END DESC," +
            "CASE WHEN :columnName = 'description' THEN ui.description END DESC,", nativeQuery = true)
	List<UploadImage> getSearchTextAndOrderByDESC(@Param("searchText")String searchText,@Param("columnName") String columnName);
    
    @Query(value = "SELECT * FROM upload_image ui WHERE Status !=2 And (ui.name like concat('%',:searchText,'%') or ui.category like concat('%',:searchText,'%') or ui.designation like concat('%',:searchText,'%') or ui.description like concat('%',:searchText,'%'))", nativeQuery = true)
	List<UploadImage> getSearchText(@Param("searchText")String searchText);
    
    @Query(value = "SELECT * FROM upload_image ui WHERE Status !=2 And (ui.name like concat('%',:searchText,'%') or ui.category like concat('%',:searchText,'%') or ui.designation like concat('%',:searchText,'%') or ui.description like concat('%',:searchText,'%')) "
    		+ " ORDER BY " +
            "CASE WHEN :columnName = 'id' THEN ui.id END ASC," +
            "CASE WHEN :columnName = 'name' THEN ui.name END ASC," +
            "CASE WHEN :columnName = 'designation' THEN ui.designation END ASC," +
            "CASE WHEN :columnName = 'description' THEN ui.description END ASC,", nativeQuery = true)
	Page<UploadImage> getSearchTextAndOrderByPageASC(@Param("searchText")String searchText,@Param("columnName") String columnName, Pageable pageable);

//    @Query(value = "SELECT * FROM upload_image ui WHERE Status !=2 And (ui.name like concat('%',:searchText,'%') or ui.category like concat('%',:searchText,'%') or ui.description like concat('%',:searchText,'%') or ui.description like concat('%',:searchText,'%'))"
//    		+ "ORDER BY " +
//            "CASE WHEN :columnName = 'id' THEN ui.id END DESC," +
//            "CASE WHEN :columnName = 'name' THEN ui.name END DESC," +
//            "CASE WHEN :columnName = 'designation' THEN ui.designation END DESC," +
//            "CASE WHEN :columnName = 'description' THEN ui.description END DESC,", nativeQuery = true)
    @Query(value = "SELECT * FROM upload_image ui WHERE Status != 2 AND (ui.name LIKE CONCAT('%', :searchText, '%') OR ui.category LIKE CONCAT('%', :searchText, '%') OR ui.description LIKE CONCAT('%', :searchText, '%')) " +
            "ORDER BY " +
            "CASE WHEN :columnName = 'id' THEN ui.id END DESC, " +
            "CASE WHEN :columnName = 'name' THEN ui.name END DESC, " +
            "CASE WHEN :columnName = 'designation' THEN ui.designation END DESC, " +
            "CASE WHEN :columnName = 'description' THEN ui.description END DESC, " +
            "CASE WHEN :columnName != 'id' AND :columnName != 'name' AND :columnName != 'designation' AND :columnName != 'description' THEN NULL END",
    nativeQuery = true)
	Page<UploadImage> getSearchTextAndOrderByPageDESC(@Param("searchText")String searchText, Pageable pageable,@Param("columnName") String columnName);

	@Query(value = "SELECT * FROM upload_image ui WHERE Status !=2 And (ui.name like concat('%',:searchText,'%') or ui.category like concat('%',:searchText,'%') or ui.designation like concat('%',:searchText,'%') or ui.description like concat('%',:searchText,'%'))", nativeQuery = true)
	Page<UploadImage> getSearchTextPage(@Param("searchText")String searchText, Pageable pageable);

    List<UploadImage> findAllByStatus(Short status);

    Page<UploadImage> findAllByStatus(Short status, Pageable pageable);
}

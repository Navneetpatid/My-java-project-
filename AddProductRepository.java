package com.janaushadhi.adminservice.repository;

import com.janaushadhi.adminservice.entity.Newproduct;
import feign.Param;
import jakarta.xml.bind.SchemaOutputResolver;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AddProductRepository extends JpaRepository<Newproduct, Long> {


    Newproduct findByproductId(Long Id);

    Optional<Newproduct> findByProductIdAndStatusNot(Long id, short i);

    @Query(value = "SELECT * FROM newproduct WHERE Status !=2 ORDER BY product_id DESC", nativeQuery = true)
    Page<Newproduct> findAllByPage(Pageable pageable);

    //-------------------------------Without-Page-----------------------------------

    @Query(value = "SELECT * FROM newproduct WHERE Status !=2 ORDER BY product_id DESC", nativeQuery = true)
    List<Newproduct> findAllNewProductroductAndStatusNot();

    //-------Pending


   // @Query(value = "SELECT * FROM newproduct np WHERE (np.drug_code like concat('%',?1,'%') or np.generic_name like concat('%',?1,'%') or np.group_name like concat('%',?1,'%') or np.mrp like concat('%',?1,'%') or np.unit_size like concat('%',?1,'%')) AND status !=2 ", nativeQuery = true)
//    @Query(value = "SELECT * FROM newproduct bg WHERE " +
//            "bg.status != 2 AND (" +
//            "(LOWER(bg.drug_code) LIKE LOWER(concat('%',:searchText,'%'))) " +
//            "OR (LOWER(bg.generic_name) LIKE LOWER(concat('%',:searchText,'%'))) "+
//            "OR (LOWER(bg.group_name) LIKE LOWER(concat('%',:searchText,'%'))) "+
//            "OR (LOWER(bg.product_id) LIKE LOWER(concat('%',:searchText,'%'))) "+
//            "OR (LOWER(bg.mrp) LIKE LOWER(concat('%',:searchText,'%'))) "+
//            "OR (LOWER(bg.unit_size) LIKE LOWER(concat('%',:searchText,'%')))", nativeQuery = true)
//    List<Newproduct> findProductBySearch(@Param("searchText") String searchText);
   @Query(value = "SELECT * FROM newproduct bg WHERE " +
           "bg.status != 2 AND (" +
           "LOWER(bg.drug_code) = LOWER(:searchText) " +
           "OR LOWER(bg.generic_name) = LOWER(:searchText) " +
           "OR LOWER(bg.group_name) = LOWER(:searchText) " +
           "OR LOWER(bg.mrp) = LOWER(:searchText) " +
           "OR LOWER(bg.unit_size) = LOWER(:searchText))", nativeQuery = true)
   List<Newproduct> findProductBySearch(@Param("searchText") String searchText);


   // @Query(value = "SELECT * FROM newproduct np WHERE Status !=2 ORDER BY np.product_id ASC,np.drug_code ASC,np.generic_name ASC,np.group_name ASC,np.mrp ASC,np.unit_size ASC", nativeQuery = true)
   @Query(value = "SELECT * FROM newproduct np WHERE np.status != 2 ORDER BY " +
           "CASE WHEN :columnName = 'product_id' THEN np.product_id END ASC, " +
           "CASE WHEN :columnName = 'drug_code' THEN np.drug_code END ASC, " +
           "CASE WHEN :columnName = 'generic_name' THEN np.generic_name END ASC, " +
           "CASE WHEN :columnName = 'group_name' THEN np.group_name END ASC, " +
           "CASE WHEN :columnName = 'mrp' THEN np.mrp END ASC, " +
           "CASE WHEN :columnName = 'unit_size' THEN np.unit_size END ASC",nativeQuery = true)
    List<Newproduct> searchAndOrderByASC(@Param("columnName") String columnName);

  //  @Query(value = "SELECT * FROM newproduct np WHERE Status !=2 ORDER BY np.product_id DESC,np.drug_code DESC,np.generic_name DESC,np.group_name DESC,np.mrp DESC,np.unit_size DESC", nativeQuery = true)
  @Query(value = "SELECT * FROM newproduct np " +
          "WHERE np.status != 2 " +
          "ORDER BY " +
          "CASE WHEN :columnName = 'product_id' THEN np.product_id END DESC, " +
          "CASE WHEN :columnName = 'drug_code' THEN np.drug_code END DESC, " +
          "CASE WHEN :columnName = 'generic_name' THEN np.generic_name END DESC, " +
          "CASE WHEN :columnName = 'group_name' THEN np.group_name END DESC, " +
          "CASE WHEN :columnName = 'mrp' THEN np.mrp END DESC, " +
          "CASE WHEN :columnName = 'unit_size' THEN np.unit_size END DESC ", nativeQuery = true )
    List<Newproduct> searchAndOrderByDESC(@Param("columnName") String columnName);

   // @Query(value = "SELECT * FROM newproduct np WHERE (np.drug_code like concat('%',?1,'%') or np.generic_name like concat('%',?1,'%') or np.group_name like concat('%',?1,'%') or np.mrp like concat('%',?1,'%') or np.unit_size like concat('%',?1,'%')) AND np.status !=2 ORDER BY np.product_id ASC", nativeQuery = true)
   @Query(value = "SELECT * FROM newproduct bg WHERE " +
           "(LOWER(bg.drug_code) = LOWER(:searchText) " +
           "OR LOWER(bg.generic_name) = LOWER(:searchText) " +
           "OR LOWER(bg.group_name) = LOWER(:searchText) " +
           "OR LOWER(bg.mrp) = LOWER(:searchText) " +
           "OR LOWER(bg.unit_size) = LOWER(:searchText)) " +
           "AND bg.status != 2 " +
           " ORDER BY " +
           " CASE WHEN :columnName = 'drug_code' THEN bg.drug_code END ASC," +
           " CASE WHEN :columnName = 'generic_name' THEN bg.generic_name END ASC," +
           " CASE WHEN :columnName = 'group_name' THEN bg.group_name END ASC," +
           " CASE WHEN :columnName = 'mrp' THEN bg.mrp END ASC," +
           " CASE WHEN :columnName = 'unit_size' THEN bg.unit_size END ASC", nativeQuery = true)
    List<Newproduct> findASC(@Param("searchText") String searchText,@Param("columnName") String columnName);

   // @Query(value = "SELECT * FROM newproduct np WHERE (np.drug_code like concat('%',?1,'%') or np.generic_name like concat('%',?1,'%') or np.group_name like concat('%',?1,'%') or np.mrp like concat('%',?1,'%') or np.unit_size like concat('%',?1,'%')) AND np.status !=2 ORDER BY np.product_id DESC ", nativeQuery = true)
   @Query(value = "SELECT * FROM newproduct bg WHERE " +
           "(LOWER(bg.drug_code) = LOWER(:searchText) " +
           "OR LOWER(bg.generic_name) = LOWER(:searchText) " +
           "OR LOWER(bg.group_name) = LOWER(:searchText) " +
           "OR LOWER(bg.mrp) = LOWER(:searchText) " +
           "OR LOWER(bg.unit_size) = LOWER(:searchText)) " +
           "AND bg.status != 2 " +
           " ORDER BY " +
           " CASE WHEN :columnName = 'drug_code' THEN bg.drug_code END DESC," +
           " CASE WHEN :columnName = 'generic_name' THEN bg.generic_name END DESC," +
           " CASE WHEN :columnName = 'group_name' THEN bg.group_name END DESC," +
           " CASE WHEN :columnName = 'mrp' THEN bg.mrp END DESC," +
           " CASE WHEN :columnName = 'unit_size' THEN bg.unit_size END DESC", nativeQuery = true)
    List<Newproduct> findDESC(@Param("searchText")String searchText,@Param("columnName") String columnName);


    //-------------------------------page-Request-----------------------------------
    @Query(value = "SELECT * FROM newproduct WHERE Status !=2 ORDER BY product_id DESC", nativeQuery = true)
    Page<Newproduct> findAllAndStatu(Pageable pageable);

  //  @Query(value = "SELECT * FROM newproduct np WHERE (np.drug_code like concat('%',?1,'%') or np.generic_name like concat('%',?1,'%') or np.group_name like concat('%',?1,'%') or np.mrp like concat('%',?1,'%') or np.unit_size like concat('%',?1,'%')) Status !=2 ORDER BY product_id DESC", nativeQuery = true)
  @Query(value = "SELECT * FROM newproduct bg WHERE " +
          "bg.status != 2 AND (" +
          "LOWER(bg.drug_code) = LOWER(:searchText) " +
          "OR LOWER(bg.generic_name) = LOWER(:searchText) " +
          "OR LOWER(bg.group_name) = LOWER(:searchText) " +
          "OR LOWER(bg.product_id) = LOWER(:searchText) " +
          "OR LOWER(bg.mrp) = LOWER(:searchText) " +
          "OR LOWER(bg.unit_size) = LOWER(:searchText))", nativeQuery = true)
  Page<Newproduct> findAllNewProductsAndStatusNot22( @Param("searchText") String searchText, Pageable pageable);

//    @Query(value = "SELECT * FROM newproduct bg WHERE " +
//            "bg.status != 2 AND (" +
//            "LOWER(bg.drug_code) = LOWER(:searchText) " +
//            "OR LOWER(bg.generic_name) = LOWER(:searchText) " +
//            "OR LOWER(bg.group_name) = LOWER(:searchText) " +
//            "OR LOWER(bg.product_id) = LOWER(:searchText) " +
//            "OR LOWER(bg.mrp) = LOWER(:searchText) " +
//            "OR LOWER(bg.unit_size) = LOWER(:searchText))",
//            countQuery = "SELECT count(*) FROM newproduct bg WHERE " +
//                    "bg.status != 2 AND (" +
//                    "LOWER(bg.drug_code) = LOWER(:searchText) " +
//                    "OR LOWER(bg.generic_name) = LOWER(:searchText) " +
//                    "OR LOWER(bg.group_name) = LOWER(:searchText) " +
//                    "OR LOWER(bg.product_id) = LOWER(:searchText) " +
//                    "OR LOWER(bg.mrp) = LOWER(:searchText) " +
//                    "OR LOWER(bg.unit_size) = LOWER(:searchText))",
//            nativeQuery = true)
//    Page<Newproduct> findAllNewProductsAndStatusNot22(@Param("searchText") String searchText, Pageable pageable);


//  @Query(value = "SELECT * FROM newproduct bg WHERE " +
//          "bg.status != 2 AND (" +
//          "LOWER(bg.drug_code) LIKE LOWER(CONCAT('%', :searchText, '%')) " +
//          "OR LOWER(bg.generic_name) LIKE LOWER(CONCAT('%', :searchText, '%')) " +
//          "OR LOWER(bg.group_name) LIKE LOWER(CONCAT('%', :searchText, '%')) " +
//          "OR LOWER(bg.product_id) LIKE LOWER(CONCAT('%', :searchText, '%')) " +
//          "OR LOWER(bg.mrp) LIKE LOWER(CONCAT('%', :searchText, '%')) " +
//          "OR LOWER(bg.unit_size) LIKE LOWER(CONCAT('%', :searchText, '%')))",
//          nativeQuery = true)
//  Page<Newproduct> findAllNewProductsAndStatusNot22(@Param("searchText") String searchText,Pageable pageable);


    //  Page<Newproduct> findAllNewProductsAndStatusNot22(Pageable pageable,@Param("searchText") String searchText);

   // Page<Newproduct> findAllNewProductsAndStatusNot22(Pageable pageable,@Param("searchText") String searchText);

    @Query(value = "SELECT * FROM newproduct np WHERE np.status != 2 ORDER BY " +
            "CASE WHEN :columnName = 'product_id' THEN np.product_id END ASC, " +
            "CASE WHEN :columnName = 'drug_code' THEN np.drug_code END ASC, " +
            "CASE WHEN :columnName = 'generic_name' THEN np.generic_name END ASC, " +
            "CASE WHEN :columnName = 'group_name' THEN np.group_name END ASC, " +
            "CASE WHEN :columnName = 'mrp' THEN np.mrp END ASC, " +
            "CASE WHEN :columnName = 'unit_size' THEN np.unit_size END ASC",nativeQuery = true)
    Page<Newproduct> searchAndOrderByASC(@Param("columnName") String columnName, Pageable pageable);

//    @Query(value = "SELECT * FROM newproduct np WHERE Status !=2 ORDER BY np.product_id ASC,np.drug_code ASC,np.generic_name ASC,np.group_name ASC,np.mrp ASC,np.unit_size ASC ", nativeQuery = true)
//    Page<Newproduct> searchAndOrderByASC(String columnName, Pageable pageable);

    @Query(value = "SELECT * FROM newproduct np " +
            "WHERE np.status != 2 " +
            "ORDER BY " +
            "CASE WHEN :columnName = 'product_id' THEN np.product_id END DESC, " +
            "CASE WHEN :columnName = 'drug_code' THEN np.drug_code END DESC, " +
            "CASE WHEN :columnName = 'generic_name' THEN np.generic_name END DESC, " +
            "CASE WHEN :columnName = 'group_name' THEN np.group_name END DESC, " +
            "CASE WHEN :columnName = 'mrp' THEN np.mrp END DESC, " +
            "CASE WHEN :columnName = 'unit_size' THEN np.unit_size END DESC ", nativeQuery = true )
    Page<Newproduct> searchAndOrderByDESC(Pageable pageable, @Param("columnName") String columnName);

   // Page<Newproduct> searchAndOrderByDESC( Pageable pageable,@Param("columnName") String columnName);
//    @Query(value = "SELECT * FROM newproduct np WHERE Status !=2 ORDER BY np.product_id DESC,np.drug_code DESC,np.generic_name DESC,np.group_name DESC,np.mrp DESC,np.unit_size DESC ", nativeQuery = true)
//    Page<Newproduct> searchAndOrderByDESC(Pageable pageable, String columnName);

   // @Query(value = "SELECT * FROM newproduct np WHERE (np.drug_code like concat('%',?1,'%') or np.generic_name like concat('%',?1,'%') or np.group_name like concat('%',?1,'%') or np.mrp like concat('%',?1,'%') or np.unit_size like concat('%',?1,'%')) AND np.status !=2 ORDER BY np.product_id ASC,np.drug_code ASC,np.generic_name ASC,np.group_name ASC,np.mrp ASC,np.unit_size ASC ", nativeQuery = true)
   @Query(value = "SELECT * FROM newproduct bg WHERE " +
           "(LOWER(bg.drug_code) = LOWER(:searchText) " +
                   "OR LOWER(bg.generic_name) = LOWER(:searchText) " +
                   "OR LOWER(bg.group_name) = LOWER(:searchText) " +
                   "OR LOWER(bg.mrp) = LOWER(:searchText) " +
                   "OR LOWER(bg.unit_size) = LOWER(:searchText)) " +
                   "AND bg.status != 2 " +
           " ORDER BY " +
           " CASE WHEN :columnName = 'drug_code' THEN bg.drug_code END ASC," +
           " CASE WHEN :columnName = 'generic_name' THEN bg.generic_name END ASC," +
           " CASE WHEN :columnName = 'group_name' THEN bg.group_name END ASC," +
           " CASE WHEN :columnName = 'mrp' THEN bg.mrp END ASC," +
           " CASE WHEN :columnName = 'unit_size' THEN bg.unit_size END ASC", nativeQuery = true)
    Page<Newproduct> findASC(@Param("searchText") String searchText,@Param("columnName") String columnName, Pageable pageable);


   // @Query(value = "SELECT * FROM newproduct np WHERE (np.drug_code like concat('%',?1,'%') or np.generic_name like concat('%',?1,'%') or np.group_name like concat('%',?1,'%') or np.mrp like concat('%',?1,'%') or np.unit_size like concat('%',?1,'%')) AND np.status !=2 ORDER BY np.product_id DESC,np.drug_code DESC,np.generic_name DESC,np.group_name DESC,np.mrp DESC,np.unit_size DESC ", nativeQuery = true)
   @Query(value = "SELECT * FROM newproduct bg WHERE " +
           "(LOWER(bg.drug_code) = LOWER(:searchText) " +
           "OR LOWER(bg.generic_name) = LOWER(:searchText) " +
           "OR LOWER(bg.group_name) = LOWER(:searchText) " +
           "OR LOWER(bg.mrp) = LOWER(:searchText) " +
           "OR LOWER(bg.unit_size) = LOWER(:searchText)) " +
           "AND bg.status != 2 " +
           " ORDER BY " +
           " CASE WHEN :columnName = 'drug_code' THEN bg.drug_code END DESC," +
           " CASE WHEN :columnName = 'generic_name' THEN bg.generic_name END DESC," +
           " CASE WHEN :columnName = 'group_name' THEN bg.group_name END DESC," +
           " CASE WHEN :columnName = 'mrp' THEN bg.mrp END DESC," +
           " CASE WHEN :columnName = 'unit_size' THEN bg.unit_size END DESC", nativeQuery = true)
    Page<Newproduct> findDESC(@Param("searchText")String searchText, @Param("columnName")String columnName, Pageable pageable);


    List<Newproduct> findAllByStatus(Short status);



    Page<Newproduct> findAllByStatus(Short status, Pageable pageable);
}

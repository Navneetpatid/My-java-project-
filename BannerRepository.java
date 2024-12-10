package com.janaushadhi.adminservice.repository;

import com.janaushadhi.adminservice.entity.Banner;
import feign.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BannerRepository extends JpaRepository<Banner, Long> {

    @Query(value = "SELECT * FROM banner where Status !=2 ORDER BY id DESC", nativeQuery = true)
    List<Banner> findAllNewProductroductAndStatusNot();

    @Query(value = "SELECT * FROM banner b WHERE Status !=2 ORDER BY b.id ASC", nativeQuery = true)
    List<Banner>  getAndOrderByASC(@Param("columnName") String columnName);

    @Query(value = "SELECT * FROM banner b WHERE Status !=2 ORDER BY b.id DESC", nativeQuery = true)
    List<Banner> getAndOrderByDESC(@Param("columnName")  String columnName);

    //----------------------------------------------------------------------

    @Query(value = "SELECT * FROM banner where Status !=2 ORDER BY id DESC", nativeQuery = true)
    Page<Banner> findAllAndStatus(Pageable pageable);

    @Query(value = "SELECT * FROM banner b WHERE Status !=2 ORDER BY b.id ASC", nativeQuery = true)
    Page<Banner> getAndOrderByPageASC(@Param("columnName")  String columnName, Pageable pageable);

    @Query(value = "SELECT * FROM banner b WHERE Status !=2 ORDER BY b.id DESC", nativeQuery = true)
    Page<Banner> getAndOrderByPageDESC(Pageable pageable,@Param("columnName")  String columnName);


    List<Banner> findAllByStatus(Short status);

    Page<Banner> findAllByStatus(Short status, Pageable pageable);
}

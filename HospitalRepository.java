package com.janaushadhi.adminservice.repository;

import com.janaushadhi.adminservice.entity.Hospital;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HospitalRepository extends JpaRepository<Hospital,Long> {
    List<Hospital> findAllByStateAndDistrict( String state,String district,  Sort sort);

    List<Hospital> findAllByStateAndDistrictAndHsplType(String state, String district, String hsplType, Sort sort);

    List<Hospital> findAllByState(String state, Sort sort);

    Page<Hospital> findAllByStateAndDistrictAndHsplType(String state, String district, String hsplType, Pageable pageable);

    Page<Hospital> findAllByStateAndDistrict(  String state,String district,  Pageable pageable);

    Page<Hospital> findAllByState(String state, Pageable pageable);

    List<Hospital> findAllByHsplType(String hsplType, Sort sort);

    Page<Hospital> findAllByHsplType(String hsplType, Pageable pageable);

    Page<Hospital> findAllByStateAndHsplType(String state, String hsplType, Pageable pageable);

    List<Hospital> findAllByStateAndHsplType(String state, String hsplType, Sort sort);
}

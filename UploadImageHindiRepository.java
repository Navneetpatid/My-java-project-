package com.janaushadhi.adminservice.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.janaushadhi.adminservice.entity.UploadImageHindi;


@Repository
public interface UploadImageHindiRepository extends JpaRepository<UploadImageHindi,Long> {
	
}
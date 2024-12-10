package com.janaushadhi.adminservice.repository;

import com.janaushadhi.adminservice.entity.GoverningCouncil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GovernanceCounsilRepository extends JpaRepository<GoverningCouncil,Long> {


}

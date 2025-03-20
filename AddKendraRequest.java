package com.hsbc.hap.cdr.dao;

import com.hsbc.hap.cdr.entity.CpMaster;
import com.hsbc.hap.cdr.entity.CpMasterId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CpMasterDetailsDao extends JpaRepository<CpMaster, CpMasterId> {

    // Fetch all CP Masters for a given region
    @Query("SELECT c FROM CpMaster c WHERE c.id.region = :region")
    List<CpMaster> findByRegion(@Param("region") String region);

    // Find specific CP Master for region and environment
    @Query("SELECT c FROM CpMaster c WHERE c.id.region = :region AND c.id.environment = :environment")
    Optional<CpMaster> findByRegionAndEnvironment(@Param("region") String region, @Param("environment") String environment);
}

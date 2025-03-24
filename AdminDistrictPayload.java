package com.hsbc.hap.cer.dao;

import com.hsbc.hap.cer.model.CpMaster;
import com.hsbc.hap.cer.model.CpMasterId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CpMasterDetailsDao extends JpaRepository<CpMaster, CpMasterId> {

    // Primary query method matching the service implementation
    @Query("SELECT c FROM CpMaster c WHERE c.region = :region AND c.environment = :environment")
    CpMaster findById_RegionAndId_Environment(@Param("region") String region, 
                                            @Param("environment") String environment);

    // Additional method to get just the URL with the same parameters
    @Query("SELECT c.cpAdminApiUrl FROM CpMaster c WHERE c.region = :region AND c.environment = :environment")
    String findCpAdminApiUrlByRegionAndEnvironment(@Param("region") String region, 
                                                 @Param("environment") String environment);

    // Combined query that maintains your original JOIN logic but returns in the expected format
    @Query("SELECT new map(c.cpAdminApiUrl as cp_admin_api_url) FROM CpMaster c " +
           "WHERE (c.region, c.environment) IN " +
           "(SELECT a.region, b.environment FROM EngagementTarget a " +
           "JOIN WorkspaceTarget b ON a.engagementId = b.engagementId " +
           "WHERE b.engagementId = :engagementId)")
    List<Map<String, Object>> findCpAdminApiUrlMapByEngagementId(@Param("engagementId") String engagementId);
}

package com.hsbc.hap.cer.dao;

import com.hsbc.hap.cer.entity.CpMaster;
import com.hsbc.hap.cer.entity.CpMasterId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CpMasterDetailsDao extends JpaRepository<CpMaster, CpMasterId> {

    // Query using embedded ID components
    @Query("SELECT c FROM CpMaster c WHERE c.id.region = :region AND c.id.environment = :environment")
    CpMaster findByRegionAndEnvironment(@Param("region") String region, 
                                      @Param("environment") String environment);

    // Query to get just the API URL
    @Query("SELECT c.cpAdminApiUrl FROM CpMaster c WHERE c.id.region = :region AND c.id.environment = :environment")
    String findCpAdminApiUrlByRegionAndEnvironment(@Param("region") String region, 
                                                @Param("environment") String environment);

    // Query using all components of the embedded ID
    @Query("SELECT c FROM CpMaster c WHERE c.id.region = :region AND c.id.platform = :platform AND c.id.environment = :environment")
    CpMaster findByRegionAndPlatformAndEnvironment(@Param("region") String region,
                                                @Param("platform") String platform,
                                                @Param("environment") String environment);

    // Native query version that matches your original SQL
    @Query(value = "SELECT c.cp_admin_api_url FROM cp_master c " +
                   "WHERE (c.region, c.environment) IN " +
                   "(SELECT a.region, b.environment FROM engagement_target a " +
                   "JOIN workspace_target b ON a.engagement_id = b.engagement_id " +
                   "WHERE b.engagement_id = :engagementId)", 
           nativeQuery = true)
    String findCpAdminApiUrlByEngagementId(@Param("engagementId") String engagementId);
}

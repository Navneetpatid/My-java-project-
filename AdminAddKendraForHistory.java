package com.hsbc.hap.cer.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CpMasterDetailsDao extends JpaRepository<CpMaster, CpMasterId> {
    
    // Direct query based on the SQL in your image
    @Query(value = "SELECT c.cp_admin_api_url FROM cp_master c " +
                   "WHERE (c.region, c.environment) IN " +
                   "(SELECT a.region, b.environment FROM engagement_target a " +
                   "JOIN workspace_target b ON a.engagement_id = b.engagement_id " +
                   "WHERE b.engagement_id = :engagementId)", 
           nativeQuery = true)
    String findCpAdminApiUrlByEngagementId(@Param("engagementId") String engagementId);
    
    // Alternative JPA version (non-native query)
    @Query("SELECT c.cpAdminApiUrl FROM CpMaster c " +
           "WHERE c.region IN (SELECT e.region FROM EngagementTarget e WHERE e.engagementId = :engagementId) " +
           "AND c.environment IN (SELECT w.environment FROM WorkspaceTarget w WHERE w.engagementId = :engagementId)")
    String findCpAdminApiUrlByEngagementIdJpa(@Param("engagementId") String engagementId);
}

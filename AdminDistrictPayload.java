package com.hsbc.hap.cer.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CpMasterDetailsDao extends JpaRepository<CpMaster, CpMasterId> {

    /**
     * Finds CP Admin API URL by engagement ID and workspace
     * Matches the exact SQL query from requirements
     * 
     * @param engagementId The engagement ID to search for
     * @param workspace The workspace name to filter by
     * @return The CP Admin API URL or null if not found
     */
    @Query(value = "SELECT c.cp_admin_api_url FROM cp_master c " +
                   "WHERE (c.region, c.environment) IN " +
                   "(SELECT a.region, b.environment FROM engagement_target a " +
                   "JOIN workspace_target b ON a.engagement_id = b.engagement_id " +
                   "WHERE a.engagement_id = :engagementId AND b.workspace = :workspace)", 
           nativeQuery = true)
    String findAdminApiUrlByEngagementAndWorkspace(
            @Param("engagementId") String engagementId,
            @Param("workspace") String workspace);
}

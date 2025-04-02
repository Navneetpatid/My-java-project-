public interface CpMasterDetailsDao extends JpaRepository<CpMaster, CpMasterId> {
    
    @Query("SELECT cm.cpAdminApiUrl FROM CpMaster cm " +
           "WHERE cm.id.region = (SELECT et.region FROM EngagementTarget et WHERE et.engagementId = :engagementId) " +
           "AND cm.id.environment = (SELECT wt.environment FROM WorkspaceTarget wt " +
           "WHERE wt.id.engagementId = :engagementId AND wt.id.workspace = :workspace)")
    Optional<String> findCpAdminApiUrl(
        @Param("engagementId") String engagementId,
        @Param("workspace") String workspace);
}

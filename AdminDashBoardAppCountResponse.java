public interface EngagementTargetRepository extends JpaRepository<EngagementTarget, String> {
}

public interface WorkspaceTargetRepository extends JpaRepository<WorkspaceTarget, WorkspaceTargetId> {
    Optional<WorkspaceTarget> findByEngagementIdAndWorkspace(String engagementId, String workspace);
}

public interface EngagementPluginRepository extends JpaRepository<EngagementPlugin, String> {
    List<EngagementPlugin> findByEngagementId(String engagementId);
}

public interface CpMasterRepository extends JpaRepository<CpMaster, CpMasterId> {
    @Query("SELECT cm.cpAdminApiUrl FROM CpMaster cm " +
           "WHERE EXISTS (SELECT 1 FROM EngagementTarget et " +
           "JOIN WorkspaceTarget wt ON et.engagementId = wt.engagementId " +
           "WHERE et.engagementId = :engagementId " +
           "AND wt.workspace = :workspace " +
           "AND et.region = cm.id.region " +
           "AND wt.environment = cm.id.environment)")
    Optional<String> findCpAdminApiUrlByEngagementAndWorkspace(
        @Param("engagementId") String engagementId, 
        @Param("workspace") String workspace);
}

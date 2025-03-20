@Repository
public interface WorkspaceTargetDetailsDao extends JpaRepository<WorkspaceTargetDetails, WorkspaceTargetId> {

    // Fetch all workspaces for a given engagement ID
    @Query("SELECT w FROM WorkspaceTargetDetails w WHERE w.id.engagementId = :engagementId")
    List<WorkspaceTargetDetails> findByEngagementId(@Param("engagementId") String engagementId);

    // Find specific workspace for engagement
    Optional<WorkspaceTargetDetails> findById(WorkspaceTargetId id);
}

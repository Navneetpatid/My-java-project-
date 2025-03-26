public interface CpMasterDetailsDao extends JpaRepository<CpMaster, CpMasterId> {

    @Query("SELECT c.cp_admin_api_url FROM cp_master c WHERE (c.region, c.environment) IN " +
           "(SELECT e.region, w.environment FROM engagement_target e JOIN workspace_target w " +
           "ON e.engagement_id = w.engagement_id WHERE e.engagement_id = :engagementId AND w.workspace = :workspace)")
    Optional<String> findCpAdminApiUrl(@Param("engagementId") String engagementId,
                                     @Param("workspace") String workspace);

    CpMaster findById_RegionAndId_Environment(String region, String environment);
}

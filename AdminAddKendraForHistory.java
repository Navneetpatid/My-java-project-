public interface CpMasterDetailsDao extends JpaRepository<CpMaster, Long> {
    
    // Method 1: Find by region AND environment (exact match)
    CpMaster findByRegionAndEnvironment(String region, String environment);

    // Method 2: Find all entries for a given environment (e.g., "PRD")
    List<CpMaster> findByEnvironment(String environment);

    // Method 3: Custom query (if column names differ)
    @Query("SELECT c FROM CpMaster c WHERE c.region = :region AND c.environment = :env")
    CpMaster findCustomQuery(@Param("region") String region, @Param("env") String environment);
}

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Map;

@Repository
public interface DataRepository extends CrudRepository<CpMaster, String> {

    // Fetch CP URL
    @Query("SELECT c.cpAdminApiUrl FROM CpMaster c")
    String findCpUrl();

    // Fetch engagement data (engagementId, gbgf) from EngagementTargetShp
    @Query("SELECT e.engagementId, e.gbgf FROM EngagementTargetShp e WHERE e.engagementId = :engagementId")
    Map<String, Object> getEngagementData(String engagementId);

    // Fetch workspace and dp_host_url from WorkspaceTargetDetails
    @Query("SELECT e.workspace, e.dpHostUrl FROM WorkspaceTargetDetails e WHERE e.engagementId = :engagementId AND e.workspace = :workspace")
    Map<String, Object> getWorkspaceData(String engagementId, String workspace);

    // Fetch mandatory plugins for an engagement
    @Query("SELECT e.mandatoryPlugin FROM EngagementPluginDetail e WHERE e.engagementId = :engagementId")
    List<String> findMandatoryPlugins(String engagementId);

    // Fetch dpHostUrl with a LIMIT 1 constraint (assuming we fetch only one result)
    @Query("SELECT e.dpHostUrl FROM WorkspaceTargetDetails e WHERE e.engagementId = :engagementId AND e.workspace = :workspace")
    String getDpHostUrl(String engagementId, String workspace);

    // Fetch gbgf value for an engagement
    @Query("SELECT e.gbgf FROM EngagementTargetShp e WHERE e.engagementId = :engagementId")
    String findGbgf(String engagementId);

    // Fetch dmz_lb value for an engagement
    @Query("SELECT e.dmzLb FROM EngagementTargetShp e WHERE e.engagementId = :engagementId")
    String findDmzLb(String engagementId);

    // Fetch logs for an engagement
    @Query("SELECT e.logs FROM EngagementTargetShp e WHERE e.engagementId = :engagementId")
    String findLogs(String engagementId);
}

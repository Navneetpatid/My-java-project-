import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DataRepository extends CrudRepository<CpMaster, String> {

    @Query("SELECT c.cpAdminApiUrl FROM CpMaster c")
    String findCpUrl(); // Assuming there's a single CP URL

    @Query("SELECT e.mandatoryPlugin FROM EngagementPlugin e WHERE e.engagementId = :engagementId")
    List<String> findMandatoryPlugins(String engagementId);

    @Query("SELECT e.workspace FROM WorkspaceTarget e WHERE e.engagementId = :engagementId")
    List<String> findWorkspace(String engagementId);

    @Query("SELECT e.dpHostUrl FROM WorkspaceTarget e WHERE e.engagementId = :engagementId AND e.workspace = :workspace")
    String findDpHost(String engagementId, String workspace);

    @Query("SELECT e.gbgf FROM EngagementTarget e WHERE e.engagementId = :engagementId")
    String findGbgf(String engagementId);

    @Query("SELECT e.dmzLb FROM EngagementTarget e WHERE e.engagementId = :engagementId")
    String findDmzLb(String engagementId);

    @Query("SELECT e.logs FROM EngagementTarget e WHERE e.engagementId = :engagementId")
    String findLogs(String engagementId);
}

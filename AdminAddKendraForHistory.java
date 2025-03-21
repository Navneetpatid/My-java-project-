import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DataRepository extends CrudRepository<CpMaster, String> {

    @Query("SELECT c.cpAdminApiUrl FROM CpMaster c WHERE c.region = :region AND c.platform = :platform AND c.environment = :environment")
    String findCpUrl(String region, String platform, String environment);

    @Query("SELECT e.mandatoryPlugin FROM EngagementPlugin e WHERE e.engagementId = :engagementId")
    List<String> findMandatoryPlugins(String engagementId);

    @Query("SELECT e.workspace FROM WorkspaceTarget e WHERE e.engagementId = :engagementId")
    List<String> findWorkspace(String engagementId);

    @Query("SELECT e.dpHostUrl FROM WorkspaceTarget e WHERE e.engagementId = :engagementId AND e.workspace = :workspace")
    String findDpHost(String engagementId, String workspace);

    @Query("SELECT e.gbgf FROM EngagementTarget e WHERE e.engagementId = :engagementId")
    String findGbgf(String engagementId);
}

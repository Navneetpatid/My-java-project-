import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CpMasterDetailsDao extends JpaRepository<CpMaster, CpMasterId> {
    
    // Standard method using embedded ID
    Optional<CpMaster> findById(CpMasterId id);
    
    // Custom query method alternative
    CpMaster findById_RegionAndId_PlatformAndId_Environment(
        String region, 
        String platform, 
        String environment);
}

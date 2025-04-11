import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import java.util.Optional;
import java.util.List;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class MapCERServiceImplTest {

    @Mock
    private EngagementTargetKongDao engagementTargetKongDao;
    
    @Mock
    private WorkspaceTargetDetailsDao workspaceTargetDetailsDao;
    
    @Mock
    private EngagementPluginDetailsDao engagementPluginDetailsDao;
    
    @Mock
    private CpMasterDetailsDao cpMasterDetailsDao;
    
    @Mock
    private DmzLibMasterDao dmzLibMasterDao;
    
    @InjectMocks
    private MapCERServiceImpl mapCERService;
    
    private final String engagementId = "ENG123";
    private final String workspace = "WS1";
    private final String environment = "DEV";
    private final String region = "US";

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetCerEngagementData_Success() {
        // Mock data setup
        EngagementTargetKong engagementTarget = new EngagementTargetKong();
        engagementTarget.setDbgf("DBGF_VALUE");
        
        WorkspaceTarget workspaceTarget = new WorkspaceTarget();
        workspaceTarget.setEnvironment(environment);
        workspaceTarget.setOp_host_url("HOST_URL");
        
        List<String> mandatoryPlugins = Arrays.asList("plugin1", "plugin2");
        String cpAdminUrl = "CP_ADMIN_URL";
        String dmzLb = "DMZ_LB_VALUE";

        // Mock DAO responses
        Mockito.when(engagementTargetKongDao.findByEngagementId(engagementId))
               .thenReturn(Optional.of(engagementTarget));
        Mockito.when(workspaceTargetDetailsDao.findById_EngagementIdAndId_Workspace(engagementId, workspace))
               .thenReturn(Optional.of(workspaceTarget));
        Mockito.when(engagementPluginDetailsDao.findMandatoryPluginsByEngagementId(engagementId))
               .thenReturn(mandatoryPlugins);
        Mockito.when(cpMasterDetailsDao.findCpAdminApiUrl(engagementId, workspace))
               .thenReturn(Optional.of(cpAdminUrl));
        Mockito.when(dmzLibMasterDao.findLoadBalancerByEnvironmentAndRegion(environment, region))
               .thenReturn(Optional.of(dmzLb));

        // Execute
        CerGetResponse response = mapCERService.getCerEngagementData(engagementId, workspace);

        // Verify
        assertTrue(response.isSuccess());
        assertEquals("DBGF_VALUE", response.getDbgf());
        assertEquals(workspace, response.getWorkspace());
        assertEquals("HOST_URL", response.getOp_host_url());
        assertEquals(mandatoryPlugins, response.getMandatoryPlugins());
        assertEquals(cpAdminUrl, response.getOp_admin_api_url());
        assertEquals(dmzLb, response.getDmz_Lb());
        assertTrue(response.getErrors().isEmpty());
        assertFalse(response.getLogs().isEmpty());
    }

    @Test
    void testGetCerEngagementData_EngagementNotFound() {
        Mockito.when(engagementTargetKongDao.findByEngagementId(engagementId))
               .thenReturn(Optional.empty());

        CerGetResponse response = mapCERService.getCerEngagementData(engagementId, workspace);

        assertFalse(response.isSuccess());
        assertTrue(response.getErrors().contains("engagementId not found"));
        assertTrue(response.getLogs().contains("engagementId : " + engagementId + " not found"));
    }

    @Test
    void testGetCerEngagementData_WorkspaceNotFound() {
        EngagementTargetKong engagementTarget = new EngagementTargetKong();
        Mockito.when(engagementTargetKongDao.findByEngagementId(engagementId))
               .thenReturn(Optional.of(engagementTarget));
        Mockito.when(workspaceTargetDetailsDao.findById_EngagementIdAndId_Workspace(engagementId, workspace))
               .thenReturn(Optional.empty());

        CerGetResponse response = mapCERService.getCerEngagementData(engagementId, workspace);

        assertFalse(response.isSuccess());
        assertTrue(response.getErrors().contains("Workspace not found"));
        assertTrue(response.getLogs().contains("Workspace : " + workspace + " not found"));
    }

    @Test
    void testGetCerEngagementData_CpAdminUrlNotFound() {
        EngagementTargetKong engagementTarget = new EngagementTargetKong();
        WorkspaceTarget workspaceTarget = new WorkspaceTarget();
        
        Mockito.when(engagementTargetKongDao.findByEngagementId(engagementId))
               .thenReturn(Optional.of(engagementTarget));
        Mockito.when(workspaceTargetDetailsDao.findById_EngagementIdAndId_Workspace(engagementId, workspace))
               .thenReturn(Optional.of(workspaceTarget));
        Mockito.when(cpMasterDetailsDao.findCpAdminApiUrl(engagementId, workspace))
               .thenReturn(Optional.empty());

        CerGetResponse response = mapCERService.getCerEngagementData(engagementId, workspace);

        assertFalse(response.isSuccess());
        assertTrue(response.getErrors().contains("No CP Admin API URL found"));
        assertTrue(response.getLogs().contains("CP Admin API URL not found"));
    }

    @Test
    void testGetCerEngagementData_DmzLoadBalancerNotFound() {
        EngagementTargetKong engagementTarget = new EngagementTargetKong();
        WorkspaceTarget workspaceTarget = new WorkspaceTarget();
        workspaceTarget.setEnvironment(environment);
        
        Mockito.when(engagementTargetKongDao.findByEngagementId(engagementId))
               .thenReturn(Optional.of(engagementTarget));
        Mockito.when(workspaceTargetDetailsDao.findById_EngagementIdAndId_Workspace(engagementId, workspace))
               .thenReturn(Optional.of(workspaceTarget));
        Mockito.when(dmzLibMasterDao.findLoadBalancerByEnvironmentAndRegion(environment, region))
               .thenReturn(Optional.empty());

        CerGetResponse response = mapCERService.getCerEngagementData(engagementId, workspace);

        assertFalse(response.isSuccess());
        assertTrue(response.getErrors().contains("DMZ Load Balancer not found"));
        assertTrue(response.getLogs().contains("DMZ Load Balancer not found"));
    }

    @Test
    void testGetCerEngagementData_ExceptionHandling() {
        Mockito.when(engagementTargetKongDao.findByEngagementId(engagementId))
               .thenThrow(new RuntimeException("Database error"));

        CerGetResponse response = mapCERService.getCerEngagementData(engagementId, workspace);

        assertFalse(response.isSuccess());
        assertTrue(response.getErrors().contains("Unexpected error"));
        assertTrue(response.getLogs().contains("Unexpected error"));
    }
            }

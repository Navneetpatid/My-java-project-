import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class HapCERServiceTest {

    @Mock
    private RequestResponseMapper requestResponseMapper;
    
    @Mock
    private EngagementTargetKongDao engagementTargetKongDao;
    
    @Mock
    private WorkspaceTargetDetailsDao workspaceTargetDetailsDao;
    
    @Mock
    private EngagementPluginDetailsDao engagementPluginDetailsDao;
    
    @InjectMocks
    private MapCERServiceImpl cerServiceImpl;
    
    private KongCerRequest request;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        request = new KongCerRequest();
        // Set up request properties as needed
    }

    @Test
    void testProcessKongCerRequest_Success() {
        // 1. Prepare test data
        EngagementTargetKong engagementTarget = new EngagementTargetKong();
        WorkspaceTarget workspaceTarget = new WorkspaceTarget();
        EngagementPluginDetail pluginDetail = new EngagementPluginDetail();
        
        // 2. Mock mapper responses
        Mockito.when(requestResponseMapper.mapToEngagementTargetKong(request))
               .thenReturn(engagementTarget);
        Mockito.when(requestResponseMapper.mapToWorkspaceTargetDetails(request))
               .thenReturn(workspaceTarget);
        Mockito.when(requestResponseMapper.mapToEngagementPluginDetail(request))
               .thenReturn(pluginDetail);
        
        // 3. Call service method
        ResponseDto<Map<String, Object>> response = 
            cerServiceImpl.processKongCerRequest(request);
        
        // 4. Verify results
        assertNotNull(response);
        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        assertEquals("Kong data saved successfully", response.getMessage());
        
        Map<String, Object> responseData = response.getData();
        assertNotNull(responseData);
        assertEquals(engagementTarget, responseData.get("engagementTargetKong"));
        assertEquals(workspaceTarget, responseData.get("workspaceTargetDetails"));
        assertEquals(pluginDetail, responseData.get("engagementPluginDetail"));
        
        // Verify DAO interactions
        Mockito.verify(engagementTargetKongDao).save(engagementTarget);
        Mockito.verify(workspaceTargetDetailsDao).save(workspaceTarget);
        Mockito.verify(engagementPluginDetailsDao).save(pluginDetail);
    }

    @Test
    void testProcessKongCerRequest_MappingFailure() {
        // Mock mapper to throw exception
        Mockito.when(requestResponseMapper.mapToEngagementTargetKong(request))
               .thenThrow(new RuntimeException("Mapping error"));
        
        // Call and verify exception
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> cerServiceImpl.processKongCerRequest(request));
        
        assertEquals("Mapping error", exception.getMessage());
        
        // Verify no DAO interactions occurred
        Mockito.verifyNoInteractions(engagementTargetKongDao);
        Mockito.verifyNoInteractions(workspaceTargetDetailsDao);
        Mockito.verifyNoInteractions(engagementPluginDetailsDao);
    }

    @Test
    void testProcessKongCerRequest_DaoFailure() {
        // Prepare test data
        EngagementTargetKong engagementTarget = new EngagementTargetKong();
        WorkspaceTarget workspaceTarget = new WorkspaceTarget();
        
        // Mock mapper responses
        Mockito.when(requestResponseMapper.mapToEngagementTargetKong(request))
               .thenReturn(engagementTarget);
        Mockito.when(requestResponseMapper.mapToWorkspaceTargetDetails(request))
               .thenReturn(workspaceTarget);
        
        // Mock DAO to throw exception
        Mockito.doThrow(new RuntimeException("Database error"))
               .when(engagementTargetKongDao).save(engagementTarget);
        
        // Call and verify exception
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> cerServiceImpl.processKongCerRequest(request));
        
        assertEquals("Database error", exception.getMessage());
        
        // Verify only first DAO was called
        Mockito.verify(engagementTargetKongDao).save(engagementTarget);
        Mockito.verifyNoInteractions(workspaceTargetDetailsDao);
        Mockito.verifyNoInteractions(engagementPluginDetailsDao);
    }
		}

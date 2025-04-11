import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

public class CERControllerTest {
    
    @InjectMocks
    private CERController controller;
    
    @Mock
    private HapCerService hapCerService;
    
    private String engagementId;
    private String workspace;
    
    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        engagementId = "engagement123";
        workspace = "workspace1";
    }

    @Test
    void testGetCerEngagementData_Success() {
        // 1. Prepare test response
        CerGetResponse cerGetResponse = new CerGetResponse();
        cerGetResponse.setEngagementId(engagementId);
        cerGetResponse.setWorkspace(workspace);
        // Set other response fields as needed
        
        // 2. Mock service call
        Mockito.when(hapCerService.getCerEngagementData(engagementId, workspace))
               .thenReturn(cerGetResponse);
        
        // 3. Call controller method
        ResponseEntity<CerGetResponse> response = 
            controller.getCerEngagementData(engagementId, workspace);
        
        // 4. Verify results
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(cerGetResponse, response.getBody());
    }

    @Test
    void testGetCerEngagementData_NotFound() {
        // Mock service to return null
        Mockito.when(hapCerService.getCerEngagementData(engagementId, workspace))
               .thenReturn(null);
        
        ResponseEntity<CerGetResponse> response = 
            controller.getCerEngagementData(engagementId, workspace);
        
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testBulkUpdate_Success() {
        // 1. Prepare test data
        QueryRequest queryRequest = new QueryRequest();
        // Set up query request properties
        
        List<QueryResult> expectedResults = new ArrayList<>();
        // Add expected query results
        
        // 2. Mock service call
        Mockito.when(hapCerService.executeQueries(queryRequest.getQueries()))
               .thenReturn(expectedResults);
        
        // 3. Call controller method
        ResponseEntity<?> response = controller.bulkUpdate(queryRequest);
        
        // 4. Verify results
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResults, response.getBody());
    }

    @Test
    void testBulkUpdate_Failure() {
        // 1. Prepare test data
        QueryRequest queryRequest = new QueryRequest();
        // Set up query request properties
        
        // 2. Mock service to throw exception
        Mockito.when(hapCerService.executeQueries(queryRequest.getQueries()))
               .thenThrow(new RuntimeException("Database error"));
        
        // 3. Call controller method
        ResponseEntity<?> response = controller.bulkUpdate(queryRequest);
        
        // 4. Verify error handling
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Server error"));
    }
    }

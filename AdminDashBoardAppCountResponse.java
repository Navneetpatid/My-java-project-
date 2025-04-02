import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.Map;
import java.util.LinkedHashMap;

public class CERControllerTest {

    @InjectMocks
    CERController controller;

    @Mock
    HapCerService hapCerService;

    private KongCerRequest request;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        request = new KongCerRequest();
        // Set any required request fields here
    }

    @Test
    void testProcessKongCerRequest() {
        Map<String, Object> expectedResponse = new LinkedHashMap<>();
        expectedResponse.put("message", "CER data processed successfully");

        ResponseDto<Map<String, Object>> mockResponseDto = new ResponseDto<>();
        mockResponseDto.setStatusCode(HttpStatus.OK.value());
        mockResponseDto.setMessage("CER data processed successfully");

        Mockito.when(hapCerService.processKongCerRequest(request)).thenReturn(mockResponseDto);
        
        ResponseEntity<Map<String, Object>> response = controller.processKongCerRequest(request, null);
        
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());
    }
}

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.Arrays;
import java.util.Map;
import java.util.LinkedHashMap;

public class CERControllerTest {
    
    @InjectMocks
    private CERController controller;
    
    @Mock
    private HapCERService hapCERService;
    
    private ShpCerRequest request;
    private KongCerRequest kongRequest;
    
    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        request = new ShpCerRequest();
        request.setPlatform("ikp");
        kongRequest = new KongCerRequest();
    }

    @Test
    void testProcessShpCerRequest() {
        String expectedResponse = "{\"message\": \"CER data saved successfully\"}";
        Mockito.when(hapCERService.processShpCerRequest(request)).thenReturn(expectedResponse);
        
        ResponseEntity<String> response = controller.processShpCerRequest(request);
        
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());
    }

    @Test
    void testProcessKongCerRequest_Success() {
        // Prepare success response
        Map<String, Object> expectedResponse = new LinkedHashMap<>();
        expectedResponse.put("message", "Kong CER data saved successfully");
        
        ResponseDto<Map<String, Object>> responseDto = new ResponseDto<>();
        responseDto.setStatusCode(HttpStatus.OK);
        responseDto.setMessage("Kong CER data saved successfully");
        
        Mockito.when(hapCERService.processKongCerRequest(kongRequest)).thenReturn(responseDto);
        
        // Mock empty binding result (no errors)
        BindingResult bindingResult = Mockito.mock(BindingResult.class);
        Mockito.when(bindingResult.hasErrors()).thenReturn(false);
        
        ResponseEntity<Map<String, Object>> response = 
            controller.processKongCerRequest(kongRequest, bindingResult);
        
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());
    }

    @Test
    void testProcessKongCerRequest_ValidationError() {
        // Prepare error response
        Map<String, Object> expectedResponse = new LinkedHashMap<>();
        expectedResponse.put("message", "field1: error message1, field2: error message2");
        
        // Mock binding result with errors
        BindingResult bindingResult = Mockito.mock(BindingResult.class);
        Mockito.when(bindingResult.hasErrors()).thenReturn(true);
        Mockito.when(bindingResult.getFieldErrors()).thenReturn(Arrays.asList(
            new FieldError("object", "field1", "error message1"),
            new FieldError("object", "field2", "error message2")
        ));
        
        ResponseEntity<Map<String, Object>> response = 
            controller.processKongCerRequest(kongRequest, bindingResult);
        
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());
    }
            }

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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class CERControllerTest {

    @InjectMocks
    private CERController cerController;

    @Mock
    private HapCerService hapCerService;

    @Mock
    private BindingResult bindingResult;

    private KongCerRequest request;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        request = new KongCerRequest();
        // Set up any required request fields here
    }

    @Test
    void testProcessKongCerRequest_Success() {
        // Mock successful service response
        ResponseDto<Map<String, Object>> mockResponseDto = new ResponseDto<>();
        mockResponseDto.setStatusCode(HttpStatus.OK.value());
        mockResponseDto.setMessage("CER data processed successfully");
        
        Map<String, Object> expectedResponse = new LinkedHashMap<>();
        expectedResponse.put("message", "CER data processed successfully");

        when(hapCerService.processKongCerRequest(request)).thenReturn(mockResponseDto);
        when(bindingResult.hasErrors()).thenReturn(false);

        // Call the controller method
        ResponseEntity<Map<String, Object>> response = 
            cerController.processKongCerRequest(request, bindingResult);

        // Assertions
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());
    }

    @Test
    void testProcessKongCerRequest_ValidationErrors() {
        // Mock validation errors
        FieldError fieldError1 = new FieldError("kongCerRequest", "field1", "Field1 is required");
        FieldError fieldError2 = new FieldError("kongCerRequest", "field2", "Field2 must be valid");
        List<FieldError> fieldErrors = Arrays.asList(fieldError1, fieldError2);
        
        when(bindingResult.hasErrors()).thenReturn(true);
        when(bindingResult.getFieldErrors()).thenReturn(fieldErrors);

        // Call the controller method
        ResponseEntity<Map<String, Object>> response = 
            cerController.processKongCerRequest(request, bindingResult);

        // Assertions
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        
        Map<String, Object> responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals("field1 : Field1 is required, field2 : Field2 must be valid", 
                     responseBody.get("message"));
    }

    @Test
    void testProcessKongCerRequest_ServiceError() {
        // Mock service error response
        ResponseDto<Map<String, Object>> mockResponseDto = new ResponseDto<>();
        mockResponseDto.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        mockResponseDto.setMessage("Internal server error occurred");
        
        when(hapCerService.processKongCerRequest(request)).thenReturn(mockResponseDto);
        when(bindingResult.hasErrors()).thenReturn(false);

        // Call the controller method
        ResponseEntity<Map<String, Object>> response = 
            cerController.processKongCerRequest(request, bindingResult);

        // Assertions
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Internal server error occurred", 
                     response.getBody().get("message"));
    }
             }

@TestPropertySource(locations = { "classpath:application-test.properties" })
@ExtendWith(SpringExtension.class)
public class CERControllerTest {

    @InjectMocks
    private CERController cerController;

    @Mock
    private HapCERService hapCERService;

    @Mock
    private BindingResult bindingResult;

    @Mock
    private Logger logger;

    private KongCerRequest kongRequest;

    @BeforeEach
    void setUp() {
        kongRequest = new KongCerRequest();
        kongRequest.setRequiredField("valid_value"); // Set minimum required valid data
        ReflectionTestUtils.setField(cerController, "LOGGER", logger);
    }

    // Validation Failure Test
    @Test
    void processKongCerRequest_ShouldReturnBadRequest_WhenValidationFails() {
        // Arrange
        when(bindingResult.hasErrors()).thenReturn(true);
        List<FieldError> fieldErrors = List.of(
            new FieldError("kongCerRequest", "requiredField", "must not be blank"),
            new FieldError("kongCerRequest", "numericField", "must be a positive number")
        );
        when(bindingResult.getFieldErrors()).thenReturn(fieldErrors);

        // Act
        ResponseEntity<Map<String, Object>> response = 
            cerController.processKongCerRequest(kongRequest, bindingResult);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(
            "requiredField: must not be blank, numericField: must be a positive number",
            response.getBody().get("message")
        );
        verifyNoInteractions(hapCERService);
    }

    // Success Path Test
    @Test
    void processKongCerRequest_ShouldReturnSuccess_WhenRequestIsValid() {
        // Arrange
        when(bindingResult.hasErrors()).thenReturn(false);
        ResponseDto<Map<String, Object>> successResponse = new ResponseDto<>();
        successResponse.setStatusCode(HttpStatus.CREATED.value());
        successResponse.setMessage("Data processed successfully");
        when(hapCERService.processKongCerRequest(any())).thenReturn(successResponse);

        // Act
        ResponseEntity<Map<String, Object>> response = 
            cerController.processKongCerRequest(kongRequest, bindingResult);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Data processed successfully", response.getBody().get("message"));
        verify(hapCERService).processKongCerRequest(kongRequest);
    }

    // Service Exception Test
    @Test
    void processKongCerRequest_ShouldReturnInternalError_WhenServiceFails() {
        // Arrange
        when(bindingResult.hasErrors()).thenReturn(false);
        when(hapCERService.processKongCerRequest(any()))
            .thenThrow(new RuntimeException("Service unavailable"));

        // Act
        ResponseEntity<Map<String, Object>> response = 
            cerController.processKongCerRequest(kongRequest, bindingResult);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(((String)response.getBody().get("message")).contains("Error processing request"));
    }
}

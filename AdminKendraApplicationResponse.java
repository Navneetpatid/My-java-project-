@SpringBootTest
@AutoConfigureMockMvc
class CERControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HapCerService hapCerService;

    @Test
    void testGetCerEngagementData() throws Exception {
        String engagementId = "12345";
        String workspace = "dev";

        CerGetResponse mockResponse = new CerGetResponse();
        // Add mock data to mockResponse as needed

        when(hapCerService.getCerEngagementData(eq(engagementId), eq(workspace)))
                .thenReturn(mockResponse);

        mockMvc.perform(get("/data")
                        .param("engagementId", engagementId)
                        .param("workspace", workspace)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                // Add more .andExpect(...) depending on mockResponse fields
                ;
    }
}

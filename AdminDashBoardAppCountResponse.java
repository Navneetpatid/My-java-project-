@Test
void testGetCerEngagementData() {
    // 1. Prepare test data
    String engagementId = "engagement123";
    String workspace = "workspace1";
    
    // 2. Create mock response data
    List<Map<String, Object>> mockResponseList = new ArrayList<>();
    Map<String, Object> data1 = new LinkedHashMap<>();
    data1.put("key1", "value1");
    data1.put("key2", 123);
    mockResponseList.add(data1);
    
    // 3. Mock the service call
    Mockito.when(hapCerService.getCerEngagementData(engagementId, workspace))
           .thenReturn(mockResponseList);
    
    // 4. Call the controller method
    ResponseEntity<List<Map<String, Object>>> response = 
        controller.getCerEngagementData(engagementId, workspace);
    
    // 5. Assertions
    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(mockResponseList, response.getBody());
    
    // Optional: Verify logging was called
    // (Add if you have Mockito.verify() for your logger)
}

@Test
void testProcessKongCerRequest_InvalidRequest() {
    KongCerRequest invalidRequest = new KongCerRequest(); // Missing required field(s)

    BindingResult result = new BeanPropertyBindingResult(invalidRequest, "KongCerRequest");

    // Replace 'fieldName' with actual property name that is required
    result.rejectValue("requiredFieldName", "NotNull", "must not be null");

    ResponseEntity<Map<String, Object>> response = controller.processKongCerRequest(invalidRequest, result);

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
}

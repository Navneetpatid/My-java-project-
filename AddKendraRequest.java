public ResponseEntity<Map<String, Object>> processKongCerRequest(...) {
    // Check for validation errors
    if (result.hasErrors()) {
        Map<String, Object> response = new HashMap<>();
        response.put("statusCode", HttpStatus.BAD_REQUEST.value());
        response.put("message", "Validation failed");

        return ResponseEntity.badRequest().body(response);
    }

    // Process request if no validation errors
    ResponseDto<Map<String, Object>> responseDto = hapCdrService.processKongCerRequest(request);

    Map<String, Object> response = new HashMap<>();
    response.put("statusCode", responseDto.getStatusCode());
    response.put("message", responseDto.getMessage());

    return ResponseEntity.status(responseDto.getStatusCode()).body(response);
}

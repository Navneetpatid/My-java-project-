public ResponseEntity<Map<String, Object>> processKongCerRequest(@Valid @RequestBody YourRequestDto request, BindingResult result) {
    // Check for validation errors
    if (result.hasErrors()) {
        String errorMessage = result.getFieldErrors()
                                    .stream()
                                    .map(error -> error.getField() + ": " + error.getDefaultMessage())
                                    .collect(Collectors.joining(", ")); // Collect all validation messages

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("statusCode", HttpStatus.BAD_REQUEST.value());
        response.put("message", errorMessage);

        return ResponseEntity.badRequest().body(response);
    }

    // Process request if no validation errors
    ResponseDto<Map<String, Object>> responseDto = hapCdrService.processKongCerRequest(request);

    Map<String, Object> response = new LinkedHashMap<>();
    response.put("statusCode", responseDto.getStatusCode());
    response.put("message", responseDto.getMessage());

    return ResponseEntity.status(responseDto.getStatusCode()).body(response);
}

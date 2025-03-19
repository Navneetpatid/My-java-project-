@PostMapping(value = "/add/kong/data", consumes = "application/json", produces = "application/json")
public ResponseEntity<ResponseDto<Map<String, Object>>> processKongCerRequest(
        @Valid @RequestBody KongCerRequest request, BindingResult result) {

    LOGGER.info("Received request to add CER Data");

    // Check for validation errors
    if (result.hasErrors()) {
        Map<String, Object> errors = new HashMap<>();
        result.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

        return ResponseEntity.badRequest().body(new ResponseDto<>(
                HttpStatus.BAD_REQUEST.value(),
                "Validation failed",
                errors
        ));
    }

    // Process request if no validation errors
    ResponseDto<Map<String, Object>> response = hapCdrService.processKongCerRequest(request);
    return ResponseEntity.status(response.getStatusCode()).body(response);
}

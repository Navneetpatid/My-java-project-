@PostMapping(value = "/add/kong/data", consumes = "application/json", produces = "application/json")
public ResponseEntity<ResponseDto<Map<String, String>>> processKongCerRequest(
        @Valid @RequestBody KongCerRequest request, BindingResult result) {

    LOGGER.info("Received request to add CER Data");

    // Check if there are validation errors
    if (result.hasErrors()) {
        Map<String, String> errors = new HashMap<>();
        result.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

        return ResponseEntity.badRequest().body(new ResponseDto<>(
                HttpStatus.BAD_REQUEST.value(),
                "Validation failed",
                errors
        ));
    }

    // Process request
    ResponseDto<Map<String, Object>> response = hapCDRService.processKongCerRequest(request);

    return ResponseEntity.status(response.getStatusCode()).body(response);
}

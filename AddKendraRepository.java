@PostMapping(value = "/add/kong/data", consumes = "application/json", produces = "application/json")
public ResponseEntity<Map<String, Object>> processKongCerRequest(@Valid @RequestBody KongCerRequest request, BindingResult result) {
    LOGGER.info("------ Adding CER Data ------");

    // Handle validation errors
    if (result.hasErrors()) {
        return new ResponseEntity<>(bindingValidationUtil.requestValidation(result), HttpStatus.BAD_REQUEST);
    }

    // Process request
    Map<String, Object> response = hapCdrService.processKongCerRequest(request);
    LOGGER.info("------ CER Data Added ------");

    return new ResponseEntity<>(response, HttpStatus.OK);
}

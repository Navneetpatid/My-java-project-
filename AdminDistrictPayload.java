@GetMapping(value = "/validate", 
           consumes = MediaType.APPLICATION_JSON_VALUE, 
           produces = MediaType.APPLICATION_JSON_VALUE)
public ResponseEntity<WorkspaceValidationResponse> validateWorkspace(
    @RequestParam String engagementId,
    @RequestParam String workspace) {
    
    LOGGER.info("Validating workspace for Engagement ID: {} and Workspace: {}", engagementId, workspace);
    WorkspaceValidationResponse response = hapCerService.validateWorkspace(engagementId, workspace);
    LOGGER.info("Validation completed for Engagement ID: {}", engagementId);
    return ResponseEntity.ok(response);
}

// Add this record/class somewhere in your code
public record WorkspaceValidationResponse(
    boolean isValid,
    String message,
    Instant timestamp
) {}

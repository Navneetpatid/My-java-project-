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

// Add this record/class somewhere in your codeDear [Client's Name],

I hope this message finds you well.

I would like to inform you that I am currently on leave on Monday, 21st April 2025 and Tuesday, 22nd April 2025, as my parent company is conducting my onboarding and induction process across these two days. This is a full-day program, and my participation is mandatory.

I ensured to manage and delegate any urgent tasks in advance to avoid disruption during my absence. I will resume regular work from Wednesday, 23rd April 2025.

Thank you for your understanding and support.

Best regards,
[Your Full Name]
[Your Designation]
[Your Contact Information]
public record WorkspaceValidationResponse(
    boolean isValid,
    String message,
    Instant timestamp
) {}

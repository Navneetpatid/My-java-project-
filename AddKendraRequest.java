@Override
public ValidationResponseDTO validateEngagement(String engagementId, String workspace) {
    ValidationResponseDTO response = new ValidationResponseDTO();
    StringBuilder logs = new StringBuilder();
    
    logs.append("Engagement ID validated | ");

    // **Step 1: Fetch all workspaces for the Engagement ID**
    List<String> workspaces = workspaceTargetDetailsDao.findByEngagementId(engagementId)
            .stream()
            .map(details -> details.getId().getWorkspace()) // Extract workspace from EmbeddedId
            .collect(Collectors.toList());

    if (workspaces.isEmpty()) {
        logs.append("No workspaces found for this Engagement ID | ");
        response.setSuccess(false);
        response.setErrors("No workspaces found for this Engagement ID");
        response.setLogs(logs.toString());
        return response;
    }

    response.setWorkspace(workspaces);
    logs.append("Workspaces retrieved | ");

    // **Step 2: Validate the given workspace using EmbeddedId**
    WorkspaceTargetId workspaceTargetId = new WorkspaceTargetId(engagementId, workspace);
    Optional<WorkspaceTargetDetails> workspaceOpt = workspaceTargetDetailsDao.findById(workspaceTargetId);

    if (workspaceOpt.isPresent()) {
        logs.append("Workspace validated successfully | ");
        response.setSuccess(true);
    } else {
        logs.append("Invalid workspace provided | ");
        response.setSuccess(false);
        response.setErrors("Invalid workspace for the given Engagement ID");
    }

    response.setLogs(logs.toString());
    return response;
}

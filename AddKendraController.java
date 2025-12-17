EGR0206

Cause: Mule HAP template build failed due to compilation errors or missing dependencies in the template.

Resolution: Review the build error logs, fix dependency or configuration issues in the HAP template, and rerun the pipeline.



---

EGR0207

Cause: Pipeline failed to update the XML file due to invalid XML content or insufficient permissions in the HAP template.

Resolution: Validate the XML syntax and file path, ensure write access in the HAP template, and retry the pipeline.
    

EGR0322

Cause: apiPlatformGW definition in CloudHub is missing mandatory attributes for the Mule application.

Resolution: Update the apiPlatformGW configuration with all required attributes and retry the deployment.

EGR0605

Cause: Pipeline failed to store the Deploy Control Object due to an internal error or connectivity issue with DEPLOY CONTROL service.

Resolution: Verify DEPLOY CONTROL service availability and credentials, then rerun the pipeline.



---

EGR0604

Cause: Application failed to store the Deploy Control Object in the central deployment register due to validation or service error.

Resolution: Check the central deployment register service status and error details, then retry the deployment.

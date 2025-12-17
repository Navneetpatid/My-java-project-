EGR0100

Cause: HAP Engagement ID was not passed as an input parameter.

Resolution: Provide a valid HAP Engagement ID (folder name) and rerun the pipeline.



---

EGR0099

Cause: deployUtility is not configured or unsupported (neither helm nor kubectl).

Resolution: Verify onboarding details and configure a supported deploy utility.



---

EGR0100 (Configuration YAML)

Cause: Configuration YAML is not available in Nexus prod group or path is incorrect.

Resolution: Upload the YAML to Nexus prod group and pass the correct configurationYamlPath.



---

EGR0101

Cause: DEPLOYMENT_IMAGE (artifactId:versionTag) is missing for a governed environment.

Resolution: Provide the DEPLOYMENT_IMAGE parameter in the job configuration.



---

EGR0102

Cause: Source code GIT branch parameter is not provided.

Resolution: Specify a valid GIT branch name before triggering the pipeline.



---

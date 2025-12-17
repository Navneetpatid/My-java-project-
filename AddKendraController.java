EGR0202

Cause: Approved deployed version validation failed due to missing or inconsistent deployment metadata.

Resolution: Verify the deployed version details and contact HAP support if the issue persists.



---

EGR0203

Cause: Pipeline failed to update the properties file due to incorrect file path or insufficient write permissions in HAP template.

Resolution: Ensure the properties file exists in the HAP template and the pipeline has write access, then retry.



---

EGR0204

Cause: YAML configuration file update failed due to invalid YAML content or incorrect file location in HAP template.

Resolution: Validate the YAML syntax and file path in the HAP template and rerun the pipeline.



---

EGR0205

Cause: Required pom.xml file is missing in the HAP template for build or deploy action.

Resolution: Add the correct pom.xml to the HAP template or provide Nexus GAV details in the pipeline input.
    

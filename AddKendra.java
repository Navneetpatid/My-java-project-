
EGR0309
Cause: VALUES_YAML_NEXUS_PATH is not provided in job parameters.
Resolution: Pass a valid VALUES_YAML_NEXUS_PATH value.
EGR0310
Cause: Invalid Nexus region value is passed.
Resolution: Set NEXUS_REGION as HK or UK where the image exists.
EGR0311
Cause: Nexus region parameter is missing.
Resolution: Provide NEXUS_REGION (HK or UK) in job parameters.
EGR0312
Cause: values-<targetEnvironment>.yaml failed validation.
Resolution: Fix syntax/mandatory fields and revalidate the YAML file.
EGR0313
Cause: Workspace value is empty in configuration YAML.
Resolution: Add a valid workspace value in YAML.
EGR0316
Cause: Unsupported or wrong action value in YAML.
Resolution: Use a valid action as per supported configuration.
EGR0317
Cause: deployToDmz value is not defined in YAML.
Resolution: Add deployToDmz with true/false value.
EGR0318
Cause: Invalid deployToDmz value format.
Resolution: Set deployToDmz as true or false only.
EGR0320
Cause: SNOW change number is missing for Pre-Prod/Prod.
Resolution: Provide a valid SNOW Change Record number.
EGR0801
Cause: Provided Nexus artifact is invalid or not found.
Resolution: Verify artifact name, group, version, and repository.
EGR0802
Cause: SNAPSHOT version used for higher environments.
Resolution: Use a RELEASE version for Pre-Prod/Prod deployment.

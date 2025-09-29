Deployment Pipeline JSON Documentation
1. configuration
Contains deployment configuration details.
Field
Description
hapEngagementID
Unique engagement ID for HAP deployment.
emailDistributionList
Email address for deployment notifications.
workspace
Target workspace for deployment.
deployToDmz
Indicates DMZ deployment setting (enable/disable).
oasFilePath
Path to OAS YAML definition file.
action
Action to be performed (e.g., deploy).
plugins
List of plugins with configuration details.

2. pipelineData
Holds metadata related to deployment pipeline execution.
Field
Description
deployControlSummery
Validation summary of the Change Request (CR).
id
Unique identifier for the pipeline execution.
component
Component being deployed (e.g., kong).
purl
Package URL for the deployment artifact.
rollbackArtifactInstance
Reference to rollback artifact instance.
config
Configuration package reference URL.
eimIds
List of associated EIM IDs.
version
Version of the deployed artifact.
tags
Tags associated with the pipeline execution.

3. jobParam
Parameters used by the Jenkins pipeline job.
Field
Description
pipelineGITBranch
Branch of the pipeline repository.
targetEnvironment
Target environment (e.g., PRE-PROD).
configurationYMLPath
Path to pipeline configuration YML.
snowCR
Associated ServiceNow Change Request ID.
verboseLogging
Indicates verbose logging is enabled or not.

4. job
Details about Jenkins job execution.
Field
Description
buildNumber
Jenkins build number.
buildUser
User who triggered the build.
buildURL
Jenkins job URL.
error
Error details if job failed.
buildEnd
Timestamp when build ended.
buildStatus
Final status of the build (e.g., Failed/Success).
buildStart
Timestamp when build started.

5. centralEnvironmentRegister
Validation details for deployment in target environment.
Field
Description
success
Indicates if environment validation succeeded.
errors
List of errors encountered.
workspace
Validated workspace identifier.
cp_admin_api_url
Control plane admin API URL.
mandatoryPlugins
List of mandatory plugins validated.
dp_host_url
Data plane host URL.
gbgf
Business group identifier.
dmz_Lb
List of DMZ Load Balancers used.


    

Objective:
To safely restore ITADS services to the previous stable state in case the deployment does not function as expected during or after implementation.
Trigger Condition:
Backout will be initiated if any of the following occur:
Deployment failure
Jenkins pipeline execution failure
ITADS services are unavailable or unstable
Regression tests fail
Critical consumer impact observed
Fix-Forward Decision Point:
If deployment issues are identified before completion of Jenkins pipeline and validation, backout will be executed immediately.
Once ARP is successfully uploaded and regression validation passes, backout will no longer be required, and fix-forward will be considered if minor issues arise.
Rollback Steps (Step-by-Step):
Task 1: Rollback Release for ITADS
Identify deployment failure or unexpected behavior.
Checkout previous stable release:
Git Repo:
https://alm-github.systems.uk.hsbc/HAP/MuleSoft-Jenkins-Pipeline-v2.0.git
Branch: Release-3.2.9
Re-trigger Jenkins pipelines using the previous release branch.
Task 2: Execute Jenkins Pipelines
Run the following pipelines to restore services:
HAP_Kong
HAP_Service_Hosting
HAP_Pipeline_v2
Task 3: Verification After Backout
Confirm ITADS services are reachable.
Validate Jenkins job execution status as SUCCESS.
Ensure consumers are able to access services normally.
Worst-Case Impact if Backout Fails:
Temporary service unavailability for ITADS consumers.
Reduced redundancy until services are restored.
Backout Testing Confirmation:
Rollback procedure has been tested and validated in lower environments.

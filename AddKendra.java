Define the G3 workflow process for application release management.
Include steps for creating Package Work Item (PWI).
Include configuration setup using Release Configuration Work Item (RCWI).
Define the process for creating Release Work Item (RWI).
Describe how packages are deployed to the target environments (Dev/UAT/Prod).
Ensure proper linkage between PWI, RWI, and RCWI in the workflow.


  How G3 Automation Works
Artifact Creation (Manual + CI Automation)
Application build generates artifacts automatically via CI tools.
Artifacts are stored in a repository (e.g., Nexus).
Package Work Item (PWI) Creation (Manual)
User selects artifacts and creates the package.
Release Configuration Work Item (RCWI) Setup (Manual – One Time)
Define deployment steps, scripts, and target servers.
Configure environments like Dev, UAT, or Prod.
Release Work Item (RWI) Execution (Automated Deployment)
Once RWI is triggered, the system automatically:
Fetches packages (PWI)
Uses deployment steps from RCWI
Deploys to target environments.
Deployment & Logging (Automated)
G3 runs deployment steps automatically.
Logs and status are generated for monitoring.

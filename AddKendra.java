1. Important Update
G3-Ansible is now the standard deployment solution.
RepliWeb is deprecated and no longer supported.
All new setups should follow G3-Ansible flow.
🔹 2. G3 Access Setup
Raise ServiceNow request (G3 – ADLDS Access).
Without access, you cannot create or manage projects.
Use “Gain/Remove Access” page if access issues occur.
🔹 3. Create New G3 Project
Project creation is self-service.
Requires EIM Application Instance ID / Name.
Important points:
Only one project per EIM allowed.
Requestor should be Project Owner.
Otherwise, visibility issues may happen.
🔹 4. Manage User Access
Add/remove users in the project.
Define roles and permissions.
Ensures team members can work on deployment.
🔹 5. Create New G3 Application
While creating application:
Select “Add New Application”.
Choose correct purpose:
Example: DB Script Deployment (for database changes).
This defines how deployment will be executed.
🔹 6. Target Server Onboarding
Register Linux/Windows servers in G3.
Required only if:
You are deploying scripts or applications to servers.
Not required for:
Pure SQL script deployments.
🔹 7. Database Schema Onboarding
Pre-requisites:
Database must already be onboarded in DBAi.
Application must be marked for DB Script Deployment.
Then:
Register DB schema in G3.
Without DBAi onboarding, schema won’t be visible.
🔹 8. Modify Server / DB Config
Limited fields can be edited after setup.
Use “Manage Target Servers and DB Schemas”.
🔹 9. DMZ / IDMZ / eDMZ Servers
Extra steps required for these zones.
Follow G3-Ansible additional configuration.
🔹 10. SSH Key Setup
Required for secure connection to servers.
Setup needed for:
Linux servers
Windows servers (if applicable)
Enables automated deployments.
🔹 11. Health Check
Validate:
Server connectivity
Database connectivity
Must pass before deployment.
🔹 12. Release Configuration (RCWI)
Auto-created when application is created.
Used to define:
Deployment steps
Configuration details
Key points:
Can be modified as per requirement.
Every change creates a new RCWI version.
Required during deployment scheduling.
🔹 13. Package Work Item (PWI)
Represents artifact/package to deploy.
Example:
JAR, WAR, SQL scripts, etc.
Stored in Nexus Repository.
Important:
Artifact path must be correct.
Artifact name must match RCWI config.
Duplicate PWIs avoided using version/name.
🔹 14. Release Work Item (RWI)
Final step for deployment.
Used to:
Schedule deployment
Execute release
Requires:
RCWI reference
PWI (artifact)
This is the actual execution layer.
✅ Overall Flow (Simple Understanding)
Take Access
Create Project
Add Users
Create Application
Setup Server / DB
Configure SSH & Health Check
Configure RCWI
Create PWI (artifact from Nexus)
Create RWI → Deploy 🚀

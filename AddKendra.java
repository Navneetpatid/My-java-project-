🚀 1. Self-Release Phase
Description: Initial deployment by developers using automated pipeline.
Steps:
Changes are developed in feature branch
Code is merged via self-service release pipeline
Deployment executed automatically
Basic testing is performed after release
🧪 2. G3 Release (UAT Phase)
Description: Controlled release for testing and validation.
Steps:
Create RWI (Release Work Item) for:
Deployment
Rollback
Use existing G3 RWI for rollback scenario
Perform Happy Path Testing
Document all package details (components, versions)
🏗️ 3. Production Planning
Description: Preparation before production release.
Steps:
Create Release Page in Confluence
Raise Change Request (CR) using:
Same package ID used in UAT
Ensure:
All packages are tested in G3
Perform Post-validation checks
📝 4. CR Creation Steps
Description: Steps to create Change Request.
Steps:
Trigger Release Note Pipeline
Collect package details
Create CR in system
Add:
Deployment plan
Rollback plan
Impact details
Link CR with RWI & Release Page
🔁 Rollback Strategy
Use previously created G3 RWI
Ensure rollback steps are documented
Validate rollback in UAT before production
✅ Acceptance Criteria
All deployments follow defined process
RWI created for both deploy & rollback
CR includes complete details
Packages tested in G3 before production
Post-validation completed successfully

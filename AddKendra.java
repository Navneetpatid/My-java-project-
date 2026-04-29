ITADS Unified Release Process Guide

Purpose

This document combines both provided release process documents into one clear end-to-end guide covering Self Release, G3 (UAT), Production Release, Global Release, and CR creation.


---

Section 1: Release Lifecycle Overview

Phase 1: Feature Development

1. Create feature branch as per release version.

Example: If release version is 3.3.0, branch name should be feature-3.3.0.



2. Perform code enhancements / bug fixes in feature branch.


3. Complete unit testing and peer review.


4. Merge code using approved process.




---

Phase 2: Self Release Phase

Description: Initial deployment by developers using automated pipeline.

Steps

1. Changes are developed in feature branch.


2. Code is merged via self-service release pipeline.


3. Automated deployment runs through Jenkins pipeline.


4. Validate deployment success.


5. Perform basic smoke testing.


6. Fix issues if found and redeploy.




---

Phase 3: G3 Release (UAT Phase)

Description: Controlled release for testing and validation.

Steps

1. Create RWI (Release Work Item) for:

Deployment n   - Rollback



2. Trigger G3 release.


3. System automatically:

Fetches packages (PWI)

Uses deployment steps from RWI

Deploys to target UAT environment



4. Execute UAT testing:

End-to-End ARP UAT run pointing to feature branch

Manual Happy Path testing



5. Collect evidences/screenshots/logs.


6. Prepare testing document.


7. Use existing G3 RWI if rollback is needed.




---

Phase 4: Production Planning

Pre-Production Checklist

1. Confirm UAT signoff.


2. Freeze release scope.


3. Prepare rollback plan.


4. Confirm approvals from stakeholders.


5. Validate package versions.


6. Schedule deployment window.




---

Phase 5: CR (Change Request) Creation

Steps

1. Create CR using OMNI tool.


2. Use an existing CR as template if required.


3. Generate YAML build template.


4. Update required details:

Location: Asia/Kolkata

Start date / End date

Application details

Deployment steps

Rollback steps



5. Submit CR for approvals.


6. Link below items in CR:

Jira Tasks n   - ICE links

UAT evidence links

Release notes



7. Obtain all approvals before PROD deployment.




---

Phase 6: Production Release

Steps

1. Start deployment during approved window.


2. Deploy release branch/package to PROD.


3. Monitor logs and health checks.


4. Perform PROD validation:

End-to-End ARP PROD run pointing to release branch

Manual Happy Path testing



5. Collect production evidence.


6. Update stakeholders on status.




---

Phase 7: Global Release / Finalization

Steps

1. After successful PROD validation, perform Global Release.


2. Update release branch in all pipelines:

Mule n   - KONG

SHP



3. Confirm all dependent services updated.


4. Collect final evidences.


5. Close CR / RWI.


6. Publish release summary.




---

Rollback Process

1. If production issue occurs, assess severity.


2. Trigger approved rollback RWI / CR.


3. Restore previous stable version.


4. Revalidate services.


5. Share incident summary.




---

Deliverables Checklist

Code merged successfully

Self release completed

UAT passed

CR approved

PROD deployed

PROD testing passed

Global release completed

Evidence document stored

Change closed



---

Quick Flow Summary

Feature Branch -> Self Release -> G3/UAT -> CR Creation -> PROD Release -> PROD Testing -> Global Release -> Closure

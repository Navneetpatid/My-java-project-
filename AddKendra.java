Acceptance Criteria – Phase 5
Deployment Service retains deployment history for a minimum of 2 years.
Deployment activity includes Change Task in activity payload reported to Deploy Control.
Deployment Service executes only Change Tasks and does not use the Release tool field.
Deployment is allowed only when CHG task state is Open, Pending, or In Progress.
Phase 5 configuration changes are implemented and validated successfully.
This change is being implemented as part of Release 3.3.1 to improve system stability, compliance, and production readiness of ITADS. The enhancements to error handling for Control 4.20 and 4.21 strengthen monitoring, logging, and failure management, ensuring better operational resilience and adherence to deploy control standards. Additionally, the addition of a Production GCP node is required to support increased workload, improve scalability, and ensure high availability in the production environment. These changes collectively help in delivering a more reliable, compliant, and robust production deployment.

Acceptance Criteria:
Deployment Service Release 3.3.1 is successfully deployed to the PROD environment within the approved change window.
Enhanced monitoring and logging are enabled and verified for the Deployment Service.
Error handling improvements are implemented and validated, with failures properly captured in logs.
System stability is verified post-deployment with no critical or high-severity incidents observed.
All deployment pipelines (HAP / ITADS related pipelines) execute successfully without errors.
Service availability is maintained, and no downtime or customer impact is reported.
Application complies with deployment control standards and compliance requirements.
Post-deployment sanity testing and verification are completed successfully.
Rollback plan is available and tested (if required), though not executed.
Evidence of successful deployment and testing is collected and attached to the change request.

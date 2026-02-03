Acceptance Criteria – Phase 5
Deployment Service retains deployment history for a minimum of 2 years.
Deployment activity includes Change Task in activity payload reported to Deploy Control.
Deployment Service executes only Change Tasks and does not use the Release tool field.
Deployment is allowed only when CHG task state is Open, Pending, or In Progress.
Phase 5 configuration changes are implemented and validated successfully.
This change is being implemented as part of Release 3.3.1 to improve system stability, compliance, and production readiness of ITADS. The enhancements to error handling for Control 4.20 and 4.21 strengthen monitoring, logging, and failure management, ensuring better operational resilience and adherence to deploy control standards. Additionally, the addition of a Production GCP node is required to support increased workload, improve scalability, and ensure high availability in the production environment. These changes collectively help in delivering a more reliable, compliant, and robust production deployment.

STEP-4: Rollback to Previous Version (Release-3.3.1)
In case of deployment failure, rollback will be performed using G3 Deployment Service.
A 5-minute fix-forward window will be followed; if unresolved or critical, RWI (Automated Deployment) will be triggered. G3 will automatically:
Fetch packages (PWI)
Use deployment steps from RCWI
Deploy to target environments
Rollback will complete in <5 minutes.
Post rollback, validation will be done (application access, API/services check, Jenkins status, logs monitoring) to confirm stability.
Minimal impact expected (brief degradation possible, no data loss). Any outage will be recorded and stakeholders will be informed.

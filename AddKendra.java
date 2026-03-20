Summary of Issue and Resolution
Issues Identified:
ENG ID: HAP-CTO-36516
Jenkins pipeline failed due to shell command error.
Root cause: Splunk configuration was commented in values file, so deployment could not read required values.
ENG ID: HAP-CTO-37752
MI event was not getting pushed to Splunk.
ENG ID: HAP-CTO-1002
Required configuration missing in Prod pipeline (clusters and namespace not added).
KONG DNS was down, causing KONG-related URLs to be inaccessible.
Resolution / Actions Taken:
Uncommented and corrected Splunk configuration in values file.
Verified MI event flow to Splunk after fix.
Added required clusters (uk-aza-1103, uk-azb-1103) and namespace (eos-hosting-platform-uk) in Prod pipeline as per onboarding.
Waited for KONG DNS auto-recovery and verified services post restoration.
Current Status:
All ENG issues resolved successfully.
KONG DNS is up and stable.
Pipeline execution and Splunk integration are working as expected.

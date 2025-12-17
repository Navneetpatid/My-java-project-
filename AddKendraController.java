EGR1006

Cause: SAST scan failed because configurationID is not defined in the pipeline configuration.
Resolution: Add a valid configurationID in the SAST scan configuration and rerun the pipeline.


---

EGR1007

Cause: SAST scan failed because projectID is missing or not mapped to the repository.
Resolution: Configure the correct projectID in the SAST tool and update the pipeline.


---

EGR1008

Cause: SAST scan failed due to missing CyberFlow credentials in the CI/CD environment.
Resolution: Configure valid CyberFlow credentials in the pipeline and re-execute the scan.


---

EGR1009

Cause: AutoICE update failed because customer approval parameter is missing or invalid.
Resolution: Provide a valid autoICEObjCustomerApproval value before triggering AutoICE.


---

EGR1010

Cause: ICE processing failed due to missing mandatory configuration or inputs.
Resolution: Validate all required ICE parameters and artifacts, then rerun the pipeline.


---
  

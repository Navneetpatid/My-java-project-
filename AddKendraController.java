EGR0202

Cause: Deployed version does not match the approved version in HAP.

Resolution: Verify the approved version in HAP and redeploy the correct build.



---

EGR0204

Cause: Nexus repository is unreachable or artifact URL/credentials are invalid.

Resolution: Check Nexus URL, credentials, network connectivity, and artifact availability.



---

EGR0205

Cause: Downloaded ZIP file is corrupted or incomplete.

Resolution: Re-download the artifact from Nexus and ensure ZIP integrity.



---

EGR0206

Cause: Checksum file is missing or Nexus access failed.

Resolution: Ensure checksum exists in Nexus and verify repository access permissions.



---

EGR0203

Cause: Provided hapEngagementID does not exist or is incorrect.

Resolution: Pass a valid HAP Engagement ID in the job parameters.



---

EGR0101

Cause: Required job parameter CONSUMER_TYPE is not provided.

Resolution: Pass CONSUMER_TYPE in the job parameter before execution.
  

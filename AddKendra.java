Mistake & Solution Points (Final CR Release Guideline Format)

1) Mistake: CR is not created in advance, or CR is reverted frequently for feature usage — causing emergency delays and lead-time issues.

Solution: Always create and submit the CR early (in advance) with proper planning. Keep CR stable by reserving features in advance so no last-minute emergency CR is needed. This avoids delays and gives enough time for review.


---

2) Mistake: Important CR checklist points are not reviewed or rechecked — leading to missed parameters, wrong values, or configuration mistakes.

Solution: Recheck the full CR checklist before submission. Verify all parameters, values, approvals, attachments, and test evidence. A final self-review avoids common mistakes and ensures CR accuracy.


---

3) Mistake: Pipelines mentioned in the CR description are wrong or pointing to the incorrect environment (pre-prod pipeline added for prod CR or vice-versa).

Solution: Confirm that every pipeline linked in the CR matches the correct environment. Example: Pre-prod CR → pre-prod pipeline, Prod CR → prod pipeline. Cross-check pipeline names and URLs before submitting.


---

4) Mistake: Dry-run of pipelines is not performed, so package validation fails during the real release task.

Solution: Always run a full dry-run of each pipeline before adding it to the CR. Ensure the package builds successfully in the dry-run. This confirms that the release package is correct and ready.


---

5) Mistake: CR approval is requested late (same day or last minute), causing delays and difficulty reverting if issues appear.

Solution: Complete all tasks early and take approval at least one day before the release. Early approval provides buffer time to fix or revert issues without impact.


---

6) Mistake: CR is not closed on time after completion, creating unnecessary risk or leaving tasks pending in the system.

Solution: Once all stages, validations, and tests are completed, close the CR immediately. Timely closure prevents risk escalation and keeps the release queue clean.
7) Release Approval Email Not Sent Before/After Release (NEW)

Mistake: Approval email for the release is not sent before starting the release, and completion email is also missed.
Solution:

Before release: Send the approval email for the current release and take approval from the appropriate approver/person.

After release: Send a “Release Success” email with completed tasks, test results, and all supporting documents attached.
  8) ICE & CR Confluence Page Not Completed Properly (NEW)

Mistake:
ICE (Implementation Complexity Evaluation) and CR Confluence documentation is not completed, or the ICE score is below the expected threshold.

Solution:
Make sure the ICE and CR Confluence page is fully completed with all required details, and ensure the ICE score is above 90 before proceeding with the release.

1. Create CR in Omni

Open Omni CMD and use the correct command to create a new CR.

Create the CR with the correct Change Type.

Send an approval email before starting the release.



---

2. Fill Header Details

Category

Service

Service Offering

Configuration Item (CI)

Risk Level

Impact Level

Assignment Group

Post Deployment Review Group

Assigned To



---

3. CR Summary

Provide a short description (Example: “Global Release 3.2.x update”).

Add a detailed description including:

Jenkins URLs

Pipeline URLs

GIT branch

Commit ID

Release Version

Release Notes

Jira Link




---

4. Planning Section

Reason for Change / Business Justification

Release Version Details

Implementation Plan (Step-by-step)

Customer & Business Impact

Technical Impact

Backout Plan (Rollback Steps)



---

5. Change Tasks

Create Implementation Task

Create Backout Task

Add Business Review Approval

Add End Review Approval

Validate all Task Assignment Groups



---

6. Schedule

Add Start Date / Time

Add End Date / Time

Validate Conflict Calendar

Ensure release is within Change Window



---

7. Submit CR First Time (Lead Time Requirement)

Submit CR → System generates CR Number

Re-open CR and continue adding updates

Save after every update



---

8. Requirements & Testing Section (ICE Page)

Requirement Links (Jira Story Links)

Independent Code Review Link (Pull Request URL)

Test Evidence Links (Confluence/Jenkins)

Performance & Stress Test Evidence (If applicable)

Reason for not performing performance test (if required)

Post Deployment Verification Evidence

Select Regression Testing Type

Automated / Manual-Full / Manual-Partial / None


Regression Test Evidence Link



---

9. Jira Activities

Create Jira Ticket for this Release

Add CR Number into Jira

Add all Release Activities in Jira

Update Jira Status as progress continues



---

10. Confluence Page Creation

Create Confluence Page for this CR

Add all prod & non-prod CR release details and links

Add Confluence Page URL into CR



---

11. ICE Details

Add ICE Section Details in CR:

Requirement Links

Evidence Links

Regression Info


Add All Artifact Details

Check ICE Score in Pre-check

Ensure score meets required threshold

Fix all issues highlighted in ICE



---

12. Final Validation

Verify all fields in CR are correct & completed

Check Planning Section – all fields filled

Check Change Tasks – correct & assigned

Check Schedule – correct

Check ICE details – correct

Check Jira & Confluence links added

Ensure no conflicts

All mandatory fields should be green



---

13. Final Submission

Submit CR for final approval

Request approval from required groups

Follow up until CR moves to Scheduled state



---

14. After Approval

Execute Implementation during the release window

Complete all tasks

Add Post-Deployment Info & Evidence

Update Jira & Confluence Page

Move CR to Review → Closed



---
  

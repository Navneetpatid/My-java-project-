CODE RELEASE CHECKLIST (UPDATED ONE PAGE)

1. Create CR in Omni / ServiceNow

Login to Omni

Create new CR with correct Change Type


2. Fill Header Details

Category

Service

Service Offering

Configuration Item (CI)

Risk level

Impact level

Assignment Group

Post Deployment Review Group

Assigned To


3. CR Summary

Short Description (Example: Global Release 3.2.x update)

Description including: Jenkins URLs, GIT branch, Commit ID, Release version, Deployment notes


4. Planning Section

Reason for Change / Business Justification

Release Version details

Implementation Plan (step-by-step)

Customer & Business Impact

Technical Impact

Backout Plan (rollback steps)


5. Change Tasks

Implementation task

Backout task

Business Review approval

Ind Review approval

Validate task assignment groups


6. Schedule

Add Start Date / Time

Add End Date / Time

Validate conflict calendar

Ensure within change window


7. Submit CR First Time to Avoid Lead Time

Submit CR → number generates

Re-open and continue editing

Save after each update


8. Requirements & Testing Section (as per ICE page)

Requirement links (JIRA story links)

Independent code review link (pull request URL)

Test evidence link (Confluence or Jenkins)

Performance and stress test evidence link

Reason for not performing performance test (if applicable)

Post Deployment Verification Evidence link

Regression testing type selected (Automated / Manual-Full / Manual-Partial / None)

Regression test evidence link


9. JIRA Activities

Create JIRA ticket for this release

Add CR number into JIRA

Add all release activities in JIRA (testing, pipeline update, verification, approvals)

Update JIRA status as per progress


10. Confluence Page Creation

Create Confluence page for this CR

Add: Release details, Implementation steps, Evidence links, Test results, Rollback plan

Add page link into CR


11. ICE Details

Add ICE section details in CR (Requirement links, Evidence links, Regression info)

Add Artifacts details

Check ICE Score in ICE Pre-Check

Ensure ICE Score meets required threshold

Fix any issues highlighted in ICE checks


12. Final Validation

Verify all fields in CR are completed

Check Planning → all sections filled

Check Tasks → correct and assigned

Check Schedule → correct

Check ICE details → correct

Check JIRA & Confluence links added

No conflicts pending

All mandatory fields green


13. Final Submission

Submit CR

Request approvals from required groups

Follow up on approval until CR moves to Scheduled state


14. After Approval

Execute implementation during window

Complete all tasks

Add post-deployment info & evidence in CR

Update JIRA & Confluence page

Move CR to Review → Close
  

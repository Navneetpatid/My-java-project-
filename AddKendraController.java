,
The deployment job is failing with the error:
“No such property: cluster for class: appDeploymentPipeline_Node_GKE_v1_PIL”
This indicates that the cluster variable is not defined or not being passed correctly in the pipeline/shared library.
Could you please confirm:
Whether cluster parameter is configured in the Jenkins job, or
If any recent changes were made in the shared library (appDeploymentPipeline_Node_GKE_v1_PIL) related to cluster handling?
It seems we need to either define or correctly pass the cluster value for the pipeline to proceed.

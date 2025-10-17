def call(config) {
    logger.printBlueMessage("==== Docker Image Promotion Pipeline START ====")

    if (config.Image_Type != 'Docker') {
        logger.info("Only Docker image type is supported. Skipping.")
        return
    }

    def docker = new Docker()
    def gcloud = new GCloud()
    def utils = new Utils()
    def gcp_node = 'gcp-node'          // Jenkins node label
    def googleProjectId = 'my-google-project' // Replace with your actual GCP project ID

    // Log input parameters
    logger.info("Parameters Received:")
    logger.info("  Image_Type        : ${config.Image_Type}")
    logger.info("  Image_Action      : ${config.Image_Action}")
    logger.info("  Nexus_Source_Image: ${config.Nexus_Source_Image}")
    logger.info("  Nexus_Target_Image: ${config.Nexus_Target_Image}")

    // Perform the main action
    stage('Docker Image Promotion') {
        if (config.Image_Action == 'Push') {
            logger.printBlueMessage("Action Selected: PUSH - Uploading from Nexus3 to GCR")
            uploadDockerImageToGCR(docker, gcloud, gcp_node, googleProjectId, config.Nexus_Source_Image, config.Nexus_Target_Image)

        } else if (config.Image_Action == 'Pull') {
            logger.printBlueMessage("Action Selected: PULL - Pulling Image from GCR (if implemented)")
            pullDockerImageFromGCR(docker, gcp_node, config.Nexus_Target_Image)

        } else {
            logger.printYellowMessage("Action Selected: SKIP - No GCR operation will be performed")
        }
    }

    logger.printGreenMessage("==== Docker Image Promotion Pipeline COMPLETE ====")
}


//===============================================================
//  Upload Docker Image to GCR
//===============================================================
def uploadDockerImageToGCR(docker, gcloud, gcp_node, googleProjectId, nexusSource, gcrTarget) {
    node(gcp_node) {
        try {
            stage("Docker: Publish Image to GCR") {
                logger.printBlueMessage("Push Docker Image to GCR Stage - START")
                logger.info("Source Nexus3 Image: ${nexusSource}")
                logger.info("Target GCR Image: ${gcrTarget}")

                docker.pullImage(nexusSource)
                docker.tagImage(nexusSource, gcrTarget)
                docker.pushImage(gcrTarget)

                logger.printGreenMessage("Push Docker Image to GCR Stage - COMPLETE")
            }

        } catch (Exception e) {
            logger.error("EGR0001 - Job Failed - ${e.getMessage()}")
            throw e
        } finally {
            deleteDir()
            cleanUp()
        }
    }
}


//===============================================================
//  Pull Docker Image from GCR
//===============================================================
def pullDockerImageFromGCR(docker, gcp_node, gcrImage) {
    node(gcp_node) {
        stage("Docker: Pull Image from GCR") {
            logger.printBlueMessage("Pulling Image from GCR - START")
            docker.pullImage(gcrImage)
            logger.printGreenMessage("Pulling Image from GCR - COMPLETE")
        }
    }
    }

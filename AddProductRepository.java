def call(config) {
    logger.printBlueMessage("==== Docker Image Promotion Pipeline START ====")

    if (config.Image_Type != 'Docker') {
        logger.info("Only Docker image type is supported. Skipping.")
        return
    }

    // Jenkins environment variables
    def gcp_node = 'gcp-node'          // Jenkins agent label
    def googleProjectId = 'my-google-project' // Change to your GCP project id

    // Log received parameters
    logger.info("Parameters Received:")
    logger.info("  Image_Type        : ${config.Image_Type}")
    logger.info("  Image_Action      : ${config.Image_Action}")
    logger.info("  Nexus_Source_Image: ${config.Nexus_Source_Image}")
    logger.info("  Nexus_Target_Image: ${config.Nexus_Target_Image}")

    stage('Docker Image Promotion') {
        if (config.Image_Action == 'Push') {
            logger.printBlueMessage("Action Selected: PUSH - Uploading from Nexus3 to GCR")
            uploadDockerImageToGCR(config.Nexus_Source_Image, config.Nexus_Target_Image, gcp_node, googleProjectId)

        } else if (config.Image_Action == 'Pull') {
            logger.printBlueMessage("Action Selected: PULL - Pulling Image from GCR")
            pullDockerImageFromGCR(config.Nexus_Target_Image, gcp_node)

        } else {
            logger.printYellowMessage("Action Selected: SKIP - No GCR operation will be performed")
        }
    }

    logger.printGreenMessage("==== Docker Image Promotion Pipeline COMPLETE ====")
}


//===============================================================
//  Upload Docker Image to GCR
//===============================================================
def uploadDockerImageToGCR(nexusSource, gcrTarget, gcp_node, googleProjectId) {
    node(gcp_node) {
        try {
            stage("Docker: Publish Image to GCR") {
                logger.printBlueMessage("Push Docker Image to GCR Stage - START")
                logger.info("Source Nexus3 Image: ${nexusSource}")
                logger.info("Target GCR Image: ${gcrTarget}")

                // assuming 'docker' global var is defined in your shared library
                docker.pullImage(nexusSource)
                docker.tagImage(nexusSource, gcrTarget)
                docker.pushImage(gcrTarget)

                logger.printGreenMessage("Push Docker Image to GCR Stage - COMPLETE")
            }

        } catch (Exception e) {
            logger.error("EGR0001 - Job Failed - ${e.getMessage()}")
            throw e
        } finally {
            cleanUp()
        }
    }
}


//===============================================================
//  Pull Docker Image from GCR
//===============================================================
def pullDockerImageFromGCR(gcrImage, gcp_node) {
    node(gcp_node) {
        stage("Docker: Pull Image from GCR") {
            logger.printBlueMessage("Pulling Image from GCR - START")
            docker.pullImage(gcrImage)
            logger.printGreenMessage("Pulling Image from GCR - COMPLETE")
        }
    }
    }

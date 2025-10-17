def call(config) {
    echo "==== Docker Image Promotion Pipeline START ===="

    // ===== Validate Parameters =====
    if (!config.Image_Type || config.Image_Type.trim() != 'Docker') {
        echo "[ERROR] Only Docker image type is supported. Received: ${config.Image_Type}"
        return
    }

    def gcp_node = 'gcp-node'  // Jenkins node label (update to your actual node)
    def googleProjectId = 'my-gcp-project' // Replace with your actual project ID

    // ===== Print All Parameters =====
    echo "Parameters Received:"
    echo "  Image_Type        : ${config.Image_Type}"
    echo "  Image_Action      : ${config.Image_Action}"
    echo "  Nexus_Source_Image: ${config.Nexus_Source_Image}"
    echo "  Nexus_Target_Image: ${config.Nexus_Target_Image}"

    // ===== Run Stage =====
    stage('Docker Image Promotion') {
        switch (config.Image_Action) {
            case 'Push':
                echo "Action Selected: PUSH → Uploading image from Nexus3 to GCR"
                uploadDockerImageToGCR(config.Nexus_Source_Image, config.Nexus_Target_Image, gcp_node, googleProjectId)
                break

            case 'Pull':
                echo "Action Selected: PULL → Pulling image from GCR"
                pullDockerImageFromGCR(config.Nexus_Target_Image, gcp_node)
                break

            case 'Skip':
                echo "Action Selected: SKIP → No Docker operation will be performed"
                break

            default:
                echo "[ERROR] Invalid Image_Action value: ${config.Image_Action}. Must be Push / Pull / Skip."
        }
    }

    echo "==== Docker Image Promotion Pipeline COMPLETE ===="
}


//===============================================================
// Upload Docker Image to GCR
//===============================================================
def uploadDockerImageToGCR(nexusSource, gcrTarget, gcp_node, googleProjectId) {
    node(gcp_node) {
        try {
            stage("Docker: Publish Image to GCR") {
                echo "Push Docker Image to GCR Stage - START"
                echo "Source Nexus3 Image : ${nexusSource}"
                echo "Target GCR Image    : ${gcrTarget}"

                sh """
                    echo "Pulling image from Nexus3..."
                    docker pull ${nexusSource}

                    echo "Tagging image for GCR..."
                    docker tag ${nexusSource} ${gcrTarget}

                    echo "Authenticating with GCR..."
                    gcloud auth configure-docker -q

                    echo "Pushing image to GCR..."
                    docker push ${gcrTarget}
                """

                echo "Push Docker Image to GCR Stage - COMPLETE"
            }
        } catch (Exception e) {
            echo "[ERROR] Push to GCR failed: ${e.getMessage()}"
            error("Push to GCR failed.")
        } finally {
            cleanUp()
        }
    }
}


//===============================================================
// Pull Docker Image from GCR
//===============================================================
def pullDockerImageFromGCR(gcrImage, gcp_node) {
    node(gcp_node) {
        stage("Docker: Pull Image from GCR") {
            try {
                echo "Pulling Image from GCR - START"
                sh """
                    echo "Authenticating with GCR..."
                    gcloud auth configure-docker -q

                    echo "Pulling image from GCR..."
                    docker pull ${gcrImage}
                """
                echo "Pulling Image from GCR - COMPLETE"
            } catch (Exception e) {
                echo "[ERROR] Pull from GCR failed: ${e.getMessage()}"
                error("Pull from GCR failed.")
            } finally {
                cleanUp()
            }
        }
    }
}


//===============================================================
// Clean Up Workspace
//===============================================================
def cleanUp() {
    echo "Cleaning up workspace and Docker cache..."
    sh "docker system prune -af || true"
    deleteDir()
                    }

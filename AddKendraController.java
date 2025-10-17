//----------- Create Docker Image -----------
private def createDockerImage() {
    logger.printBlueMessage("Docker Image Build Stage - START")

    def dockerFilePath = (this.config.dockerFilePath) ? this.config.dockerFilePath : ''
    workspaceDir = '.'
    String mvnTargetDir = "target"

    if (!".".equals(dockerFilePath.trim())) {
        int lastIndex = dockerFilePath.lastIndexOf("/")
        mvnTargetDir = dockerFilePath.substring(0, lastIndex)
        workspaceDir = sourceCodeDir + "/" + mvnTargetDir
    }

    // logger.info("inside custom workspace ${workspaceDir}")

    // TODO: This needs refactoring, use pipelineTools.getTool('maven')
    def mvn_version = "Maven_3_3_3_Linux"
    def jdk_version = "JDK_1_17_0_2_Linux"

    docker = new Docker()

    logger.info("inside custom workspace ${workspaceDir}")

    stage('Docker: Build Image') {
        logger.trace("Docker Image Build Stage Start")
        logger.info("initial dockerFilePath: ${dockerFilePath}")
        if (isUiApp) {
            docker.initNode()
            this.artifactId = this.appName
            uiDockerTag = docker.buildImageUI(this.appName, this.appVersion)
        } else {
            docker.init(mvn_version, jdk_version, workspaceDir)
            this.appVersion = docker.getVersion()
            this.artifactId = docker.getArtifactId()
            this.groupId = docker.getGroupId()
            docker.buildImage(dockerFilePath)
        }
        logger.trace("Docker Image Build Stage Complete")
        logger.printGreenMessage("Docker Image Build Stage - COMPLETE")
    }
}

//----------- Upload Docker Image To Nexus3 -----------
private def uploadDockerImageToNexus3() {
    logger.printBlueMessage("Publish Image to Nexus 3 Stage - START")

    stage("Docker: Publish Image to Nexus 3 Repository") {
        logger.info("Docker Image Nexus Stage Start")
        dockerNexus3Tag = utils().createNexus3TagForAH("docker", this.appVersion)
        logger.info("dockerNexus3Tag: " + dockerNexus3Tag)
        logger.info("dockerNexus3CompleteTag: " + uiDockerTag)

        if (isUiApp) {
            // this.artifactId = uiDockerTag
            // this.groupId = uiNexusPath // take this as user parameter
            docker.publishDockerImageToNexus3DevStagingForUI(uiDockerTag)
        } else {
            docker.publishDockerImageToNexus3DevStaging(dockerNexus3Tag)
        }

        logger.trace("Docker: Publish Image to Nexus 3 Repository Complete")
    }

    logger.printGreenMessage("Publish Image to Nexus 3 Stage - COMPLETE")
              }

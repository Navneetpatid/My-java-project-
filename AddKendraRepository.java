def call() {

    logger.printBlueMessage("==== Docker Image Promotion Pipeline START ====")

    // Check Image Type
    if (params.Image_Type != 'Docker') {
        logger.warn("Only Docker image type is supported. Skipping.")
        return
    }

    // Create instance of main class
    def dockerPipeline = new org.example.helper.DockerPipelineClass()

    // 1️⃣ Upload Docker Image to Nexus3
    dockerPipeline.uploadDockerImageToNexus3()

    // 2️⃣ Run Cyberflows Image Scan (optional, if you enable it in your class)
    dockerPipeline.executeCyberflowsImageScan()

    // 3️⃣ Perform action based on Image_Action parameter
    if (params.Image_Action == 'Push') {
        logger.printBlueMessage("Action Selected: PUSH — Uploading to GCR")
        dockerPipeline.uploadDockerImageToGCR()
    } else if (params.Image_Action == 'Pull') {
        logger.printBlueMessage("Action Selected: PULL — Pulling Image from GCR (if implemented)")
        // Optional: implement dockerPipeline.pullDockerImageFromGCR() in your class
    } else {
        logger.printYellowMessage("Action Selected: SKIP — No GCR operation will be performed")
    }

    logger.printGreenMessage("==== Docker Image Promotion Pipeline COMPLETE ====")
}

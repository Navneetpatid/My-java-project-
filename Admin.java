@Library('grctest_navneet') _
pipeline {
    agent any

    parameters {
        choice(name: 'Image_Type', choices: ['Docker'], description: 'Select the image type')
        choice(name: 'Image_Action', choices: ['Push', 'Pull', 'Skip'], description: 'Select image action')
        string(name: 'Nexus_Source_Image', defaultValue: 'nexus3.example.com/repo/app:1.0.0', description: 'Source image path in Nexus3')
        string(name: 'Nexus_Target_Image', defaultValue: 'gcr.io/my-project/app:1.0.0', description: 'Target image path in GCR')
    }

    stages {
        stage('Run Docker Image Promotion Pipeline') {
            steps {
                script {
                    testPipeline([
                        Image_Type: params.Image_Type,
                        Image_Action: params.Image_Action,
                        Nexus_Source_Image: params.Nexus_Source_Image,
                        Nexus_Target_Image: params.Nexus_Target_Image
                    ])
                }
            }
        }
    }
}

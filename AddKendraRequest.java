@Library('gcr@test_navbeey') _

pipeline {
    agent any

    parameters {
        choice(name: 'Image_Type', choices: ['Docker'], description: 'Select the image type')
        choice(name: 'Image_Action', choices: ['Push', 'Pull', 'Skip'], description: 'Select whether to Push, Pull or Skip the image operation')
        string(name: 'Nexus3_Source_Image', defaultValue: '', description: 'Full Nexus3 image path to pull from (e.g., nexus3.systems.uk.hsbc:8080/com/hsbc/host/app:1.0.0)')
        string(name: 'Nexus3_Target_Image', defaultValue: '', description: 'Target image name and tag (without gcr.io prefix, e.g., hap/hap-cdr-microservice:1.0.0)')
    }

    stages {
        stage('Run Docker Image Promotion Pipeline') {
            steps {
                script {
                    // Call shared library helper
                    dockerPipelineHelper()
                }
            }
        }
    }
                     }

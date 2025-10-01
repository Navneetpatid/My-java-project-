
@Library('your-shared-library') _

pipeline {
    agent any

    stages {
        stage('Run Config Loader') {
            steps {
                script {
                    loadHardcodedConfig()   // this calls your shared lib
                }
            }
        }
    }
}

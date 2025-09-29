pipeline {
    agent any
    stages {
        stage('Checkout') {
            steps { git 'https://github.com/example/repo.git' }
        }
        stage('Build') {
            steps { sh 'mvn clean package' }
        }
        stage('Test') {
            steps { sh 'mvn test' }
        }
pipeline {
    agent any
    stages {
        stage('Test') {
            steps {
                echo 'Running tests...'
                sh 'echo Hello Test Successful'
            }
        }
    }
}

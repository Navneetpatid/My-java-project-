// Example 1: Hello World
pipeline {
    agent any
    stages {
        stage('Hello') {
            steps {
                echo 'Hello World'
            }
        }
    }
}

// Example 2: Run a Shell Command
pipeline {
    agent any
    stages {
        stage('Shell') {
            steps {
                sh 'echo Running a simple shell command'
            }
        }
    }
}

// Example 3: Checkout from Git
pipeline {
    agent any
    stages {
        stage('Checkout') {
            steps {
                git 'https://github.com/example/repo.git'
            }
        }
    }
}

// Example 4: Build with Maven
pipeline {
    agent any
    stages {
        stage('Build') {
            steps {
                sh 'mvn clean package'
            }
        }
    }
}

// Example 5: Run Unit Tests
pipeline {
    agent any
    stages {
        stage('Test') {
            steps {
                sh 'mvn test'
            }
        }
    }
}

// Example 6: Multiple Stages (Build + Test)
pipeline {
    agent any
    stages {
        stage('Build') {
            steps {
                echo 'Building project...'
            }
        }
        stage('Test') {
            steps {
                echo 'Running tests...'
            }
        }
    }
}

// Example 7: Use Environment Variables
pipeline {
    agent any
    environment {
        MY_VAR = "HelloEnv"
    }
    stages {
        stage('Env Vars') {
            steps {
                sh 'echo "Value of MY_VAR is $MY_VAR"'
            }
        }
    }
}

// Example 8: Parallel Stages
pipeline {
    agent any
    stages {
        stage('Parallel Jobs') {
            parallel {
                stage('Job 1') {
                    steps { echo 'Job 1 running' }
                }
                stage('Job 2') {
                    steps { echo 'Job 2 running' }
                }
            }
        }
    }
}

// Example 9: Post-Build Actions
pipeline {
    agent any
    stages {
        stage('Main Task') {
            steps {
                script {
                    error("Simulating failure")
                }
            }
        }
    }
    post {
        success { echo "Build Success!" }
        failure { echo "Build Failed!" }
        always { echo "Cleaning up..." }
    }
}

// Example 10: Pipeline with Parameters
pipeline {
    agent any
    parameters {
        string(name: 'USER_NAME', defaultValue: 'DefaultUser', description: 'Enter your name')
    }
    stages {
        stage('Greeting') {
            steps {
                echo "Hello ${params.USER_NAME}, your build is running!"
            }
        }
    }
}

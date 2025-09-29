//------------------------------------
// Example 1: Hello World
//------------------------------------
pipeline {
    agent any
    stages {
        stage('Hello') {
            steps {
                echo 'Hello Jenkins! My first pipeline.'
            }
        }
    }
}

//------------------------------------
// Example 2: Run Shell Command
//------------------------------------
pipeline {
    agent any
    stages {
        stage('Shell Command') {
            steps {
                sh 'echo "This is a shell command running inside Jenkins"'
            }
        }
    }
}

//------------------------------------
// Example 3: Multiple Stages
//------------------------------------
pipeline {
    agent any
    stages {
        stage('Stage 1') {
            steps { echo 'This is Stage 1' }
        }
        stage('Stage 2') {
            steps { echo 'This is Stage 2' }
        }
    }
}

//------------------------------------
// Example 4: Using Environment Variable
//------------------------------------
pipeline {
    agent any
    environment {
        GREETING = "Hello from Environment"
    }
    stages {
        stage('Env Example') {
            steps {
                sh 'echo $GREETING'
            }
        }
    }
}

//------------------------------------
// Example 5: Parameters Example
//------------------------------------
pipeline {
    agent any
    parameters {
        string(name: 'USERNAME', defaultValue: 'JenkinsUser', description: 'Enter your name')
    }
    stages {
        stage('Greeting') {
            steps {
                echo "Hello ${params.USERNAME}, this is your pipeline!"
            }
        }
    }
}

//------------------------------------
// Example 6: Parallel Stages
//------------------------------------
pipeline {
    agent any
    stages {
        stage('Parallel Example') {
            parallel {
                stage('Task A') {
                    steps { echo 'Running Task A' }
                }
                stage('Task B') {
                    steps { echo 'Running Task B' }
                }
            }
        }
    }
}

//------------------------------------
// Example 7: Fail and Catch with Post
//------------------------------------
pipeline {
    agent any
    stages {
        stage('Failure Demo') {
            steps {
                script {
                    error("Intentional failure for demo")
                }
            }
        }
    }
    post {
        success { echo 'Pipeline succeeded!' }
        failure { echo 'Pipeline failed!' }
        always { echo 'This will always run (cleanup etc.)' }
    }
}

//------------------------------------
// Example 8: Loop Example
//------------------------------------
pipeline {
    agent any
    stages {
        stage('Loop') {
            steps {
                script {
                    for (int i = 1; i <= 3; i++) {
                        echo "Loop count: ${i}"
                    }
                }
            }
        }
    }
}

//------------------------------------
// Example 9: When Condition
//------------------------------------
pipeline {
    agent any
    stages {
        stage('Run on Branch') {
            when {
                branch "main"
            }
            steps {
                echo 'This runs only on the main branch (for demo, it always passes if branch is main).'
            }
        }
    }
}

//------------------------------------
// Example 10: Simple Build Simulation
//------------------------------------
pipeline {
    agent any
    stages {
        stage('Build') {
            steps {
                echo 'Compiling code... (dummy step)'
            }
        }
        stage('Test') {
            steps {
                echo 'Running tests... (dummy step)'
            }
        }
        stage('Deploy') {
            steps {
                echo 'Deploying to staging... (dummy step)'
            }
        }
    }
}

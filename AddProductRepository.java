pipeline {
    agent any

    parameters {
        string(name: 'USER', defaultValue: 'Student', description: 'Enter your name')
    }

    environment {
        COURSE = "Jenkins Basics"
        AUTHOR = "Trainer"
    }

    stages {
        // Example 1: Hello World
        stage('Example 1 - Hello World') {
            steps {
                echo 'Hello Jenkins! 🎉'
            }
        }

        // Example 2: Multiple Stages in One
        stage('Example 2 - Multiple Steps') {
            steps {
                echo 'This is Step 1'
                echo 'This is Step 2'
            }
        }

        // Example 3: Run Shell Command
        stage('Example 3 - Shell Command') {
            steps {
                sh 'echo "Hello from Linux shell command"'
            }
        }

        // Example 4: Parameters
        stage('Example 4 - Parameters') {
            steps {
                echo "Hello ${params.USER}, welcome to Jenkins pipeline!"
            }
        }

        // Example 5: Environment Variables
        stage('Example 5 - Environment Variables') {
            steps {
                echo "Course: ${env.COURSE}"
                echo "Author: ${env.AUTHOR}"
            }
        }

        // Example 6: Parallel Stages
        stage('Example 6 - Parallel Example') {
            parallel {
                stage('Job A') {
                    steps { echo 'Running Job A in parallel' }
                }
                stage('Job B') {
                    steps { echo 'Running Job B in parallel' }
                }
            }
        }

        // Example 7: Post Actions (inside stage simulation)
        stage('Example 7 - Post Simulation') {
            steps {
                echo 'Pretend: Main task running'
                echo 'If it fails, post will handle it (see below)'
            }
        }

        // Example 8: Loop
        stage('Example 8 - Loop Example') {
            steps {
                script {
                    for (int i = 1; i <= 3; i++) {
                        echo "Loop count: ${i}"
                    }
                }
            }
        }

        // Example 9: Conditional (When)
        stage('Example 9 - Conditional') {
            when {
                expression { return true }  // always true for demo
            }
            steps {
                echo 'This stage runs only if condition is true'
            }
        }

        // Example 10: Mini CI/CD Flow
        stage('Example 10 - Mini CI/CD') {
            steps {
                echo 'Pretend: Checkout code...'
                echo 'Pretend: Build project...'
                echo 'Pretend: Run tests...'
                echo 'Pretend: Package artifacts...'
                echo 'Pretend: Deploy to staging...'
            }
        }
    }

    post {
        success { echo '✅ Pipeline finished successfully!' }
        failure { echo '❌ Pipeline failed!' }
        always  { echo '🔄 Always running cleanup step' }
    }
}

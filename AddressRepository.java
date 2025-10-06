// ===============================================
// 20 Jenkins Pipeline Examples (Basic + Advanced)
// ===============================================

// -------------------------------
// 1. Basic Hello World Pipeline
// -------------------------------
pipeline {
    agent any
    stages {
        stage('Hello') {
            steps {
                echo 'Hello, Jenkins!'
            }
        }
    }
}

// -------------------------------
// 2. Multiple Stages Example
// -------------------------------
pipeline {
    agent any
    stages {
        stage('Build') {
            steps { echo 'Building...' }
        }
        stage('Test') {
            steps { echo 'Testing...' }
        }
        stage('Deploy') {
            steps { echo 'Deploying...' }
        }
    }
}

// -------------------------------
// 3. Environment Variable Example
// -------------------------------
pipeline {
    agent any
    environment {
        APP_NAME = 'MyApp'
    }
    stages {
        stage('Print ENV') {
            steps {
                echo "Application: ${APP_NAME}"
            }
        }
    }
}

// -------------------------------
// 4. Input Parameter Example
// -------------------------------
pipeline {
    agent any
    parameters {
        string(name: 'USERNAME', defaultValue: 'guest', description: 'Enter username')
    }
    stages {
        stage('Greet User') {
            steps {
                echo "Hello, ${params.USERNAME}"
            }
        }
    }
}

// -------------------------------
// 5. Using Tools Example
// -------------------------------
pipeline {
    agent any
    tools { git 'Default' }
    stages {
        stage('Tool Info') {
            steps {
                echo 'Git tool available'
            }
        }
    }
}

// -------------------------------
// 6. Simple If Condition
// -------------------------------
pipeline {
    agent any
    parameters {
        booleanParam(name: 'RUN_DEPLOY', defaultValue: true, description: 'Run deploy stage')
    }
    stages {
        stage('Conditional') {
            steps {
                script {
                    if (params.RUN_DEPLOY) {
                        echo 'Deploying now...'
                    } else {
                        echo 'Skipping deploy.'
                    }
                }
            }
        }
    }
}

// -------------------------------
// 7. Simple Loop Example
// -------------------------------
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

// -------------------------------
// 8. File Operation Example
// -------------------------------
pipeline {
    agent any
    stages {
        stage('File Write') {
            steps {
                writeFile file: 'output.txt', text: 'Pipeline file content.'
                echo 'File created successfully.'
            }
        }
    }
}

// -------------------------------
// 9. Post Actions Example
// -------------------------------
pipeline {
    agent any
    stages {
        stage('Run') {
            steps { echo 'Running main job...' }
        }
    }
    post {
        success { echo '✅ Build Success!' }
        failure { echo '❌ Build Failed!' }
        always { echo '🏁 Job Finished.' }
    }
}

// -------------------------------
// 10. Parallel Stages Example
// -------------------------------
pipeline {
    agent any
    stages {
        stage('Parallel Tasks') {
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

// ==================================================
// ADVANCED PIPELINE EXAMPLES
// ==================================================

// -------------------------------
// 11. Matrix Build Example
// -------------------------------
pipeline {
    agent none
    stages {
        stage('Matrix Build') {
            matrix {
                axes {
                    axis { name 'OS'; values 'Linux', 'Windows' }
                    axis { name 'BROWSER'; values 'Chrome', 'Firefox' }
                }
                agent any
                stages {
                    stage('Test') {
                        steps {
                            echo "Testing on ${OS} with ${BROWSER}"
                        }
                    }
                }
            }
        }
    }
}

// -------------------------------
// 12. Using Credentials
// -------------------------------
pipeline {
    agent any
    environment {
        MY_CRED = credentials('my-credentials-id')
    }
    stages {
        stage('Show Credentials') {
            steps {
                echo "Using username: ${MY_CRED_USR}"
            }
        }
    }
}

// -------------------------------
// 13. Trigger Another Job
// -------------------------------
pipeline {
    agent any
    stages {
        stage('Trigger Job') {
            steps {
                build job: 'other-pipeline', wait: false
                echo 'Triggered another job.'
            }
        }
    }
}

// -------------------------------
// 14. Timeout Example
// -------------------------------
pipeline {
    agent any
    stages {
        stage('Timeout Demo') {
            steps {
                timeout(time: 5, unit: 'SECONDS') {
                    echo 'Sleeping for 2 seconds'
                    sleep 2
                }
            }
        }
    }
}

// -------------------------------
// 15. Retry Example
// -------------------------------
pipeline {
    agent any
    stages {
        stage('Retry Stage') {
            steps {
                retry(3) {
                    echo 'Attempting step...'
                }
            }
        }
    }
}

// -------------------------------
// 16. Shared Library Example
// -------------------------------
@Library('my-shared-lib') _
pipeline {
    agent any
    stages {
        stage('Library') {
            steps {
                script {
                    mySharedMethod()
                }
            }
        }
    }
}

// -------------------------------
// 17. Agent Label Example
// -------------------------------
pipeline {
    agent { label 'linux-node' }
    stages {
        stage('Build') {
            steps { echo 'Building on Linux node' }
        }
    }
}

// -------------------------------
// 18. When Condition Example
// -------------------------------
pipeline {
    agent any
    parameters {
        string(name: 'ENV', defaultValue: 'dev', description: 'Environment')
    }
    stages {
        stage('Deploy') {
            when {
                expression { params.ENV == 'prod' }
            }
            steps {
                echo 'Deploying to production...'
            }
        }
    }
}

// -------------------------------
// 19. Artifact Archiving Example
// -------------------------------
pipeline {
    agent any
    stages {
        stage('Archive') {
            steps {
                writeFile file: 'artifact.txt', text: 'Some artifact data'
                archiveArtifacts artifacts: 'artifact.txt', fingerprint: true
            }
        }
    }
}

// -------------------------------
// 20. Complex Scripted Stage
// -------------------------------
pipeline {
    agent any
    stages {
        stage('Complex Logic') {
            steps {
                script {
                    def versions = ['1.0', '1.1', '1.2']
                    versions.each { v ->
                        echo "Deploying version ${v}"
                    }
                    echo "Deployment complete."
                }
            }
        }
    }
    }

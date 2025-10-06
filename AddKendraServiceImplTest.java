// ===================================================
// 30 Jenkins Pipeline Examples (Basic → Advanced)
// Learning + Testing Purpose
// ===================================================

// -------------------------------
// 1. Hello World
// -------------------------------
pipeline {
    agent any
    stages {
        stage('Hello') {
            steps {
                echo 'Hello Jenkins!'
            }
        }
    }
}

// -------------------------------
// 2. Multiple Stages
// -------------------------------
pipeline {
    agent any
    stages {
        stage('Build') { steps { echo 'Building...' } }
        stage('Test') { steps { echo 'Testing...' } }
        stage('Deploy') { steps { echo 'Deploying...' } }
    }
}

// -------------------------------
// 3. Environment Variable
// -------------------------------
pipeline {
    agent any
    environment {
        APP = 'SampleApp'
    }
    stages {
        stage('Show ENV') {
            steps { echo "App: ${APP}" }
        }
    }
}

// -------------------------------
// 4. Input Parameter
// -------------------------------
pipeline {
    agent any
    parameters {
        string(name: 'USERNAME', defaultValue: 'guest', description: 'Enter name')
    }
    stages {
        stage('Greet') {
            steps { echo "Hello ${params.USERNAME}" }
        }
    }
}

// -------------------------------
// 5. Boolean Parameter
// -------------------------------
pipeline {
    agent any
    parameters {
        booleanParam(name: 'RUN_TEST', defaultValue: true, description: 'Run tests?')
    }
    stages {
        stage('Check') {
            steps {
                script {
                    if (params.RUN_TEST) echo 'Running tests...'
                    else echo 'Tests skipped.'
                }
            }
        }
    }
}

// -------------------------------
// 6. Simple Loop
// -------------------------------
pipeline {
    agent any
    stages {
        stage('Loop') {
            steps {
                script {
                    for (int i = 1; i <= 5; i++) {
                        echo "Loop ${i}"
                    }
                }
            }
        }
    }
}

// -------------------------------
// 7. Conditional Stage
// -------------------------------
pipeline {
    agent any
    stages {
        stage('Condition') {
            steps {
                script {
                    def value = 10
                    if (value > 5) echo 'Value > 5'
                    else echo 'Value ≤ 5'
                }
            }
        }
    }
}

// -------------------------------
// 8. File Write and Read
// -------------------------------
pipeline {
    agent any
    stages {
        stage('FileOps') {
            steps {
                writeFile file: 'data.txt', text: 'This is Jenkins pipeline file.'
                echo readFile('data.txt')
            }
        }
    }
}

// -------------------------------
// 9. Post Actions
// -------------------------------
pipeline {
    agent any
    stages {
        stage('Run') { steps { echo 'Running job...' } }
    }
    post {
        success { echo '✅ Success' }
        failure { echo '❌ Failure' }
        always { echo '🏁 Finished' }
    }
}

// -------------------------------
// 10. Parallel Stages
// -------------------------------
pipeline {
    agent any
    stages {
        stage('Parallel') {
            parallel {
                stage('A') { steps { echo 'Task A' } }
                stage('B') { steps { echo 'Task B' } }
            }
        }
    }
}

// -------------------------------
// 11. Timeout Example
// -------------------------------
pipeline {
    agent any
    stages {
        stage('Timeout') {
            steps {
                timeout(time: 5, unit: 'SECONDS') {
                    echo 'Wait 2 seconds'
                    sleep 2
                }
            }
        }
    }
}

// -------------------------------
// 12. Retry Block
// -------------------------------
pipeline {
    agent any
    stages {
        stage('Retry') {
            steps {
                retry(3) {
                    echo 'Attempting...'
                }
            }
        }
    }
}

// -------------------------------
// 13. Matrix Example
// -------------------------------
pipeline {
    agent none
    stages {
        stage('Matrix') {
            matrix {
                axes {
                    axis { name 'OS'; values 'Linux', 'Windows' }
                    axis { name 'BROWSER'; values 'Chrome', 'Firefox' }
                }
                agent any
                stages {
                    stage('Run') {
                        steps { echo "Testing on ${OS} with ${BROWSER}" }
                    }
                }
            }
        }
    }
}

// -------------------------------
// 14. Artifact Archive
// -------------------------------
pipeline {
    agent any
    stages {
        stage('Archive') {
            steps {
                writeFile file: 'artifact.txt', text: 'Build output'
                archiveArtifacts artifacts: '*.txt', fingerprint: true
            }
        }
    }
}

// -------------------------------
// 15. Agent Label
// -------------------------------
pipeline {
    agent { label 'linux' }
    stages {
        stage('Build') { steps { echo 'Running on Linux agent' } }
    }
}

// -------------------------------
// 16. When Condition
// -------------------------------
pipeline {
    agent any
    parameters {
        string(name: 'ENV', defaultValue: 'dev', description: 'Environment')
    }
    stages {
        stage('Deploy') {
            when { expression { params.ENV == 'prod' } }
            steps { echo 'Deploying to Production' }
        }
    }
}

// -------------------------------
// 17. Build Trigger
// -------------------------------
pipeline {
    agent any
    stages {
        stage('Trigger') {
            steps {
                echo 'Triggering another job...'
                // build job: 'OtherJob', wait: false
            }
        }
    }
}

// -------------------------------
// 18. Email Notification (Mock)
// -------------------------------
pipeline {
    agent any
    stages {
        stage('Email') {
            steps { echo 'Pretend sending email...' }
        }
    }
    post {
        always { echo 'Email stage done.' }
    }
}

// -------------------------------
// 19. Shared Library (Mock)
// -------------------------------
@Library('my-lib') _
pipeline {
    agent any
    stages {
        stage('Lib Use') {
            steps { echo 'Using shared lib function...' }
        }
    }
}

// -------------------------------
// 20. Declarative + Scripted Mix
// -------------------------------
pipeline {
    agent any
    stages {
        stage('Mix') {
            steps {
                script {
                    def nums = [1, 2, 3]
                    nums.each { echo "Num: ${it}" }
                }
            }
        }
    }
}

// -------------------------------
// 21. Credentials Mock
// -------------------------------
pipeline {
    agent any
    stages {
        stage('Creds') {
            steps { echo 'Credentials handled securely (mock).' }
        }
    }
}

// -------------------------------
// 22. Sleep Demo
// -------------------------------
pipeline {
    agent any
    stages {
        stage('Sleep') {
            steps {
                echo 'Sleeping 2 sec...'
                sleep 2
                echo 'Awake now.'
            }
        }
    }
}

// -------------------------------
// 23. Build Number Usage
// -------------------------------
pipeline {
    agent any
    stages {
        stage('Build Info') {
            steps { echo "Build number: ${env.BUILD_NUMBER}" }
        }
    }
}

// -------------------------------
// 24. Parallel Testing Simulation
// -------------------------------
pipeline {
    agent any
    stages {
        stage('Parallel Testing') {
            parallel {
                stage('Unit Tests') { steps { echo 'Running unit tests...' } }
                stage('Integration Tests') { steps { echo 'Running integration tests...' } }
            }
        }
    }
}

// -------------------------------
// 25. Docker (Mock)
// -------------------------------
pipeline {
    agent any
    stages {
        stage('Docker Build') {
            steps { echo 'Simulating Docker build...' }
        }
    }
}

// -------------------------------
// 26. Dynamic Stage Creation
// -------------------------------
pipeline {
    agent any
    stages {
        stage('Dynamic') {
            steps {
                script {
                    ['A', 'B', 'C'].each { echo "Running dynamic stage ${it}" }
                }
            }
        }
    }
}

// -------------------------------
// 27. Git Checkout (Mock)
// -------------------------------
pipeline {
    agent any
    stages {
        stage('Git') {
            steps {
                echo 'Simulating git checkout...'
            }
        }
    }
}

// -------------------------------
// 28. Clean Workspace
// -------------------------------
pipeline {
    agent any
    stages {
        stage('Clean') {
            steps {
                cleanWs()
                echo 'Workspace cleaned.'
            }
        }
    }
}

// -------------------------------
// 29. Error Handling
// -------------------------------
pipeline {
    agent any
    stages {
        stage('Error') {
            steps {
                script {
                    try {
                        error('Forced failure for testing.')
                    } catch (err) {
                        echo "Caught error: ${err}"
                    }
                }
            }
        }
    }
}

// -------------------------------
// 30. Complete Example (Build→Test→Deploy)
// -------------------------------
pipeline {
    agent any
    stages {
        stage('Build') { steps { echo 'Building project...' } }
        stage('Test') { steps { echo 'Testing project...' } }
        stage('Deploy') { steps { echo 'Deploying project...' } }
    }
    post {
        success { echo '🎉 All stages completed successfully.' }
        failure { echo '💥 Build failed.' }
    }
}

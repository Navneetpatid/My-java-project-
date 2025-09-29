// Example 1: Hello World
node {
    stage('Hello') {
        echo 'Hello World'
    }
}

// Example 2: Run Shell Command
node {
    stage('Shell Command') {
        sh 'echo Running a simple shell command'
    }
}

// Example 3: Checkout from Git
node {
    stage('Checkout') {
        git 'https://github.com/example/repo.git'
    }
}

// Example 4: Build with Maven
node {
    stage('Build') {
        sh 'mvn clean package'
    }
}

// Example 5: Run Unit Tests
node {
    stage('Test') {
        sh 'mvn test'
    }
}

// Example 6: Multiple Stages (Build + Test)
node {
    stage('Build') {
        sh 'echo Building project...'
    }
    stage('Test') {
        sh 'echo Running tests...'
    }
}

// Example 7: Use Environment Variables
node {
    stage('Env Vars') {
        env.MY_VAR = "HelloEnv"
        sh 'echo "Value of MY_VAR is $MY_VAR"'
    }
}

// Example 8: Parallel Stages
node {
    stage('Parallel Jobs') {
        parallel(
            job1: {
                sh 'echo Job 1 running'
            },
            job2: {
                sh 'echo Job 2 running'
            }
        )
    }
}

// Example 9: Post-Build Actions
node {
    try {
        stage('Main Task') {
            sh 'exit 1' // simulate failure
        }
    } catch (err) {
        echo "Build failed: ${err}"
    } finally {
        echo "Cleaning up..."
    }
}

// Example 10: Pipeline with Parameters
node {
    stage('Run with Params') {
        def name = params.USER_NAME ?: 'DefaultUser'
        echo "Hello ${name}, your build is running!"
    }
}

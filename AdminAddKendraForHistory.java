@Library('gcr@test_navneet') _

def call(Map params = [:]) {

    // Import your Docker class
    import com.hscb.hap.hapx.docker.Docker

    // Create object instance
    def docker = new Docker(this)

    stage('Build & Push') {
        def tag = "your-docker-tag-here"
        docker.publishDockerImageToNexus3DevStagingForUI(tag)
    }
}

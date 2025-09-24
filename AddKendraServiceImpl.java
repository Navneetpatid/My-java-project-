
import groovy.transform.Field

@Field String grcNode = "cm-linux-cjoc"

def call(Map config) {
    this.config = config

    node(grcNode) {
        stage('Print Parameters') {
            echo "Engagement ID: ${params.engagementid}"
            echo "EIM ID: ${params.eimid}"
            echo "URL: ${params.URL}"
        }

        stage('Split and Print URL') {
            script {
                def parts = splitUrl(params.URL)
                echo "URL Parts:"
                parts.each { part ->
                    echo part
                }
            }
        }
    }
}

def splitUrl(String url) {
    return url.tokenize(".")
}

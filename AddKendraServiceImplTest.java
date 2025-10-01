// deploymentPayload.groovy
import groovy.json.JsonOutput

// -------------------------------
// 1) Small model classes (each has a toMap() to produce serializable Maps)
// -------------------------------
class Plugin {
    String name
    boolean enabled
    Map config

    Map toMap() {
        [name: name, enabled: enabled, config: config]
    }
}

class Configuration {
    String hapEngagementID
    String emailDistributionList
    String workspace
    String deployToDmz
    String oasFilePath
    String action
    List<Plugin> plugins = []

    Map toMap() {
        [
            hapEngagementID: hapEngagementID,
            emailDistributionList: emailDistributionList,
            workspace: workspace,
            deployToDmz: deployToDmz,
            oasFilePath: oasFilePath,
            action: action,
            plugins: plugins.collect{ it.toMap() }
        ]
    }
}

class PipelineData {
    String deployControlSummery
    String id
    String component
    String purl
    String rollbackArtifactInstance
    String config
    List<String> eimIds = []
    String version
    List<String> tags = []

    Map toMap() {
        [
            deployControlSummery: deployControlSummery,
            id: id,
            component: component,
            purl: purl,
            rollbackArtifactInstance: rollbackArtifactInstance,
            config: config,
            eimIds: eimIds,
            version: version,
            tags: tags
        ]
    }
}

class JobParam {
    String pipelineGITBranch
    String targetEnvironment
    String configurationYMLPath
    String snowCR
    boolean verboseLogging

    Map toMap() {
        [
            pipelineGITBranch: pipelineGITBranch,
            targetEnvironment: targetEnvironment,
            configurationYMLPath: configurationYMLPath,
            snowCR: snowCR,
            verboseLogging: verboseLogging
        ]
    }
}

class JobError {
    String errorCode
    String errorMessage

    Map toMap() {
        [errorCode: errorCode, errorMessage: errorMessage]
    }
}

class Job {
    String buildNumber
    String buildUser
    String buildURL
    List<JobError> error = []
    String buildEnd
    String buildStatus
    String buildStart

    Map toMap() {
        [
            buildNumber: buildNumber,
            buildUser: buildUser,
            buildURL: buildURL,
            error: error.collect{ it.toMap() },
            buildEnd: buildEnd,
            buildStatus: buildStatus,
            buildStart: buildStart
        ]
    }
}

class CentralEnvironmentRegister {
    boolean success
    List<String> errors = []
    String workspace
    String cp_admin_api_url
    List<String> mandatoryPlugins = []
    String dp_host_url
    String gbgf
    String dmz_Lb

    Map toMap() {
        [
            success: success,
            errors: errors,
            workspace: workspace,
            cp_admin_api_url: cp_admin_api_url,
            mandatoryPlugins: mandatoryPlugins,
            dp_host_url: dp_host_url,
            gbgf: gbgf,
            dmz_Lb: dmz_Lb
        ]
    }
}

// -------------------------------
// 2) Build the objects step-by-step (using your provided values)
// -------------------------------
int step = 1
println "Step ${step++}: Create plugin configuration..."
def pluginConfig = [ second: 5, hour: 10000, policy: "local", fault_tolerant: true, hide_client_headers: false ]
def rateLimiting = new Plugin(name: "rate-limiting", enabled: true, config: pluginConfig)

println "Step ${step++}: Create main Configuration..."
def configuration = new Configuration(
    hapEngagementID: "HAP-CTO-000",
    emailDistributionList: "chetan.radke@hsbc.co.in",
    workspace: "HAP-HK-CTO-000-PRD",
    deployToDmz: "disable",
    oasFilePath: "./oas-PRD.yaml",
    action: "deploy",
    plugins: [rateLimiting]
)

println "Step ${step++}: Create PipelineData..."
def deployControlSummery = '''@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@

Change Request No       CHG5772410
CR Status               Implement
Change Order Type       normal
Change Order Sub Type   null
Change Model            
Scheduled Start Date    2025-09-02 10:10:00
Scheduled End Date      2025-09-02 13:10:00
Implementing Group      CTO-HAP-OpenTelemetry
Current Timestamp       2025-09-02 11:48:16 BST

4.1   CR has valid Change Order Type   : Success
4.1.1 CR has valid Change Model Type   : Success
4.2   CR has valid Category            : Success
4.7   CR is not On-HOLD Status         : Success
4.8   CR status is Approved            : Success
4.10  CR Implementation Window is Valid: Success
4.11  CR is in Implement Status        : Success
4.12  Package ID Validation            : Success
4.17  HAP Deployment Service is registered to this Application in ICE : Success
5.1   Implementer access validation    : Success

*********************************** CR Validation Passed ***********************************

@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@'''
def pipelineData = new PipelineData(
    deployControlSummery: deployControlSummery,
    id: "680139af-3b04-47a9-85cb-dc8c5ee8ea72",
    component: "kong",
    purl: "pkg:docker/hsbc-11298320-ctoinfra/hapds/docker/boot-oas-ds@2.0.1_95_273f175",
    rollbackArtifactInstance: "nexus3.systems.uk.hsbc:18082&artifactRepo=docker-hsbc-internal-prd_n3p&checksum=sha256:35854e26653166309a43e2fd217dd32c9c0345b90dce2f88eb89260ead59278&rollbackchecksum",
    config: "[https://nexus304.systems.uk.hsbc:8081/nexus/repository/maven-hsbc-internal-prod_n3p/com/hsbc/hap/deployment/service/values_configs/boot-oas-ds-test/0.0.5/boot-oas-ds-test-0.0.5.zip&rollbackconfig=](https://nexus304.systems.uk.hsbc:8081/nexus/repository/maven-hsbc-internal-prod_n3p/com/hsbc/hap/deployment/service/values_configs/boot-oas-ds-test/0.0.5/boot-oas-ds-test-0.0.5.zip&rollbackconfig=)",
    eimIds: ["12409291"],
    version: "2.0.1_95_273f175",
    tags: ["deploy"]
)

println "Step ${step++}: Create job parameters..."
def jobParam = new JobParam(
    pipelineGITBranch: "Release-3.2.6",
    targetEnvironment: "PRE-PROD",
    configurationYMLPath: "[https://nexus304.systems.uk.hsbc:8081/nexus/repository/maven-hsbc-internal-prod_n3p/com/hsbc/hap/deployment/service/kong_configs/boot-oas-ds-kong-deploy-prd/0.0.2/boot-oas-ds-kong-deploy-prd-0.0.2.zip](https://nexus304.systems.uk.hsbc:8081/nexus/repository/maven-hsbc-internal-prod_n3p/com/hsbc/hap/deployment/service/kong_configs/boot-oas-ds-kong-deploy-prd/0.0.2/boot-oas-ds-kong-deploy-prd-0.0.2.zip)",
    snowCR: "CHG5772410",
    verboseLogging: true
)

println "Step ${step++}: Create job object (with error details)..."
def errMsg = '''CR validation failed with below control checks:
[Change Request No CHG5772410, CR Status New, Change Order Type normal, Change Order Sub Type null, Change Model Scheduled Start Date 2025-09-02 08:30:00, Scheduled End Date 2025-09-02 11:30:00, Application CTO-HAP-OpenTelemetry, Current Timestamp 2025-09-02 09:56:38 BST, 
4.1 CR has valid Change Order Type: Success,
4.2 CR has valid Change Model Type: Success,
4.3 CR has valid Category Success,
4.7 CR is not On-HOLD Status: Success,
4.8 CR status is Approved is Failed,
4.10 CR Implementation Window is Valid: Success,
4.11 CR is in Implement Status: Failed,
4.12 Package ID Validation: Success,
4.17 HAP Deployment Service is registered to this Application in ICE: Success,
5.1 Implementer access validation: Failed, ******** CR Validation Failed !!! ]'''
def jobError = new JobError(errorCode: "ERR102", errorMessage: errMsg)

def job = new Job(
    buildNumber: "9",
    buildUser: "Prajwal Dongare (45456560)",
    buildURL: "[https://alm-jenkins207.hc.cloud.uk.hsbc:8706/job/HAP_Kong/job/HAP-CTO-000/job/PRD/job/hap-deployment-service-KONG-PROD/9/](https://alm-jenkins207.hc.cloud.uk.hsbc:8706/job/HAP_Kong/job/HAP-CTO-000/job/PRD/job/hap-deployment-service-KONG-PROD/9/)",
    error: [jobError],
    buildEnd: "2025-09-02 09:56:45.499",
    buildStatus: "Failed",
    buildStart: "2025-09-02 09:56:16.830"
)

println "Step ${step++}: Create centralEnvironmentRegister..."
def central = new CentralEnvironmentRegister(
    success: true,
    errors: [],
    workspace: "HAP-HK-CTO-000-PRD",
    cp_admin_api_url: "[https://kcphk-prod.hsbc-11383538-kongcphk-prod.prod.gcp.cloud.hk.hsbc](https://kcphk-prod.hsbc-11383538-kongcphk-prod.prod.gcp.cloud.hk.hsbc)",
    mandatoryPlugins: ["rate-limiting"],
    dp_host_url: "kdp2hk-prod.ikp401.cloud.hk.hsbc",
    gbgf: "CTO",
    dmz_Lb: "hap-api-gw-stk.hk.hsbc, hap-api-gw-sx.hk.hsbc"
)

// -------------------------------
// 3) Assemble top-level payload (preserve key order for readability)
// -------------------------------
println "Step ${step++}: Assemble payload map..."
def payloadMap = [
    configuration: configuration.toMap(),
    pipelineData: pipelineData.toMap(),
    jobParam: jobParam.toMap(),
    job: job.toMap(),
    centralEnvironmentRegister: central.toMap()
]

// -------------------------------
// 4) Serialize to JSON and print to console
// -------------------------------
println "Step ${step++}: Serializing to pretty JSON and printing to console...\n"
def prettyJson = JsonOutput.prettyPrint(JsonOutput.toJson(payloadMap))
println prettyJson

println "\nDone."

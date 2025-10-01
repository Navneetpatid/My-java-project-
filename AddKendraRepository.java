import groovy.json.JsonOutput
import groovy.transform.Field

@Field def logger = new Logger()

def call() {
    node {
        try {
            stage('Load Hardcoded Config') {
                def config = getDeploymentConfig()

                logger.info("Workspace: ${config.configuration.workspace}")
                logger.info("Action: ${config.configuration.action}")
                logger.info("Pipeline Branch: ${config.jobParam.pipelineGITBranch}")
                logger.info("Build User: ${config.job.buildUser}")
                logger.info("Central Env API URL: ${config.centralEnvironmentRegister.cp_admin_api_url}")

                // Print full JSON in logs (optional)
                println "======== FULL CONFIG ========"
                println JsonOutput.prettyPrint(JsonOutput.toJson(config))
            }
        } catch (Exception e) {
            logger.error("Error loading config: ${e.message}")
            throw e
        }
    }
}

/**
 * Hardcoded deployment config as Map
 */
def getDeploymentConfig() {
    return [
        configuration: [
            hapEngagementID      : "HAP-CTO-000",
            emailDistributionList: "chetan.radke@hsbc.co.in",
            workspace            : "HAP-HK-CTO-000-PRD",
            deployToDmz          : "disable",
            oasFilePath          : "./oas-PRD.yaml",
            action               : "deploy",
            plugins              : [
                [
                    name   : "rate-limiting",
                    enabled: true,
                    config : [
                        second            : 5,
                        hour              : 10000,
                        policy            : "local",
                        fault_tolerant    : true,
                        hide_client_headers: false
                    ]
                ]
            ]
        ],
        pipelineData: [
            deployControlSummery: "CR Validation Passed",
            id                  : "680139af-3b04-47a9-85cb-dc8c5ee8ea72",
            component           : "kong",
            purl                : "pkg:docker/hsbc-11298320-ctoinfra/hapds/docker/boot-oas-ds@2.0.1_95_273f175",
            rollbackArtifactInstance: "nexus3.systems.uk.hsbc:18082...",
            config              : "https://nexus304.systems.uk.hsbc:8081/.../boot-oas-ds-test-0.0.5.zip",
            eimIds              : ["12409291"],
            version             : "2.0.1_95_273f175",
            tags                : ["deploy"]
        ],
        jobParam: [
            pipelineGITBranch     : "Release-3.2.6",
            targetEnvironment     : "PRE-PROD",
            configurationYMLPath  : "https://nexus304.systems.uk.hsbc:8081/.../boot-oas-ds-kong-deploy-prd-0.0.2.zip",
            snowCR                : "CHG5772410",
            verboseLogging        : true
        ],
        job: [
            buildNumber : "9",
            buildUser   : "Prajwal Dongare (45456560)",
            buildURL    : "https://alm-jenkins207.hc.cloud.uk.hsbc:8706/job/HAP_Kong/job/HAP-CTO-000/job/PRD/job/hap-deployment-service-KONG-PROD/9/",
            error       : [[
                errorCode   : "ERR102",
                errorMessage: "CR validation failed with some checks"
            ]],
            buildStart  : "2025-09-02 09:56:16.830",
            buildEnd    : "2025-09-02 09:56:45.499",
            buildStatus : "Failed"
        ],
        centralEnvironmentRegister: [
            success : true,
            errors  : [],
            workspace: "HAP-HK-CTO-000-PRD",
            cp_admin_api_url: "https://kcphk-prod.hsbc-11383538-kongcphk-prod.prod.gcp.cloud.hk.hsbc",
            mandatoryPlugins: ["rate-limiting"],
            dp_host_url     : "kdp2hk-prod.ikp401.cloud.hk.hsbc",
            gbgf            : "CTO",
            dmz_Lb          : "hap-api-gw-stk.hk.hsbc, hap-api-gw-sx.hk.hsbc"
        ]
    ]
      }

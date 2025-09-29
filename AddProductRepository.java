{
  "configuration": {
    "hapEngagementID": "HAP-CTO-000",
    "emailDistributionList": "chetan.radke@hsbc.co.in",
    "workspace": "HAP-HK-CTO-000-PRD",
    "deployToDmz": "disable",
    "oasFilePath": "./oas-PRD.yaml",
    "action": "deploy",
    "plugins": [
      {
        "name": "rate-limiting",
        "enabled": true,
        "config": {
          "second": 5,
          "hour": 10000,
          "policy": "local",
          "fault_tolerant": true,
          "hide_client_headers": false
        }
      }
    ]
  },
  "pipelineData": {
    "deployControlSummery": "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@\n\nChange Request No       CHG5772410\nCR Status               Implement\nChange Order Type       normal\nChange Order Sub Type   null\nChange Model            \nScheduled Start Date    2025-09-02 10:10:00\nScheduled End Date      2025-09-02 13:10:00\nImplementing Group      CTO-HAP-OpenTelemetry\nCurrent Timestamp       2025-09-02 11:48:16 BST\n\n4.1   CR has valid Change Order Type   : Success\n4.1.1 CR has valid Change Model Type   : Success\n4.2   CR has valid Category            : Success\n4.7   CR is not On-HOLD Status         : Success\n4.8   CR status is Approved            : Success\n4.10  CR Implementation Window is Valid: Success\n4.11  CR is in Implement Status        : Success\n4.12  Package ID Validation            : Success\n4.17  HAP Deployment Service is registered to this Application in ICE : Success\n5.1   Implementer access validation    : Success\n\n*********************************** CR Validation Passed ***********************************\n\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@",
    "id": "680139af-3b04-47a9-85cb-dc8c5ee8ea72",
    "component": "kong",
    "purl": "pkg:docker/hsbc-11298320-ctoinfra/hapds/docker/boot-oas-ds@2.0.1_95_273f175",
    "rollbackArtifactInstance": "nexus3.systems.uk.hsbc:18082&artifactRepo=docker-hsbc-internal-prd_n3p&checksum=sha256:35854e26653166309a43e2fd217dd32c9c0345b90dce2f88eb89260ead59278&rollbackchecksum",
    "config": "https://nexus304.systems.uk.hsbc:8081/nexus/repository/maven-hsbc-internal-prod_n3p/com/hsbc/hap/deployment/service/values_configs/boot-oas-ds-test/0.0.5/boot-oas-ds-test-0.0.5.zip&rollbackconfig=",
    "eimIds": [
      "12409291"
    ],
    "version": "2.0.1_95_273f175",
    "tags": [
      "deploy"
    ]
  }

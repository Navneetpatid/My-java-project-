package com.hsbc.hap

class Configuration {

    // === Top-level configuration fields ===
    String hapEngagementID
    String emailDistributionList
    String workspace
    String deployToDmz
    String oasFilePath
    String action

    // === Plugin details ===
    String pluginName
    boolean pluginEnabled
    int pluginConfigSecond
    int pluginConfigHour
    String pluginConfigPolicy
    boolean pluginConfigFaultTolerant
    boolean pluginConfigHideClientHeaders

    String toString() {
        return """{
  "configuration": {
    "hapEngagementID": "${hapEngagementID}",
    "emailDistributionList": "${emailDistributionList}",
    "workspace": "${workspace}",
    "deployToDmz": "${deployToDmz}",
    "oasFilePath": "${oasFilePath}",
    "action": "${action}",
    "plugins": [
      {
        "name": "${pluginName}",
        "enabled": ${pluginEnabled},
        "config": {
          "second": ${pluginConfigSecond},
          "hour": ${pluginConfigHour},
          "policy": "${pluginConfigPolicy}",
          "fault_tolerant": ${pluginConfigFaultTolerant},
          "hide_client_headers": ${pluginConfigHideClientHeaders}
        }
      }
    ]
  }
}"""
    }
    }

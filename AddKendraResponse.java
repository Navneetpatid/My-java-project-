package com.hsbc.hap

class Configuration {
    String hapEngagementID
    String emailDistributionList
    String workspace
    String deployToDmz
    String oasFilePath
    String action
    String pluginName
    boolean pluginEnabled
    int pluginConfigSecond
    int pluginConfigHour
    String pluginConfigPolicy
    boolean pluginConfigFaultTolerant
    boolean pluginConfigHideClientHeaders

    @Override
    String toString() {
        return """[Configuration] 
        hapEngagementID: ${hapEngagementID}, 
        emailDistributionList: ${emailDistributionList}, 
        workspace: ${workspace}, 
        deployToDmz: ${deployToDmz}, 
        oasFilePath: ${oasFilePath}, 
        action: ${action}, 
        pluginName: ${pluginName}, 
        pluginEnabled: ${pluginEnabled}, 
        pluginConfigSecond: ${pluginConfigSecond}, 
        pluginConfigHour: ${pluginConfigHour}, 
        pluginConfigPolicy: ${pluginConfigPolicy}, 
        pluginConfigFaultTolerant: ${pluginConfigFaultTolerant}, 
        pluginConfigHideClientHeaders: ${pluginConfigHideClientHeaders}
        """
    }
          }

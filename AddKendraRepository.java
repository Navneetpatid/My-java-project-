//Fetch Mandate Plugin - List 1
for (i = 0; i < responseJson.mandatoryPlugins.size(); i++) {
    mandatePluginList.add(responseJson.mandatoryPlugins[i].toLowerCase())
}

//check MTLS plugin rules // this needs to be variablized
if (mandatePluginList.contains("wsit-secure-access-eb2b-mtls-authn-v2-0")) {
    isDMZWithMTLSEnabled = true

    // ==== Similar pattern as line 1168 ====
    miEvent.centralEnvironmentRegister.mandatePluginList = mandatePluginList
    miEvent.infraData.mandatePluginList = mandatePluginList

    if (adminVerboseLogging) {
        logger.info("Mandate Plugin List Assigned: ${mandatePluginList}")
    }
    // =======================================
}

logger.info("Mandate Plugin - List 1 : " + mandatePluginList)
logger.info("Configuration Yaml Plugin - List 2 : " + configurationYMLPluginList)

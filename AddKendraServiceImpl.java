package com.hsbc.hap

class CentralEnvironmentRegister {
    Boolean success
    String errors
    List<String> workspace
    String cp_admin_api_url
    String prod_gcp_cloud_url
    List<String> mandatoryPlugins
    String dp_host_url
    String gbgf
    String dmzLb
    String logs

    @Override
    String toString() {
        return """[CentralEnvironmentRegister] 
                  success: ${success}, 
                  errors: ${errors}, 
                  workspace: ${workspace}, 
                  cp_admin_api_url: ${cp_admin_api_url}, 
                  prod_gcp_cloud_url: ${prod_gcp_cloud_url}, 
                  mandatoryPlugins: ${mandatoryPlugins}, 
                  dp_host_url: ${dp_host_url}, 
                  gbgf: ${gbgf}, 
                  dmzLb: ${dmzLb}, 
                  logs: ${logs}
               """
    }
            }

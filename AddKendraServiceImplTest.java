package com.hsbc.hap

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

    @Override
    String toString() {
        return """[PipelineData]
        deployControlSummery      : ${deployControlSummery}
        id                        : ${id}
        component                 : ${component}
        purl                      : ${purl}
        rollbackArtifactInstance  : ${rollbackArtifactInstance}
        config                    : ${config}
        eimIds                    : ${eimIds}
        version                   : ${version}
        tags                      : ${tags}
        """
    }
    }

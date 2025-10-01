package com.hsbc.hap

class MIEvent {
    Job job = new Job()
    JobParam jobParam = new JobParam()
    CER cer = new CER()
    App app = new App()
    Configuration configuration = new Configuration()
    PipelineData pipelineData = new PipelineData()
    CentralEnvironmentRegistry centralEnvironmentRegistry = new CentralEnvironmentRegistry()

    String toString() {
        return """[MIEvent]
        job              : ${job},
        jobParam         : ${jobParam},
        cer              : ${cer},
        app              : ${app},
        configuration    : ${configuration},
        pipelineData     : ${pipelineData},
        centralEnvReg    : ${centralEnvironmentRegistry}
        """
    }
            }

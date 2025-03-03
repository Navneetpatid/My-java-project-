def call(Map config) {
    this.config = config
    node(gcrNode) {
        logger = new Logger()
        echo "sandip starts"
        
        // pushMiEventToSplunk = new PushMiEventToSplunk(logger: logger)
        miEvent = new MiEvent()
        splunkRequestBody = new SplunkRequestBody()
        
        stage('Checkout') {
            try {
                cleanWs()
                gitCheckout()
                sh '''
                    >> email_data.txt
                    ls
                    echo "DATE ENVIRONMENT CP DATAPLANE STATUS" >> email_data.txt
                '''
                
                def date = new Date()
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy")
                rundate = sdf.format(date)
                echo "rundate:${rundate}"
                
                miEvent.Environment = "ENVIRONMENT"
                miEvent.CP = "CP"
                miEvent.Status = "STATUS"
                miEvent.Dataplane = "DATAPLANE"
                miEvent.Date = "DATE"
                
                splunkRequestBody.event = miEvent
                splunkRequestBody.sourcetype = sourceType
                
                // pushMiEventToSplunk.pushToSplunk(splunkRequestBody)
                
                def environment = "dev-HK,dev-UK,ppd-HK,ppd-UK,prd-HK,prd-UK"
                // def environment = "dev-HK,dev-UK"
                // def environment = "dev-HK"
                
                def DP_CTO, DP_ET, DP_GDT, DP_SHARED, CP
                buildno = env.BUILD_NUMBER
                echo "buildno:${buildno}"
                
                try {
                    enlist = "${environment}".split(',') as List
                } catch (Exception e) {
                    error("Error while fetching gcloud output response: " + e.getMessage())
                }
                
                stage("Report Generation Process") {
                    try {
                        def counter = 0
                        def counterworkspace = 0
                        echo "pass workspace name ${workspace_name}"
                        
                        for (def j in enlist) {
                            String DataPlanename = "GKE-GCP"
                            def configDetailsGKEYaml = readYaml text: libraryResource("GKE.yaml")
                            
                            def config_detail_GKE = libraryResource '../resources/GKE.yaml'
                            writeFile file: 'GKE.yaml', text: config_detail_GKE
                            def configurationGKEYML = readYaml file: "resources/GKE.yaml"

                            ENV_TYPE = "${j}".trim()
                            echo "${ENV_TYPE}"

                            def configGKEYML = configurationGKEYML."${ENV_TYPE}"
                            configGKEYMLAll = configGKEYML

                            echo "printing ${configurationGKEYML}"
                            echo "Printing configIKPYML: ${configGKEYML}"
                            echo "Printing configGKEYMLAll: ${configGKEYMLAll}"

                            CP = configGKEYML.CP
                            DP_SHARED = configGKEYML.DP_Shared

                            if (j.equals("sbox-HK")) {
                                logger.info("********** if Get getPlugins **************")
                            } else {
                                DP_CTO = configGKEYML.DP_cto
                                DP_ET = configGKEYML.DP_et
                                DP_GDT = configGKEYML.DP_gdt
                            }
                        }
                    } catch (Exception e) {
                        error("Error in Report Generation Process: " + e.getMessage())
                    }
                }
        }
    }
                    }

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
                        }
                    } catch (Exception e) {
                        error("Error in Report Generation Process: " + e.getMessage())
                    }
                }
            } catch (Exception e) {
                echo "Error during checkout: ${e.getMessage()}"
            }
        }
    }
                    }

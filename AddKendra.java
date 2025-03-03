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
                            
                    def dplist1 = ["${DP_SHARED}", "${DP_CTO}", "${DP_ET}", "${DP_GDT}"]

                if (counter < 5000) {
                    logger.info("********** igetworkspaces started main ************** - ${CP}")
                    counter = counter + 1;

                    def errList = [];
                    logger.info("********** testAuth Calling ************** ${counter}")
                    this.testAuth(CP)

                    // logger.info("********** getworkspaces ended ************** ${CP}")
                    
                    if (workspace_name == "ALL") {
                    if (contentFile == "")
                        contentFile = "Workspace Name, Service Count, CP\n"

                    logger.info("********** igetworkspaces started ************** - ${CP}")
                    WorkSpacesList.clear();
                    getworkspaces(CP)

                    for (i = 0; i < WorkSpacesList.size(); i++) {
                        if (WorkSpacesList[i].contains("-")) {
                            try {
                                counterworkspace = counterworkspace + 1;
                                echo("Writing for workspace " + WorkSpacesList[i] + " ${i} counterworkspace ${counterworkspace}total workspace "+workSpacesList.size())

                                def servicecount = this.getworkspaceservices(CP, "${WorkSpacesList[i]}")
                                
                            contentFile = contentFile + WorkSpacesList[i] + "," + servicecount + "," + "CP" + "\n";

} catch (Exception e) {
    error("Error while fetching gcloud output response: " + e.getMessage());                 
        }
        
                        }
                    }
                    }else{
if (contentFile == "") {
    contentFile = "Workspace Name,Service Count,Service Name, CP\n";
}

logger.info("********** tempworkspacename started ********** - ${CP}");
echo "get licence info";
getCPLicenceinfo("${CP}", "${j}");
                    }
                                }
                            }
                        }
                    } catch (Exception e){
                        error ("error while fetching gcloud output response+e massage ())
                            }
                    String filename = "Report_Detail.csv"

echo "filename for creating writing 1: ${filename}"
echo "filename for contentFile: ${contentFile}"

writeFile file: "./Report/${filename}", text: "${contentFile}"
echo "writeFile executed successfully - send mail list ${email_reciver}"

String responseBody = readFile encoding: 'UTF-8', file: "./Report/${filename}"
echo "responseBody: ${responseBody}"
echo "readFile executed successfully"

String tempworkspacename1 = "${workspace_name}"
tempworkspacename1 = tempworkspacename1.replace("-DEV", "")

echo "Sending Email to Recipients Report/${filename}"

String message = "Hi Team,\n\nThis is an auto-generated mail for ${tempworkspacename1}.\n"
message += "Please find the attached report for Workspace, Services, Routes, and Plugins.\n"
message += "For any queries, please reach out to the team.\n"
message += "\n\nThanks\n"

emailext to: "${email_reciver}",
    subject: "Admin API Pipeline Report - ${tempworkspacename1}",
    attachLog: false,
    attachmentsPattern: "Report/${filename}",
    body: message,
    }
            }
        }

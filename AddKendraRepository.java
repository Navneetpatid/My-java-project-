const { client } = require("pg");

const params = ['hapEngagementID', 'workspace'];
const args = process.argv;

var lib_details = {};
lib_details = {
    "success": "",
    "errors": "",
    "workspace": "",
    "cp_url": "",
    "mandatoryPlugins": "",
    "dpHost": "",
    "gbgf": "",
    "dmz_lb": "",
    "logs": ""
}

var Print_data = "HAP Database Validation Started";
lib_details.logs = Print_data;

//console.log(process.argv.length);
if (process.argv.length < 4) {
    console.error('Expected two argument like HAP-CTO-001 HAP-HK-CTO-001-DEV');
    var argument_print_data = "HAP Database Validation failed for commandline argument";
    lib_details.errors = argument_print_data;
    lib_details.logs = argument_print_data;
    console.log(lib_details);
    process.exit(1);
} 
else {
    var a1 = args[2].toString();
    var a2 = args[3].toString();
    //console.log('HAP Engagement ID    : ', a1);
    //console.log('HAP Workspace        : ', a2);

    var str = a2;
    var arr = str.split("-");

    var a3 = arr[1].toString(); // Region
    lib_details.region = a3;
    var a4 = arr[4].toString(); // Env
    //lib_details.environment = a4;
        }
if (process.argv.length > 4) {
    //console.error('Expected two argument like HAP-CTO-001 HAP-HK-CTO-001-DEV');
    var argument_print_data = "HAP Database Validation failed for commandline argument";
    lib_details.errors = argument_print_data;
    lib_details.logs = argument_print_data;
    console.log(lib_details);
    process.exit(1);
}

const client = new Client({
    user: 'DATABASE_USER',
    host: 'GKE_PROXY_HOST_IP',
    database: 'DB_NAME',
    port: 'PORT'
});

const validateWorkspaceForEngagement = async () => {
    try {
        client
            .connect()
            .then(() => Print_data = "Connected to Database successfully !!")
            .catch(err => Print_data = "Database Connection error !!");

        const res = await client.query(`select engagement_id from engagement_target where engagement_id = '${a1}'`);
        var DB_Conn = "true";
        lib_details.success = DB_Conn;
lib_details.logs = lib_details.logs + ' | ' + Print_data;

// If Rows found
console.log(res.rows);

if (res.rows[0]) {
    if (res.rows[0].engagement_id == a1) {
        // console.log('Engagement id', a1, 'validated');
        lib_details.logs = lib_details.logs + ' | ' + `engagement_id ${a1} validated`;

        const res_gbgf = await client.query(`select gbgf from engagement_target where engagement_id = '${a1}'`);

        if (res_gbgf.rows[0]) {
            gbgf_data = res_gbgf.rows[0].gbgf;
            lib_details.gbgf = gbgf_data;
        } else {
            // console.log('Error : GBGF data not found');
            Print_data = `Error : GBGF data not found for engagement_id ${a1}`;
            lib_details.logs = lib_details.logs + ' | ' + Print_data;
            client.end();

            Print_data = "Database Connection closed successfully !!";
            lib_details.logs = lib_details.logs + ' | ' + Print_data;
            console.log(lib_details);
            process.exit();
        }
}
    }else {  
    //console.log('engagement_id', a1, 'not validated');  
    client.end();  
    Print_data = `engagement_id ${a1} not validated`  
    lib_details.logs = lib_details.logs + ' | ' + Print_data;  
    lib_details.errors = Print_data;  
    Print_data = "Database Connection closed successfully !!"  
    lib_details.logs = lib_details.logs + ' | ' + Print_data;  
    console.log(lib_details)  
    process.exit();  
}  

//Getting ALL ws name for ENGID  
const ws_name = await client.query(`select workspace from workspace_target where engagement_id = '${a1}';`)  
//If Rows found console.log(res1.rows)  
//console.log(ws_name.rows)  
if (ws_name.rows) {  
    var ws_array = [];  
    for(var i = 0; i < ws_name.rows.length; i++)  
    {  
        var ws_a = ws_name.rows[i].workspace  
        ws_array.push(ws_a);  
    }  
    //console.log(ws_array);  
    lib_details.workspace = ws_array;  
            }
else {  
    console.log("No Workspace mapped for this EngID");  
}  

const res1 = await client.query(`select workspace from workspace_target where engagement_id = '${a1}' and workspace = '${a2}';`)  
//If rows found console.log(res1.rows)  
//console.log(res1.rows)  

if (res1.rows[0].workspace == a2) {  
    console.log('workspace', a2, 'validated');  
    lib_details.logs = lib_details.logs + ' | ' + `workspace ${a2} validated`;  

    //Getting DP host details  
    const res2 = await client.query(`select dp_host_url from workspace_target where engagement_id = '${a1}' and workspace = '${a2}';`)  

    if (res2.rows[0].dp_host_url) {  
        DP_Host_Data = res2.rows[0].dp_host_url  
        //console.log('DP Host :', DP_Host_Data);  
        lib_details.logs = lib_details.logs + ' | ' + `DP_Host : ${DP_Host_Data}`;  
        lib_details.dpHost = DP_Host_Data;  
    }  
    else {  
        console.log('Error : DP Host not found');  
        client.end();  
        console.log("Database Connection closed successfully !!")  
        console.log(lib_details);  
            process.exit();
    }  
}
}
else {  
    Print_data = `workspace ${a2} not validated`  
    lib_details.logs = lib_details.logs + ' | ' + Print_data;  
    lib_details.errors = Print_data;  
    client.end();  
    Print_data = "Database Connection closed successfully !!"  
    lib_details.logs = lib_details.logs + ' | ' + Print_data;  
    console.log(lib_details)  
    process.exit();  
}  

// Getting Mandatory Plugin  
const res3 = await client.query(`select mandatory_plugin from engagement_plugin where engagement_id = '${a1}';`)  

if (res3.rows) {  
    var res3_mand_plugin = [];  
    for(var plugin_list = 0; plugin_list < res3.rows.length; plugin_list++)  
    {  
        var mand_plugin_a = res3.rows[plugin_list].mandatory_plugin  
        res3_mand_plugin.push(mand_plugin_a);  
    }  

    lib_details.mandatoryPlugins = res3_mand_plugin;  
    lib_details.logs = lib_details.logs + ' | ' + `mandatoryPlugins : ${res3_mand_plugin}`  
}  
else {  
    console.log('No Mandatory plugin found ');
            }
        //geting cp_url
                                const res4 = await client.query(`select cp_admin_api_url from cp_master where (region, environment) IN  
    (select a.region, b.environment from engagement_target a join workspace_target b on a.engagement_id = b.engagement_id where a.engagement_id = '${a1}'and b.workspace =${a2}');)  
`);  

//console.log(res4.rows)  
if (res4.rows[0].cp_admin_api_url) {  
    var cp_url_data = res4.rows[0].cp_admin_api_url  
    //console.log('workspace', a2, 'validated');  
    lib_details.logs = lib_details.logs + ' | ' + `Received CP_ADMIN URL: ${cp_url_data}`;  
    lib_details.cp_url = cp_url_data;  
}  
else {  
    console.log('CP_ADMIN URL not found');  
    client.end();  
    Print_data = `CP_ADMIN URL not found for ${a2}`  
    lib_details.logs = lib_details.logs + ' | ' + Print_data;  
    lib_details.errors = Print_data;  
    Print_data = `CP_ADMIN URL not found for ${a2}`  
    lib_details.logs = lib_details.logs + ' | ' + Print_data;  
    console.log(lib_details)  
    process.exit();  
}
// Getting dmz lb  
const res5 = await client.query(`select load_balancer from dmz_lb_master where environment = '${a4}' AND region = '${a3}';`)  

//console.log(res4.rows)  
if (res5.rows[0].load_balancer) {  
    var load_balancer_data = res5.rows[0].load_balancer  
    lib_details.logs = lib_details.logs + ' | ' + `Received DMZ Load Balancer: ${load_balancer_data}`;  
    lib_details.lb = load_balancer_data;  
}  
else {  
    console.log('DMZ Load Balancer not found');  
    client.end();  
    Print_data = `DMZ Load balancer not found for ${a4} ${a3}`  
    lib_details.logs = lib_details.logs + ' | ' + Print_data;  
    lib_details.errors = Print_data;  
    console.log(lib_details);  
    process.exit();  
}} catch (error) {  
    //console.error("Failed to connect to DB");  
    Print_data = error;  
    lib_details.errors = Print_data;  
    lib_details.logs = lib_details.logs + ' | ' + Print_data;  
    //console.log(lib_details)  
    //console.log(error)  
}  
finally {  
    client.end();  
    console.log(lib_details);  
    //console.log("Database Connection closed sucessfully !!")  
}  

//Calling the main function  
validateWorkspaceForEngagement();

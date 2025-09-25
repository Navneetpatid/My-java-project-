import groovy.util.*
import groovy.json.*
import groovy.lang.*

@Field def logger = new Logger()

def call() {
    node {
        try {
            stage('Send Email') {
                def emailBody = """\
<h3>Pipeline Execution Details</h3>
<ul>
  <li><b>ENGAGEMENT_ID:</b> ${params.engagementid}</li>
  <li><b>MANDATORY_PLUGIN:</b> ${params.emid}</li>
  <li><b>REGION:</b> ${params.URL}</li>
</ul>

<h3>AAP Execution Using HAP Deployment Service</h3>
<p>Please use the below package ID PURL in the Release Task:</p>
<table border="1" style="border-collapse: collapse; width: 100%;">
  <tr>
    <th style="padding: 8px; text-align: left;">Field</th>
    <th style="padding: 8px; text-align: left;">Value</th>
  </tr>
  <tr>
    <td style="padding: 8px;">PURL</td>
    <td style="padding: 8px;">${params.purl}</td>
  </tr>
</table>

<h3>Use the below values while running the Production Pipeline:</h3>
<table border="1" style="border-collapse: collapse; width: 100%;">
  <tr>
    <th style="padding: 8px; text-align: left;">Field</th>
    <th style="padding: 8px; text-align: left;">Value</th>
  </tr>
  <tr>
    <td style="padding: 8px;">Release Nexus Version</td>
    <td style="padding: 8px;">${params.releaseNexusVersionUrl}</td>
  </tr>
  <tr>
    <td style="padding: 8px;">Rollback Nexus Version</td>
    <td style="padding: 8px;">${params.rollbackNexusVersionUrl}</td>
  </tr>
</table>

<p>Please contact HAP SUPPORT at 
<a href="mailto:hap.support@hsbc.co.in">hap.support@hsbc.co.in</a> 
for any query.</p>

<p>Refer to the following link for more help: 
<a href="https://alm-confluence.systems.uk.hsbc/confluence/display/ENGP/HAP+Deployment+Service+-+CD+Pre-Prod+and+Prod">
HAP Deployment Service Documentation</a></p>
"""

                emailext(
                    to: "${params.Email_Id}",
                    subject: "Pipeline Execution Report : ${params.engagementid}",
                    body: emailBody,
                    mimeType: 'text/html'
                )

                logger.info("Email sent successfully to ${params.Email_Id}")
            }
        } catch (Exception e) {
            logger.error("Error while running TestPipeline: ${e.message}")
            error("Pipeline failed: ${e.message}")
        }
    }
}

def splitUrl(String url) {
    return url.tokenize("/")
}

def splitemid(String emid) {
    return emid.tokenize("-")
                    }

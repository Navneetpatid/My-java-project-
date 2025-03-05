import groovy.json.JsonSlurper

def filename = "${filename}_license.json"  // Use the same filename as in writeFile

// Read the file
def file = new File(filename)

if (file.exists()) {
    def fileContents = file.text
    println "File Contents:\n${fileContents}"

    // Parse JSON
    def jsonSlurper = new JsonSlurper()
    def jsonResponse = jsonSlurper.parseText(fileContents)

    println "Parsed JSON Data:"
    println jsonResponse

    // If JSON contains data array, iterate over it
    if (jsonResponse?.data) {
        jsonResponse.data.each { service ->
            println "Service Name: ${service.name}"
            println "Service ID: ${service.id}"
        }
    }
} else {
    println "File not found: ${filename}"
}

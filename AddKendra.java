import java.net.HttpURLConnection
import java.net.URL

// Read URLs from file
def file = new File("urls.txt")
def urls = file.readLines()

def results = []

urls.each { url ->
    try {
        URL site = new URL(url)
        HttpURLConnection connection = (HttpURLConnection) site.openConnection()
        connection.setRequestMethod("GET")
        connection.setConnectTimeout(5000)
        connection.connect()
        int statusCode = connection.getResponseCode()
        results << "$url - Status: $statusCode"
    } catch (Exception e) {
        results << "$url - ERROR: ${e.message}"
    }
}

// Write results to a file
new File("health_results.txt").text = results.join("\n")

println "Health check complete. Results saved in health_results.txt"

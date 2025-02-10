import java.nio.file.Files
import java.nio.file.Paths

def csvFilePath = "output.csv"  // CSV file name
Files.write(Paths.get(csvFilePath), myFinalOutput.getBytes("UTF-8"))
println "CSV file saved as: ${csvFilePath}"

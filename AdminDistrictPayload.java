from selenium import webdriver
from helper_methods.perform_cr_test import capture_change_request

# Initialize WebDriver
driver = webdriver.Chrome()  # Ensure chromedriver.exe is in the same directory or in PATH

# Define inputs
CR_number = "CHANGE_REQUEST_123"
screenshot_path = "C:/path/to/screenshots"  # Change this to an actual path

# Call the function
capture_change_request(driver, CR_number, screenshot_path)

# Close the driver
driver.quit()

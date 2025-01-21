import os
import requests
import pandas as pd
from sqlalchemy import create_engine

# Load credentials from environment variables
API_KEY = os.getenv("API_KEY")
DB_USERNAME = os.getenv("DB_USERNAME")
DB_PASSWORD = os.getenv("DB_PASSWORD")
DB_HOST = os.getenv("DB_HOST")
DB_NAME = os.getenv("DB_NAME")

def fetch_data(api_url):
    """Fetch data from an API."""
    headers = {"Authorization": f"Bearer {API_KEY}"}
    response = requests.get(api_url, headers=headers)
    response.raise_for_status()  # Raise an error for bad status codes
    return response.json()

def process_data(data):
    """Process raw data into a DataFrame."""
    df = pd.DataFrame(data)
    # Example: Filter and transform data
    df = df[df["active"]]
    df["created_at"] = pd.to_datetime(df["created_at"])
    return df

def save_to_database(df, table_name):
    """Save the DataFrame to a database."""
    db_url = f"postgresql://{DB_USERNAME}:{DB_PASSWORD}@{DB_HOST}/{DB_NAME}"
    engine = create_engine(db_url)
    df.to_sql(table_name, engine, if_exists="replace", index=False)

def main():
    """Main pipeline function."""
    api_url = "https://api.example.com/data"
    data = fetch_data(api_url)
    processed_data = process_data(data)
    save_to_database(processed_data, "processed_table")
    print("Pipeline executed successfully!")

if __name__ == "__main__":
    main()

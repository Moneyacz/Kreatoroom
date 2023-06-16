import logging
from os import path
import pandas as pd
import yaml
from google.cloud import storage

logger = logging.getLogger(__name__)
project_path = path.dirname(path.dirname(path.abspath(__file__)))

# Initialize a Google Cloud Storage client
storage_client = storage.Client()


def read_csv(input_data_path):
    """
    read csv to pandas DataFrame
    Args:
        input_data_path: (String)

    Returns:(DataFrame)

    """
    try:
        # Load data from GCS
        bucket_name, blob_name = input_data_path.split('/', 1)
        bucket = storage_client.get_bucket(bucket_name)
        blob = bucket.blob(blob_name)
        csv_content = blob.download_as_text()

        logger.info("Trying to load data from %s", input_data_path)
        df = pd.read_csv(pd.StringIO(csv_content), header=0, low_memory=False)
        logger.info("Successfully loaded data from {}".format(input_data_path))
        return df
    except FileNotFoundError as e1:
        logger.error('FileNotFoundError: {}'.format(e1))


def save_csv(df, output_data_path, index=False):
    """
    Save pandas DataFrame to csv
    Args:
        df: (DataFrame)
        output_data_path: (String)
        index: (Boolean) True: include index when write to csv

    """
    try:
        # Save data to GCS
        bucket_name, blob_name = output_data_path.split('/', 1)
        bucket = storage_client.get_bucket(bucket_name)
        blob = bucket.blob(blob_name)

        csv_buffer = pd.StringIO()
        df.to_csv(csv_buffer, index=index)
        blob.upload_from_string(csv_buffer.getvalue())

        logger.info("Successfully saved data to {}".format(output_data_path))
    except FileNotFoundError as e1:
        logger.error('FileNotFoundError: {}'.format(e1))
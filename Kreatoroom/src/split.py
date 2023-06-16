import logging
from os import path
from sklearn import model_selection
from google.cloud import storage
from src.helper import read_csv, save_csv, load_config

# Set up Google Cloud client
client = storage.Client.from_service_account_json('path/to/your/gcp_key.json')

logger = logging.getLogger(__name__)
project_path = path.dirname(path.dirname(path.abspath(__file__)))

def main(args):
    """
    main function to split data
    :param args: (argparse) user-input configuration file
    """
    try:

        config_path = project_path + "/" + args.config
        input_data_path = args.input
        out_train_path = args.output_train
        out_test_path = args.output_test

        config = load_config(config_path)
        df = read_gcs_csv(input_data_path)
        df_train, df_test = split(df, **config['split_data'])

        # Write to output file
        save_gcs_csv(df_train, out_train_path)
        save_gcs_csv(df_test, out_test_path)
    except ValueError as e1:
        logger.error("ValueError: " + str(e1) + " Please validate Values in the configuration file.")
    except Exception as e:
        logger.error("Unexpected error occurred when splitting data: " + str(e))


def split(df, split_data):
    """
    Split and store the training/test data
    :param df: (String) Relative local file path of the data source
    :param split_data: (Dictionary) Arguments to split the data into training and test
    :return: None
    """
    # Split data into test and train
    df_train, df_test = model_selection.train_test_split(df, **split_data)

    return [df_train, df_test]

def read_gcs_csv(file_path):
    # Replace with your bucket name and file path
    bucket_name = 'your-bucket-name'
    bucket = client.get_bucket(bucket_name)
    blob = storage.Blob(file_path, bucket)
    content = blob.download_as_text()
    return read_csv(content)

def save_gcs_csv(df, file_path):
    # Replace with your bucket name and file path
    bucket_name = 'your-bucket-name'
    bucket = client.get_bucket(bucket_name)
    blob = storage.Blob(file_path, bucket)
    content = save_csv(df)
    blob.upload_from_string(content, content_type='text/csv')
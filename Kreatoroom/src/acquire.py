import logging
from os import path
from google.cloud import storage
from google.api_core.exceptions import GoogleAPIError

logger = logging.getLogger(__name__)
project_path = path.dirname(path.dirname(path.abspath(__file__)))


def acquire(object_name, bucket_name, output):
    """

    Args:
        object_name: (String) Name of the object to download
        bucket_name: (String) GCP bucket to download from
        output: (String) path of the downloaded file

    Returns: None

    """

    data_path = project_path + "/" + output
    storage_client = storage.Client()

    try:
        logger.info("Acquiring {} from {}".format(object_name, bucket_name))
        bucket = storage_client.bucket(bucket_name)
        blob = bucket.blob(object_name)
        blob.download_to_filename(data_path)
        logging.info("Successfully downloaded data to {}".format(data_path))

    except GoogleAPIError as e1:
        logger.error("GoogleAPIError: " + str(e1))
        raise GoogleAPIError
    except Exception as e2:
        logger.error("Unexpected error occurred when acquiring data: " + str(e2))


def main(args):
    """
    main function to Acquire data
    :param args: (argparse) user-input configuration file
    """

    try:
        var = vars(args)
        var.pop('func', None)
        acquire(**var)
    except Exception as e2:
        logger.error("Unexpected error occurred when acquiring data: " + str(e2))
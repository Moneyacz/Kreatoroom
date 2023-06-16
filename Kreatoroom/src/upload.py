import logging.config
from os import path
from google.cloud import storage
from google.api_core.exceptions import GoogleAPIError

import config.config as config

logger = logging.getLogger(__name__)
project_path = path.dirname(path.dirname(path.abspath(__file__)))


def upload_file(args):
    """Upload a file to a Google Cloud Storage bucket

    Args:
        args: argparse args - should include args.file_name, args.bucket_name, args.object_name
        args.file_name: String, file to upload
        args.bucket_name: String, GCS bucket to upload into
        args.object_name: String, object name of the uploaded file
    """

    data_path = project_path + "/" + args.file_name

    # Upload the file
    storage_client = storage.Client()
    bucket = storage_client.bucket(args.bucket_name)

    try:
        blob = bucket.blob(args.object_name)
        blob.upload_from_filename(data_path)
        logging.info(f"Successfully uploaded {args.file_name} to {args.bucket_name} GCS bucket")
    except GoogleAPIError as e:
        logging.error(e)
    except FileNotFoundError as e2:
        logger.error('FileNotFoundError: {}'.format(e2))
    except Exception as e3:
        logger.error("Unexpected error occurred when uploading file to GCS: " + str(e3))
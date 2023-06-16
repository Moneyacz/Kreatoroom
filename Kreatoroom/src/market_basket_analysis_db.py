import logging.config
import os
import sqlalchemy
import pandas as pd
import config.config as config
from google.cloud import storage
from google.auth import compute_engine
from google.auth.transport.requests import Request

logger = logging.getLogger(__name__)


def add_rec(args):
    """Add new records into table.

    Args:
        args: argparse args - should include arg.method, args.file, arg.table
        args.method: (String) How to behave if the table already exists.
        args.file: (String) csv file read the records from
        arg.table: (String) table name

    Returns:None

    """
    try:
        credentials = compute_engine.Credentials.from_service_account_file(
            os.environ.get("GOOGLE_APPLICATION_CREDENTIALS"), scopes=["https://www.googleapis.com/auth/cloud-platform"]
        )
        credentials.refresh(Request())

        connection_name = os.environ.get("GCP_CONNECTION_NAME")
        db_user = os.environ.get("GCP_DB_USER")
        db_password = os.environ.get("GCP_DB_PASSWORD")
        db_name = os.environ.get("GCP_DB_NAME")

        db_socket_dir = os.environ.get("DB_SOCKET_DIR", "/cloudsql")

        SQLALCHEMY_DATABASE_URI = (
            f"postgresql://{db_user}:{db_password}@/{db_name}"
            f"?host={db_socket_dir}/{connection_name}"
        )
        engine = sqlalchemy.create_engine(SQLALCHEMY_DATABASE_URI)
        logger.info("Successfully connected to the database.")
        df = pd.read_csv(args.file)
        df.to_sql(con=engine, index_label='id', name=config.TABLE, if_exists=args.method)
        logger.info("Successfully uploaded data into prds_rec with " + args.method + " method")
    except Exception as e:
        logger.error("Unexpected error occurred when add records to database: " + str(e))
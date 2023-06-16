import logging
import pandas as pd
from os import path
import config.config as conf
from mlxtend.frequent_patterns import apriori
from mlxtend.frequent_patterns import association_rules
from src.helper import read_csv, save_csv, load_config
import numpy as np

pd.options.mode.chained_assignment = None

logger = logging.getLogger(__name__)
project_path = path.dirname(path.dirname(path.abspath(__file__)))


def main(args):
    """
    main function to run the market basket analysis and save the recommendations to csv
    :param args: (argparse) user-input configuration file
    """
    try:
        config_path = project_path + "/" + args.config
        input_data_path = project_path + "/" + args.input
        output_data_path = project_path + "/" + args.output
        product_path = project_path + "/" + conf.PRODUCT_DIM

        config = load_config(config_path)
        df = read_csv(input_data_path)
        product = read_csv(product_path)

        result = train(df, **config['train'])

        # Join product object table to get the name and price.

        final_results = join_info(result, product, "StockCode", "StockCode")
        final_results = join_info(final_results, product, "rec1", "StockCode")
        final_results = join_info(final_results, product, "rec2", "StockCode")

        # format conf
        final_results['conf1'] = round(final_results['conf1'] * 100, 2)
        final_results['conf2'] = round(final_results['conf2'] * 100, 2)

        final_results = final_results[config["result_columns"]]

        # Write to output file
        save_csv(final_results, output_data_path)
    except KeyError as e3:
        logger.error("KeyError: " + str(e3))
    except ValueError as e4:
        logger.error("ValueError: " + str(e4) + " Please validate Values in the configuration file.")
    except Exception as e:
        logger.error("Unexpected error occurred when making recommendations: " + str(e))


def train(df, apriori_args, association_rules_args, ante_num_filter, rank_arg, rank_filter, to_columns):
    """
    Run market basket analysis on baskets data, return recommendations
    Args:
        df: (DataFrame) Baskets
        apriori_args: (Dictionary) Arguments of apriori()
        association_rules_args: (Dictionary) Arguments of the association_rules()
        ante_num_filter: (Integer) Threshold of the maximum number of items in antecedents
        rank_filter: (Integer) Threshold of number recommendations for each item
        rank_arg: (Dictionary) Argument of the rank() function
        to_columns: (List of String) Column names of the new DataFrame

    Returns: (DataFrame) Recommendations

    """

    df = df.set_index('Invoice')
    # all products
    prods = df.columns.to_frame()

    # Calculate support for different baskets
    frequent_itemsets = get_support(df, apriori_args)
    popular_list = get_popular(frequent_itemsets)

    # Calculate confidence
    rules = get_recommendations(frequent_itemsets, association_rules_args, ante_num_filter, rank_filter, rank_arg)

    # Change data types of antecedents and consequents to string
    rules = frozenset2String(rules, 'antecedents')
    rules = frozenset2String(rules, 'consequents')

    # Pivot the DataFrame
    rec = pivot_rules(rules, to_columns)
    # Get full table
    rec = get_full_table(prods, rec, to_columns)

    # Fill na
    rec = fill_no_rec(rec, popular_list)
    logger.info("Successfully made recommendations for {} products".format(len(rec)))
    return rec


def get_support(df, apriori_args):
    """
    Calculate support for each basket using apriori()
    Args:
        df: (DataFrame) Baskets
        apriori_args: (Dictionary) Arguments of apriori()

    Returns: (DataFrame)frequent_itemsets

    """
    logger.warning("Calculate supports for item sets is heavy on memory, please increase the memory limitation to at "
                   "least 12GB")
    logger.info("Calculating supports for the item sets")
    frequent_itemsets = apriori(df, **apriori_args)
    logger.info


#!/usr/bin/env python
# coding: utf-8

# # Bundling Recommendations Product

# ## Overview
# 
# 
# Customers are indeed vital for the survival and success of any business. By understanding their preferences and satisfying their needs, businesses can generate revenue and ensure profitability. One effective strategy to enhance customer satisfaction and drive sales is by providing personalized product bundling recommendations.
# 
# To achieve this, we can leverage retail transactional data to analyze customer purchase patterns and identify items that are frequently bought together. By employing the Apriori Algorithm, a widely used technique in association rule mining, we can build a robust model that reveals the relationships between different products.
# 
# By applying this model to an online retail transactional dataset, we can gain insights into the following questions:
# 
# Which items are commonly purchased together?
# By identifying item associations, we can determine which products tend to be bought in conjunction. This information enables businesses to create attractive product bundles, offering customers a convenient and cost-effective solution by combining complementary items.
# 
# Which items are frequently purchased together in specific countries?
# Different countries often have unique consumer preferences and cultural characteristics. By analyzing retail transactional data on a country-specific basis, we can discover item associations that are specific to particular regions. This allows businesses to tailor their product bundling recommendations according to local preferences, further enhancing customer satisfaction and driving sales.
# 

# ### Dataset

# * **PojokITCom_Product_Data.xlsx** - The file contains all transactions occurring between 01/12/2010 and 09/12/2011 for a UK-based and registered non-store online retail. In thecdataset, there are eight columns:
# 
#     * InvoiceNo: Invoice number. Nominal, a 6-digit integral number uniquely assigned to each transaction. If this code starts with letter 'c', it indicates a cancellation.
#     * StockCode: Product (item) code. Nominal, a 5-digit integral number uniquely assigned to each distinct product.
#     * Description: Product (item) name. Nominal.
#     * Quantity: The quantities of each product (item) per transaction. Numeric.
#     * InvoiceDate: Invice Date and time. Numeric, the day and time when each transaction was generated.
#     * UnitPrice: Unit price. Numeric, Product price per unit in sterling.
#     * CustomerID: Customer number. Nominal, a 5-digit integral number uniquely assigned to each customer.
#     * Country: Country name. Nominal, the name of the country where each customer resides.
#     
# The dataset is avilable in [Tokopedia](https://www.tokopedia.com/pojokitcom) website. 

# The project is organized as follows. Section 1 explores and visualize the data. Section 2 includes data preperation and Section 3 implements the solution. Finally, Section 4 presents the summary.

# ## 1. Data Understanding

# This step explores the dataset using different functions such as `head()`, `shape`, `describe()` and checks the null values using `isnull()`.

# In[1]:


# Import needed libraries

import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
get_ipython().run_line_magic('matplotlib', 'inline')
from sklearn.cluster import KMeans
from sklearn.preprocessing import StandardScaler
import seaborn as sns
from mlxtend.frequent_patterns import apriori, association_rules
import re

pd.options.display.max_rows = None


# In[2]:


# Read data in the excel file

df = pd.read_excel('pojokITCom_data_product.xlsx')
df.head()


# In[3]:


df.shape


# In[4]:


df.info()


# In[5]:


df.describe()


# In[6]:


# Check null values
df.isnull().sum()


# In[7]:


# Check number of unique values
df.nunique()


# In[8]:


# Check each stock code has only one description
df.groupby('StockCode').apply(lambda x: x['Description'].unique())


# In[9]:


# Number of invoices for each country
df.groupby(['Country']).count() ['InvoiceNo']


# **Observations:**
# 
# From the data, we can see that:
# * The minimum price and quantity are negative, and this is impossible.
# * There are null values in CustomerID and Description columns.
# * Some Stock Codes are not a 5-digit integral number.
# * Some Stock Codes has more than one item description.
# 

# ## 2. Data Preparation

# This step includes cleaning data and preparing it before using Apyori algorithm.

# ### 2.1. Missing Data

# As we can see from the previous section, there are null data. So, I will remove the rows that contain null values.  

# In[10]:


# Delete rows with null CustomerID
clean_df = df.dropna(subset = ['CustomerID'])

# Check null values
clean_df.isnull().sum()


# ### 2.2. Dealing with Inconsistent Data

# This section includes:
# * Removing the price and quantity that are less than or equal to 0. 
# * Removing inconsistent Stock code.
# * Keeping only one description for each Stock codes.

# In[11]:


# Removing the price and quantity that are less than or equal to 0
clean_df = clean_df[(clean_df.Quantity >= 0) & (clean_df.UnitPrice >= 0)]


# In[12]:


clean_df.describe()


# In[13]:


# Check the number of invoices that starts with letter 'c', cancellation.
clean_df['InvoiceNo'] = clean_df['InvoiceNo'].astype('str')
clean_df[clean_df['InvoiceNo'].str.contains("c")].shape[0]


# In[14]:


# Check the stock code

def has_right_scode(input):
    
    """
    Function: check the if the stock code is wirtten in a right way,
            The function check if the code contains 5-digit number or 5-digit number with a letter.
    Args:
      input(String): Stock code
    Return:
      Boolean: True or False
    """
    
    x = re.search("^\d{5}$", input)
    y = re.search("^\d{5}[a-zA-Z]{1}$", input)
    if (x or y):
        return True
    else:
        return False

    
clean_df['StockCode'] = clean_df['StockCode'].astype('str')
clean_df = clean_df[clean_df['StockCode'].apply(has_right_scode) == True]
clean_df.head()


# In[15]:


# One discription for each stock code

# Put all Descriptions of each StockCode in a list 
df_itms = pd.DataFrame(clean_df.groupby('StockCode').apply(lambda x: x['Description'].unique())).reset_index()
df_itms.rename(columns = { 0: 'Description2'}, inplace = True)

# StockCode that have more than one Description
df_itms[df_itms['Description2'].str.len() != 1]


# As we can see, the items in the each Description list are the same item but they are written in different ways. So, I will keep only one describtion.

# In[16]:


# Take one Description for each StockCode
df_itms.loc[:, 'Description2'] = df_itms.Description2.map(lambda x: x[0])

# StockCode that have more than one Description
df_itms[df_itms['Description2'].str.len() != 1]


# In[17]:


# Merge clean_df with df_itms
clean_df = pd.merge(clean_df, df_itms, on = 'StockCode')
clean_df = clean_df.drop('Description', axis = 1)
clean_df.head()


# In[18]:


clean_df.rename(columns = { 'Description2': 'Description'}, inplace = True)
clean_df.head()


# ### 2.3. Creating Dummy Variables

# To build the association rule model, the `Description` categorical variables should be converted to dummy variables.

# #### 2.3.1 All Countries Data

# In[19]:


df_itms_togthr = clean_df.groupby(['InvoiceNo','Description'])['Quantity'].sum()
df_itms_togthr.head()


# In[20]:


df_itms_togthr = df_itms_togthr.unstack().fillna(0)
df_itms_togthr.head()


# In[21]:


# Encode the frequency of description to 0 or 1
encode = lambda x : 1 if (x >= 1) else 0
df_itms_togthr = df_itms_togthr.applymap(encode)
df_itms_togthr.head()


# In[22]:


df_itms_togthr.shape


# #### 2.3.2 Netherlands, Spain, France Countries Data

# In[23]:


nl_df = clean_df[clean_df['Country'] == 'Netherlands']
spain_df = clean_df[clean_df['Country'] == 'Spain']
france_df = clean_df[clean_df['Country'] == 'France']


# In[24]:


nl_df.head()


# In[25]:


spain_df.head()


# In[26]:


france_df.head()


# In[27]:


df_itms_togthr_nl = nl_df.groupby(['InvoiceNo','Description'])['Quantity'].sum()

df_itms_togthr_nl = df_itms_togthr_nl.unstack().fillna(0)

encode = lambda x : 1 if (x >= 1) else 0
df_itms_togthr_nl = df_itms_togthr_nl.applymap(encode)
df_itms_togthr_nl.head()


# In[28]:


df_itms_togthr_spain = spain_df.groupby(['InvoiceNo','Description'])['Quantity'].sum()

df_itms_togthr_spain = df_itms_togthr_spain.unstack().fillna(0)

encode = lambda x : 1 if (x >= 1) else 0
df_itms_togthr_spain = df_itms_togthr_spain.applymap(encode)
df_itms_togthr_spain.head()


# In[29]:


df_itms_togthr_france = france_df.groupby(['InvoiceNo','Description'])['Quantity'].sum()

df_itms_togthr_france = df_itms_togthr_france.unstack().fillna(0)

encode = lambda x : 1 if (x >= 1) else 0
df_itms_togthr_france = df_itms_togthr_france.applymap(encode)
df_itms_togthr_france.head()


# Now, the data is ready to be used to build the association rule model.

# ## 3. System Modeling 

# To build the association rule model and answer the questions related to product bundling recommendation, we will employ the Apriori Algorithm, which is a popular technique in association rule mining. The model will utilize a dataset containing retail transactional data.
# 
# The first step is to determine an appropriate minimum support value for our model. This value influences the threshold for item frequency in the dataset. For larger datasets, a smaller support value is chosen to capture more item associations, while for smaller datasets, a slightly higher support value is selected to avoid noise and ensure reliable results.
# 
# In this project, we will consider the size of the dataset to determine the minimum support value, ensuring it is suitable for product bundling recommendation. By carefully evaluating the dataset, we can strike a balance between capturing significant associations and maintaining a reasonable level of computational complexity.
# 
# Additionally, we will set the confidence level to 60%. This parameter helps determine the strength of the association between items. With a confidence level of 60%, we will focus on finding associations where there is a high likelihood (at least 60%) that one item will be purchased if the other is already in the customer's basket.
# 
# After building the association rule model with the selected support and confidence values, we can examine the results to address the questions at hand. We will identify the items that are frequently bought together, allowing us to suggest these bundles to customers. Moreover, by analyzing the dataset on a country-specific basis, we can determine which items are commonly purchased together in different countries. This knowledge can inform targeted product bundling recommendations for customers based on their geographical location.

# ### 3.1 All Countries Data

# In[30]:


# Build the Apriori model
rep_items = apriori(df_itms_togthr, min_support = 0.02, use_colnames = True, verbose = 1)
rep_items.head()


# In[31]:


# Generate the association rules dataframe
rules = association_rules(rep_items, metric = "confidence", min_threshold = 0.6)
rules


# In[32]:


# The number of rules
rules.shape[0]


# In[33]:


# Show confidence distribution
plt.hist(rules['confidence'])
plt.show()


# In[34]:


# Show the rules that have confidance > 0.75
high_confidance = rules[rules['confidence'] > 0.75]
high_confidance [['antecedents', 'consequents']]


# ### 3.2 Netherlands, Spain, France Countries Data

# In[35]:


# Build the Apriori model
rep_items_nl = apriori(df_itms_togthr_nl, min_support = 0.1, use_colnames = True, verbose = 1)
rep_items_nl.head()


# In[36]:


# Generate the association rules dataframe
rules_nl = association_rules(rep_items_nl, metric = "confidence", min_threshold = 0.6)
rules_nl.head()


# In[37]:


# The number of rules
rules_nl.shape[0]


# In[38]:


high_confidance_nl = rules_nl[rules_nl['confidence'] > 0.75]
high_confidance_nl [['antecedents', 'consequents']]


# In[39]:


# Build the Apriori model
rep_items_spain = apriori(df_itms_togthr_spain, min_support = 0.1, use_colnames = True, verbose = 1)
rep_items_spain.head()


# In[40]:


# Generate the association rules dataframe
rules_spain = association_rules(rep_items_spain, metric = "confidence", min_threshold = 0.6)
rules_spain


# In[41]:


# The number of rules
rules_spain.shape[0]


# In[42]:


high_confidance_spain = rules_spain[rules_spain['confidence'] > 0.75]
high_confidance_spain [['antecedents', 'consequents']]


# In[43]:


# Build the Apriori model
rep_items_france = apriori(df_itms_togthr_france, min_support = 0.05, use_colnames = True, verbose = 1)
rep_items_france.head()


# In[44]:


# Generate the association rules dataframe
rules_france = association_rules(rep_items_france, metric = "confidence", min_threshold = 0.6)
rules_france


# In[45]:


# The number of rules
rules_france.shape[0]


# In[47]:


high_confidance_france = rules_france[rules_france['confidence'] > 0.75]
high_confidance_france [['antecedents', 'consequents']]


# ### 3.3 Results Discussion
# 
# The results of association analysis show which item is frequently purchased with other items. The result of association analysis using the whole dataset is differnt than the result of association analysis when using a dataset of a spacific country. 

# ## 4. Summary Analysis

# After applying the Apriori algorithm to the retail transactional dataset, we obtained valuable rules regarding the frequent purchase associations between items. These rules provide insights into the products that are often bought together, which can be highly beneficial for product bundling recommendation strategies in e-commerce websites.
# 
# By analyzing the entire dataset, we uncovered associations between items that are relevant across different countries and customer segments. These associations reflect broader patterns of customer preferences and purchasing behavior, enabling us to generate recommendations for item bundling that have a broad appeal and potential for driving sales.
# 
# However, it's important to note that the results of association analysis may differ when analyzing datasets specific to individual countries. Cultural, geographical, and demographic factors can significantly influence customer preferences and purchasing habits. Thus, by conducting association analysis on a country-specific basis, we can identify associations that are unique to each region, providing targeted recommendations for item bundling in respective markets.
# 
# The rules derived from both the analysis of the whole dataset and the analysis of country-specific datasets are highly valuable for item recommendations on e-commerce websites. By leveraging these rules, businesses can offer customers tailored product bundles based on their preferences and purchasing behavior. This personalized approach enhances the customer experience, increases the likelihood of cross-selling, and ultimately drives revenue and customer satisfaction.
# 
# In summary, the results of the association analysis using the Apriori algorithm provide valuable insights for product bundling recommendation in e-commerce. By considering both the associations from the whole dataset and those specific to individual countries, businesses can offer personalized recommendations that cater to diverse customer segments, resulting in improved customer satisfaction and increased sales.

# ## 5. Future Develepoment

# Based on the valuable insights gained from the association analysis using the Apriori algorithm, businesses can leverage these findings to drive future development in their product bundling recommendation strategies. Here are some potential areas for development:
# 
# 1. Personalized Recommendations: The analysis of the whole dataset and country-specific datasets allows businesses to offer personalized recommendations to customers. Going forward, businesses can further refine their recommendation engines by incorporating additional customer data, such as browsing history, past purchases, and demographic information. This increased personalization will enhance the relevance and effectiveness of product bundling recommendations, leading to higher customer satisfaction and conversion rates.
# 
# 2. Dynamic Bundling: The association rules derived from the analysis can be used to create static product bundles that are commonly purchased together. However, businesses can explore dynamic bundling strategies by continuously analyzing real-time transactional data. By identifying emerging trends and adjusting product bundles accordingly, businesses can stay responsive to evolving customer preferences and market dynamics. Dynamic bundling can lead to increased sales and customer engagement by offering timely and relevant product recommendations.
# 
# 3. Cross-Selling Opportunities: The association rules reveal items that are frequently purchased together, presenting cross-selling opportunities. Businesses can capitalize on these associations by strategically promoting complementary products during the customer journey. For example, when a customer adds a specific item to their cart, the system can suggest related products that enhance the overall experience or offer cost savings through bundled offers. This proactive approach to cross-selling can boost average order value and customer loyalty.
# 
# 4. Localization: The analysis of country-specific datasets provides insights into unique associations and preferences specific to different regions. Future development can involve tailoring product bundling recommendations to local markets by considering cultural nuances, regional preferences, and purchasing behaviors. Localization efforts can include adjusting product bundles, promotional offers, and marketing campaigns to resonate with customers in each country, resulting in more effective and targeted recommendations.
# 
# 5. Continuous Analysis and Refinement: The association analysis is an iterative process that should be regularly updated to capture evolving customer preferences and market dynamics. By continuously analyzing transactional data, businesses can identify new item associations, monitor changes in purchasing patterns, and refine their product bundling strategies accordingly. This continuous improvement process ensures that businesses stay responsive to customer needs and maintain a competitive edge in the e-commerce landscape.
# 
# In conclusion, the insights gained from the association analysis provide a solid foundation for future development in product bundling recommendation strategies. By focusing on personalization, dynamic bundling, cross-selling opportunities, localization, and continuous analysis, businesses can enhance the effectiveness of their recommendations, drive sales, and deliver exceptional customer experiences in the evolving e-commerce landscape.

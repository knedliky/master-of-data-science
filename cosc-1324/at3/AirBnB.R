# Loading libraries
library(ggplot2)
library(readr)
library(dplyr)
library(Hmisc)
library(psychometric)

#Loading data set
listings <- read_csv('listings.csv')

# Selecting variables from the original dataset for the specific information that we are investigating
waverley <- listings %>% dplyr::select(id, room_type, guests_included, neighbourhood_cleansed, number_of_reviews, review_scores_rating, price) 

# Filtering the variables further, to get the location of AirBnBs, type of accommodation that is desired and homes with more than 15 reviews
waverley < - waverley %>% dplyr::filter(neighbourhood_cleansed == "Waverley")
waverley <- waverley %>% dplyr::filter(room_type == "Entire home/apt")
waverley <- waverley %>% dplyr::filter(number_of_reviews > 15)

# Parsing the prices in, as numeric instead of character - thereby removing the "$"
waverley$price <- parse_number(waverley$price)

# Creating a column for observation to get a standardised variable, price per guest
waverley <- mutate(waverley, price_per_guest = price/guests_included)

# Checking na values that might need to be removed, there are none so no need to remove them in the upcoming Summary tables
colSums(is.na(waverley))

# Rating summary table
table_rating <- waverley %>% summarise(Min = min(review_scores_rating),
                       Q1 = quantile(review_scores_rating, probs = 0.25), 
                       Mean = mean(review_scores_rating), 
                       Q3 = quantile(review_scores_rating, probs = 0.75), 
                       Max = max(review_scores_rating), 
                       Median = median(review_scores_rating), 
                       SD = sd(review_scores_rating), 
                       Mean = mean(review_scores_rating), 
                       n = n(), 
                       Missing = sum(is.na(review_scores_rating)))

# Summary table of price per guest
table_price <- waverley %>% summarise(Min = min(price_per_guest),
                                      Q1 = quantile(price_per_guest, probs = 0.25), 
                                      Mean = mean(price_per_guest), 
                                      Q3 = quantile(price_per_guest, probs = 0.75), 
                                      Max = max(price_per_guest), 
                                      Median = median(price_per_guest), 
                                      SD = sd(price_per_guest), 
                                      Mean = mean(price_per_guest), 
                                      n = n(), 
                                      Missing = sum(is.na(price_per_guest)))

# Grouped by guests included for a deeper look
table_grouped_guests <- waverley %>% group_by(guests_included) %>%  summarise(Min = min(price_per_guest),
                                                      Q1 = quantile(price_per_guest, probs = 0.25), 
                                                      Mean = mean(price_per_guest), 
                                                      Q3 = quantile(price_per_guest, probs = 0.75), 
                                                      Max = max(price_per_guest), 
                                                      Median = median(price_per_guest), 
                                                      SD = sd(price_per_guest), 
                                                      Mean = mean(price_per_guest), 
                                                      n = n(), 
                                                      Missing = sum(is.na(price_per_guest)))

# Some discovery on what our outliers are, and whether we should remove them from the exploration
rating_outliers <- boxplot(waverley$review_scores_rating)$out
View(waverley[which(waverley$review_scores_rating %in% rating_outliers),])

# These seem reasonable and what we might be looking at, so no need ot remove these outliers. This is confirmed by looking at the variance
var(rating_outliers)

# Pricing on the other hand, grows exponentially in the upper brackets and will skew the data in a positive direction
price_outliers <- boxplot(waverley$price_per_guest)$out
View(waverley[which(waverley$price_per_guest %in% price_outliers),])
# Confirmed again by looking at the outlier variance
var(price_outliers)

# Removing outliers from the price dataset
waverley <- waverley[-which(waverley$price_per_guest %in% price_outliers),]

# Loading Ggplot for visualisation 
# Initial scatter plot with a 2D density plot suggests there there could be a positive correlation between the two variables, with a concentration in top left quadrant of the graph.
gg <- ggplot(data = waverley, aes(price_per_guest, review_scores_rating))
gg + geom_density_2d() + 
  geom_jitter(aes(color = guests_included)) + 
  labs(title = "AirBnb Prices vs. Ratings",  x = "Price per Guest", y = "Average Rating", colour = "Guests") + 
  scale_color_gradient(low = "purple", high = "red")

# Using the psychometric package to measure a 95% Confidence Interval to test the Hypothesis
r = cor(waverley$review_scores_rating, waverley$price_per_guest)
CIr(r = r, n = sum(count(waverley)), level = .95)
# The Confidence Interval does not capture H0, therefore the null Hypothesis is rejected. There was a statistically significant
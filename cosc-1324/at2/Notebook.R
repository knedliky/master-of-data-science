library(dplyr)
library(ggplot2)
library(tidyr)

# Read in data
data <- read.csv("Data.csv")

# Tidy Data
summary(data)
# data <- select(data, -c(UID, Suburb))
data <- data %>% gather(Item, Price, apple:oj)
data$Item <- factor(data$Item)

# Summary statistics
# Supermarkets
data %>% group_by(Supermarket) %>% summarise(Min = min(Price, na.rm = TRUE),
                                             Q1 = quantile(Price, probs = 0.25, na.rm = TRUE),
                                             Median = median(Price, na.rm = TRUE),
                                             Mean = mean(Price, na.rm = TRUE),
                                             SD = sd(Price, na.rm = TRUE),
                                             Q3 = quantile(Price, probs = 0.75, na.rm = TRUE), 
                                             Max = max(Price, na.rm = TRUE),
                                             n = n(),
                                             Missing = sum(is.na(Price)))

# Items
data %>% group_by(Item) %>% summarise(Min = min(Price, na.rm = TRUE),
                                                  Q1 = quantile(Price, probs = 0.25, na.rm = TRUE),
                                                  Median = median(Price, na.rm = TRUE),
                                                  Mean = mean(Price, na.rm = TRUE),
                                                  SD = sd(Price, na.rm = TRUE),
                                                  Q3 = quantile(Price, probs = 0.75, na.rm = TRUE), 
                                                  Max = max(Price, na.rm = TRUE),
                                                  n = n(),
                                                  Missing = sum(is.na(Price)))

# Cleaning NA's 
data <- drop_na(data,Price)

# Boxplot of Price spread between Supermarkets
ggplot(data) + 
  geom_boxplot(aes(fill = Supermarket, x = Supermarket, y = Price)) +
  ggtitle("Pricing by Supermarket chain") +
  ylab("Price in $") +
  theme_light()

# Boxplot of Spread between Item Prices
ggplot(data) + 
  geom_boxplot(aes(fill = Supermarket, x = Item, y = Price)) +
  ggtitle("Pricing by Supermarket chain") +
  ylab("Price in $") +
  theme_light()

# Filtering for values with meaningful differences between Supermarkets
vars <- c("apple", "avo", "broc", "orange", "straw")
data_filtered <- data %>% filter(Item %in% vars) %>% droplevels()

# Replotting Boxplot with filtered data
ggplot(data_filtered) + 
  geom_boxplot(aes(fill = Supermarket, x = Item, y = Price)) +
  ggtitle("Pricing by Supermarket chain") +
  ylab("Price in $") +
  theme_light()

ggplot(data_filtered) + 
  geom_boxplot(aes(fill = Supermarket, x = Supermarket, y = Price)) +
  ggtitle("Pricing by Supermarket chain") +
  ylab("Price in $") +
  theme_light()

# Initially it looks as if Woolworths is the more expensive supermarket
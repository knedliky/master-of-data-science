library(forcats)
library(dplyr)
library(ggplot2)
library(readr)

bdims <- read_csv("bdims.csv")
bdims$sex <- bdims$sex %>% fct_recode("Female"="0", "Male"="1")
df <- data.frame(Age = age, Sex = sex, Height = height, Weight = weight, Waist = waist)
Summary <- df %>% group_by(Sex) %>% summarise(Min = min(Waist,na.rm = TRUE),
                                              Q1 = quantile(Waist,probs = .25,na.rm = TRUE),
                                              Median = median(Waist, na.rm = TRUE),
                                              Q3 = quantile(Waist,probs = .75,na.rm = TRUE),
                                              Max = max(Waist,na.rm = TRUE),
                                              Mean = mean(Waist, na.rm = TRUE),
                                              SD = sd(Waist, na.rm = TRUE),
                                              n = n(),
                                              Missing = sum(is.na(Waist)))

hist(df_male$Waist, xlab = "Waist girth (cm)", main = "Waist girth in Males", col ="lightblue")
hist(df_female$Waist, xlab = "Waist girth (cm)", main = "Waist girth in Females", col ="pink")

hist(df_male$Waist, probability = TRUE, xlab = "Waist girth (cm)", main = "Waist dimension in Males", col = "lightblue")
x <- (male_min-10):(male_max+10)
y <- dnorm(x = x, mean = male_mean, sd = male_sd)
lines(x = x, y = y, col = "maroon", lwd=2)

hist(df_female$Waist, probability = TRUE, xlab = "Waist girth (cm)", main = "Waist dimension in Females", col = "pink")
x <- (female_min-10):(female_max+10)
y <- dnorm(x = x, mean = female_mean, sd = female_sd)
lines(x = x, y = y, col = "maroon", lwd =2)

Mean <- mean(df$Waist, na.rm = TRUE),
Median <- median(df$Waist, na.rm = TRUE),
Sd <- sd(df$Waist, na.rm = TRUE),
Min <- min(df$Waist, na.rm = TRUE),
Max <- max(df$Waist, na.rm = TRUE),
Q1 <- quantile(df$Waist, probs =.25, na.rm = TRUE),
Q3 <- quantile(df$Waist, probs =.75, na.rm = TRUE),
n <- n(),
Missing <- sum(is.na(df$Waist))

df_male <- df %>% filter(Sex == "Male")
df_female <- df %>% filter(Sex == "Female")
mWaist <- df_male$Waist
mMean <-mean(mWaist)
mMed <- median(mWaist)
mSd<- sd(mWaist)
mQ1 <- quantile(mWaist, probs =.25, na.rm = TRUE)
mQ3 <- quantile(mWaist, probs =.75, na.rm = TRUE)
mIQR <- IQR(mWaist)
fWaist <- df_female$Waist
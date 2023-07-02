library(readr)
library(tidyr)
library(dplyr)
library(ggplot2)
library(lubridate)

taxi <- read_csv('taxi.csv')
temperature <- read_csv('temperature.csv')
weather <- read_csv('weather_description.csv')

ny_weather <- left_join(select(temperature, datetime, `New York`), select(weather, datetime, `New York`), by = "datetime")
ny_weather <- rename(ny_weather, temperature = `New York.x`, description = `New York.y`)

taxi$pickup_date <- as_date(taxi$pickup_datetime)
ny_weather$date <- as_date(ny_weather$datetime)

ny_weather_taxi <- left_join(taxi, ny_weather, by = c('pickup_date' = 'date'))
cols <- c('pickup_date', 'pickup_datetime', 'passenger_count', 'temperature', 'description')
ny_weather_taxi %>% select(cols)

kelvin_to_celsius <- function(kelvin) {
  return(kelvin - 273.15)
}

ny_weather_taxi$temperature_celsius <- kelvin_to_celsius(ny_weather_taxi$temperature)
# Removing N/A values
ny_weather_taxi <- ny_weather_taxi[complete.cases(ny_weather_taxi),]

# Vehicle Accidents
accidents <- read_csv('accidents.csv')
accidents_filtered <- accidents %>% select(one_of(c('UNIQUE KEY', 'DATE', 'PERSONS KILLED')))
ny_weather_taxi_accidents <- left_join(ny_weather_taxi, accidents_filtered, by = c("pickup_date" = "DATE"))
df <- select(ny_weather_taxi_accidents, one_of('pickup_date', 'pickup_datetime', 'passenger_count', 'temperature_celsius', 'description', 'UNIQUE KEY', 'PERSONS KILLED'))

# Data Frame
df <- rename(df, accident_id = 'UNIQUE KEY', persons_deceased = 'PERSONS KILLED')
df$description <- df$description %>% as.factor()
# Removing N/A values
df <- df[complete.cases(df),]
df$pickup_datetime <- ymd_hms(df$pickup_datetime)

# Get Season for Northern Hemisphere countries
getNorthernSeason <- function(input.date) {
  # create a numeric in the format off MMDD
  numeric.date <- 100 * month(input.date) + day(input.date)
  # Separate the data into the start and endof the year, and season starts and ends
  seasons <- cut(numeric.date, breaks = c(0,319,0620,0921,1220,1231))
  # Assign seasons
  levels(seasons) <- ordered(c('Winter', 'Spring', 'Summer', 'Autumn', 'Winter'))
  return(seasons)
}

df$season <- getNorthernSeason(df$pickup_date)
df$temperature_celsius <- round(df$temperature_celsius, digits = 1)
df$accident_id <- df$accident_id %>% as.factor()

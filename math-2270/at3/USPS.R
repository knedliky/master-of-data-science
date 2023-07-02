library(usmap)
library(maps)
library(mapproj)
library(dplyr)
library(tidyr)
library(ggplot2)
library(readr)
library(readxl)
library(magrittr)
library(plotly)

# Template of how the map should be generated
us_states <- map_data('state')
p <- ggplot(data = us_states, mapping = aes(x = long, y = lat, group = group, fill = region))
p + geom_polygon(color = 'gray90', size = 0.1) + coord_map(projection = 'albers', lat0 = 39, lat1 = 45) + guides(fill = FALSE)

# 2020 Collection Box data
df_2020 <- read_csv("collection_boxes_2020-08-19.csv")
boxes_2020 <- df_2020 %>% select(OUTLETID, BUSNAME, STATE, CITY, LATITUDE, LONGITUDE)
boxes_count_2020 <- boxes_2020 %>% group_by(STATE) %>% summarise(count2020 =n())

# 2019 Collection Box data
df_2019 <- read_excel('coll report.xlsx')
boxes_2019 <- df_2019 %>% select(OUTLETID, BUSNAME, CITY, STATE, LATITUDE, LONGITUDE)
boxes_count_2019 <- boxes_2019 %>% group_by(STATE) %>% summarise(count2019 =n())

# Joined Collection Box data
boxes_count <- left_join(boxes_count_2019, boxes_count_2020, by = 'STATE')

boxes_count$region <- tolower(state.name[match(boxes_count$STATE, state.abb)])

collection_boxes_2020[1:4] <- lapply(collection_boxes_2020[1:4], as.factor)
boxes_count_2020 <- boxes_2020 %>% group_by(STATE) %>% summarise(count2020 =n())

state_count_2020$STATENAME <- tolower(state.name[match(state_count_2020$STATE, state.abb)])
us_states_boxes <- left_join(us_states, state_count_2020, by = c('region' = 'STATENAME'))

df_2019 <- read_excel('coll report.xlsx')
boxes_2019 <- df_2019 %>% select(OUTLETID, BUSNAME, CITY, STATE, LATITUDE, LONGITUDE)
boxes_2019[1:4] <- lapply(boxes_2019[1:4], as.factor)


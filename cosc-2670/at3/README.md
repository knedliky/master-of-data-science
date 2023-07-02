# cosc2670
### Python for Data Science Assessment Task 3

#### Collaborative Filtering
Missing values in datsets are a challenging aspect in Data Science Projects. In recommender systems, collaborative filtering is used to provide recommendations to users, based on similarities between indvidual users, as well as individual items. Currently, there are two standard methods of collaborative filterings used in typical recommendation systems - model-based and memory based approaches. Model-based approaches include using the K Nearest Neighbour algorithm (KNN) to solve for the missing value, where memory-based approaches tackle this problem by using either user-item filtering, or item-item filtering. These systems are used by companies across the globe such as Netflix, YouTube and Spotify to provide users with recommendations on items they might find interesting. 

#### User-Item filtering
This method of memory-based collaborative filtering finds similar users to a users unrated item, and makes a suggestion based on whether a similar usera has also liked this item. The assumption is that there will be at least one similar user for each unrated item.

#### Item-Item filtering
This method find similar items to a particular users unrated item, instead of a similar users to make a suggestion. Again the assumption here is that there will be at least one other similar item in the dataset.

#### Challenges of Collaborative Filtering
One of the fundamental challenges of memory-based approaches is data sparsity. Often, users of a particular product have very few items that they have rated, meaning the resulting prediction is inaccurate. Similar users who rate more than others may be more likely to be a 'similar user' and may skew predictions, and there have been many methods to overcome this including generative probabilistic models that fuse ratings with a predictive value, or hybrid models that use the strengths of both memory and model-based aproaches in a smoothing-based model.

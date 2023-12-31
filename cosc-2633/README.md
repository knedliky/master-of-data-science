# 💾 Big Data Processing

_Completed Semester 2, 2021_

The course covers Big Data Fundamentals, including the characteristics of Big Data, the sources Big Data (such as social media, sensor data, and geospatial data), as well as the challenges imposed around information management, data analytics, privacy and security, and platforms and architectures. Emphasis will be given to non-relational databases by examining techniques for storing and processing large volumes of structured and unstructured data, streaming data as well as complex analytics on them. Data warehouses will also be presented as a solution to handling big data and business intelligence applications.

The course aimed to keep a balance between algorithmic and systems issues. The algorithms discussed in this course involve methods of organising big data for efficient complex computation. In addition we consider Big Data platforms (such as Hadoop) to present practical applications of the algorithms covered in the course.

## Assessment Task 1

1. Write a MapReduce program to count number of words by word length (small, medium, long and extra long)
2. Write a MapReduce program using in-mapper combining, without preserving state across documents
3. Write a MapReduce program using in-mapper combining and preserving state across documents

#### Analysis

On smaller datasets, preserving state is suitable across multiple documents, where the limitations of scalability bottlenecks are far less likely to be encountered. This can be seen in the third requirement of this task, where only 12 records (135 bytes) were transferred across the network compared to 1753 records (19,584 bytes) transferred across the network when local aggregation was used,  no no preservation implemented.

The scalability bottleneck refers to the limitations of each task node, in terms of memory. If the associative array being used to preserve state across multiple documents becomes too large, then it is impossible to keep intermediate key/value pairs in memory, unless some kind of blocking or flushing mechanism is used. This could occur when significantly large and diverse wordsets are encountered when more and more texts are added, for example. In this case, a local in map combiner would work better, as it would 'flush' out its memory much more efficiently and not be limited to the overall size of the global associative array. On the other hand, if a large amount of similar text, for example a library of texts on the topic of frogs where the vocabulary base would be quite small, maintaining a global associate array would be very performative as there is less data travelling across the nodes.

## Assessment Task 2

1. Write a MapReduce program to count the number of words using the pairs approach
2. Write a MapReduce program to count the frequency of words using the stripes approach
3. Write a MapReduce program implementing the partition around mediods (PAM) algorithm

## Assessment Task 3

1. Write a Scala program to count the frequency of words for each RDD of DataStream
2. Write a Scala program to count the frequency of co-occurred words while filtering out short words (less than 5 characters) for each RDD of DataStream
3. Write a Scala program to count the frequency of co-occurred words while filtering out short words (less than 5 characters) for the DataStream

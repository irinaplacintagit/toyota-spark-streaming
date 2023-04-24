## Overview
The project computes some statistics on the IMDB dataset and is built using Spark.

As the data is static, Spark batch mode has been used. 

In order to convert this to a streaming project, we can consider the data coming in the respective folders and change the read to use streams instead. 
Note this has been attempted and a timestamp column has been addded to the datasets to simulate a real time streaming application. In order to answer the first requirement, a stream x stream join is needed; the complexity started increasing at that point and after double checking with Paul, I left this as a batch project.

## How to run
Download the IMDB dataset, unzip the following files and places them as:
- `name.basics.tsv` into `src/main/resources/name.basics/`
- `title.basics.tsv` into `src/main/resources/title.basics/`
- `title.principals.tsv` into `src/main/resources/title.principals/`
- `title.ratings.tsv` into `src/main/resources/title.ratings/`

### Prerequisite
- sbt
- IntelliJ

### Instructions
- Import project into IntelliJ or your IDE of choice
- run `sbt compile`
- Run `SparkMain.scala`





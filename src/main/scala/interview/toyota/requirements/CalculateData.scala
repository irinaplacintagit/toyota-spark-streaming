package interview.toyota.requirements

import interview.toyota.model.{MovieRank, MovieRating}
import org.apache.spark.sql.functions.{avg, col}
import org.apache.spark.sql.{Dataset, SparkSession}

object CalculateData {

  /**
    * Retrieve the top N movies with a minimum of 500 votes with the ranking determined by:
    * //(numVotes/averageNumberOfVotes) * averageRating
    *
    * @param titleRatings dataset of title ratings
    * @param topN number of movies to be returned
    * @param numVotesFilter number of votes filter
    * @param sparkSession
    * @return a dataset of MovieRank
    */
  def getTopMovies(titleRatings: Dataset[MovieRating], topN: Int, numVotesFilter: Int)(implicit sparkSession: SparkSession): Dataset[MovieRank] = {
    import sparkSession.implicits._

    titleRatings.cache()
    val averageVotes = titleRatings.select(avg("numVotes")).as[Double].head

    val topMovies = titleRatings
      .filter(col("numVotes") >= numVotesFilter)
      .withColumn("rank", (col("numVotes") / averageVotes) * col("averageRating"))
      .orderBy(col("rank").desc)
      .as[MovieRank]
      .limit(topN)

    titleRatings.unpersist()

    topMovies
  }


  /**
    * For topMovies, list all the persons who are most often credited
    *
    * @param topMovies movie IDs
    * @param titlePrincipals title principals dataset
    * @param nameBasics name basics dataset
    * @param sparkSession
    * @return a dataset of [castId, primary Name, movie Id]
    */
  def getMostCredited(topMovies: Seq[String],
                      titlePrincipals:  Dataset[(String, String)],
                      nameBasics: Dataset[(String, String)])
                     (implicit sparkSession: SparkSession): Dataset[(String, String, String)] = {

    import sparkSession.implicits._

    val moviesWithCast = titlePrincipals
      .filter(col("movieId").isInCollection(topMovies))

    nameBasics
      .join(moviesWithCast.hint("broadcast"), "castId")
      .as[(String, String, String)]

  }

  /**
    * For topMovies Ids, list the different titles
    *
    * @param topMovies
    * @param titleBasics
    * @param sparkSession
    * @return a dataset of [movie Id, primary title, original title]
    */
  def getMostDifferentTitles(topMovies: Seq[String],
                             titleBasics: Dataset[(String, String, String)])
                     (implicit sparkSession: SparkSession): Dataset[(String, String, String)] = {

    titleBasics
      .filter(col("movieId").isInCollection(topMovies))
  }
}

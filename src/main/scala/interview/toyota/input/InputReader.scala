package interview.toyota.input

import interview.toyota.model.MovieRating
import org.apache.spark.sql.{Dataset, SparkSession}
import org.apache.spark.sql.functions.{col, current_timestamp}
import org.apache.spark.sql.types.{BooleanType, DoubleType, IntegerType, StringType, StructField, StructType}

object InputReader {

  private val movieId = "tconst"
  private val AverageRatingColumn = "averageRating"
  private val NumVotesColumn = "numVotes"

  /**
    * Get the title ratings from a TSV file
    * @param path of the TSV file
    * @param sparkSession
    * @return a Dataset of MovieRating
    */
  def getTitleRatings(path: String)(implicit sparkSession: SparkSession): Dataset[MovieRating] = {

    import sparkSession.implicits._

    val schema = StructType(Seq(
      StructField(movieId, StringType, true),
      StructField(AverageRatingColumn, DoubleType, true),
      StructField(NumVotesColumn, IntegerType, true)
    ))

    sparkSession
      .read
      .option("header", "true")
      .option("delimiter", "\t")
      .schema(schema)
      .csv(path)
      .withColumn("timestamp", current_timestamp())
      .select(col(movieId).as("movieId"),
        col(AverageRatingColumn),
        col(NumVotesColumn))
      .as[MovieRating]
  }

  /**
    * Get the title principals from a TSV file
    * @param path of the TSV file
    * @param sparkSession
    * @return a dataset ("movieId", "castId")
    */
  def getTitlePrincipals(path: String)(implicit sparkSession: SparkSession): Dataset[(String, String)] = {

    import sparkSession.implicits._
    // tconst (string) - alphanumeric unique identifier of the title
    //ordering (integer) – a number to uniquely identify rows for a given titleId
    //nconst (string) - alphanumeric unique identifier of the name/person
    //category (string) - the category of job that person was in
    //job (string) - the specific job title if applicable, else '\N'
    //characters (string) - the name of the character played if applicable, else '\N'
    val schema = StructType(Seq(
      StructField(movieId, StringType, true),
      StructField("ordering", DoubleType, true),
      StructField("nconst", StringType, true),
      StructField("category", StringType, true),
      StructField("job", StringType, true),
      StructField("characters", StringType, true)
    ))

    sparkSession
      .read
      .option("header", "true")
      .option("delimiter", "\t")
      .schema(schema)
      .csv(path)
      .select(col(movieId).as("movieId"), col("nconst").as("castId"))
      .as[(String, String)]
  }

  /**
    * Get the name basics from a TSV file
    * @param path of the TSV file
    * @param sparkSession
    * @return a dataset ("castId", "primaryName")
    */
  def getNameBasics(path: String)(implicit sparkSession: SparkSession): Dataset[(String, String)] = {

    import sparkSession.implicits._

    // nconst (string) - alphanumeric unique identifier of the name/person
    //primaryName (string)– name by which the person is most often credited
    //birthYear – in YYYY format
    //deathYear – in YYYY format if applicable, else '\N'
    //primaryProfession (array of strings)– the top-3 professions of the person
    //knownForTitles (array of tconsts) – titles the person is known for
    val schema = StructType(Seq(
      StructField("nconst", StringType, true),
      StructField("primaryName", StringType, true),
      StructField("birthYear", StringType, true),
      StructField("deathYear", StringType, true),
      StructField("primaryProfession", StringType, true),
      StructField("knownForTitles", StringType, true)
    ))

    sparkSession
      .read
      .option("header", "true")
      .option("delimiter", "\t")
      .schema(schema)
      .csv(path)
      .select(col("nconst").as("castId"), col("primaryName"))
      .as[(String, String)]
  }

  /**
    * Ge the title basics from a TSV file
    * @param path of the TSV file
    * @param sparkSession
    * @return a dataset ("movieId", "primaryTitle", "originalTitle")
    */
  def getTitleBasics(path: String)(implicit sparkSession: SparkSession): Dataset[(String, String, String)] = {

    import sparkSession.implicits._

    //tconst (string) - alphanumeric unique identifier of the title
    //titleType (string) – the type/format of the title (e.g. movie, short, tvseries, tvepisode, video, etc)
    //primaryTitle (string) – the more popular title / the title used by the filmmakers on promotional materials at the point of release
    //originalTitle (string) - original title, in the original language
    //isAdult (boolean) - 0: non-adult title; 1: adult title
    //startYear (YYYY) – represents the release year of a title. In the case of TV Series, it is the series start year
    //endYear (YYYY) – TV Series end year. ‘\N’ for all other title types
    //runtimeMinutes – primary runtime of the title, in minutes
    //genres (string array) – includes up to three genres associated with the title
    val schema = StructType(Seq(
      StructField("tconst", StringType, true),
      StructField("titleType", StringType, true),
      StructField("primaryTitle", StringType, true),
      StructField("originalTitle", StringType, true),
      StructField("isAdult", BooleanType, true),
      StructField("startYear", StringType, true),
      StructField("endYear", StringType, true),
      StructField("runtimeMinutes", StringType, true),
      StructField("genres", StringType, true),
    ))

    sparkSession
      .read
      .option("header", "true")
      .option("delimiter", "\t")
      .schema(schema)
      .csv(path)
      .select(col("tconst").as("movieId"),
        col("primaryTitle"),
        col("originalTitle"))
      .as[(String, String, String)]
  }
}

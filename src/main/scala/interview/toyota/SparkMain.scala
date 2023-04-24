package interview.toyota

import interview.toyota.config.SparkSetup
import interview.toyota.input.InputReader
import interview.toyota.requirements.CalculateData
import org.apache.spark.sql.{Dataset, SparkSession}

object SparkMain {

  val titleRatingsPath = "src/main/resources/title.ratings"
  val titlePrincipalsPath = "src/main/resources/title.principals"
  val nameBasicsPath = "src/main/resources/name.basics"
  val titleBasicsPath = "src/main/resources/title.basics"

  def main(args: Array[String]): Unit = {
    implicit val sparkSession: SparkSession = SparkSetup.getSparkSession()
    import sparkSession.implicits._

    val titleRatings = InputReader.getTitleRatings(titleRatingsPath)
    val titlePrincipals: Dataset[(String, String)] = InputReader.getTitlePrincipals(titlePrincipalsPath)
    val nameBasics: Dataset[(String, String)] = InputReader.getNameBasics(nameBasicsPath)
    val titleBasics: Dataset[(String, String, String)] = InputReader.getTitleBasics(titleBasicsPath)

    // Requirement 1
    val top10Movies = CalculateData.getTopMovies(titleRatings, 10, 500)
    top10Movies.show(200, false)
    val topMoviesIds = top10Movies.select("movieId").as[String].collect().toSeq

    // Requirement 2
    val moviesWithCast = CalculateData.getMostCredited(topMoviesIds, titlePrincipals, nameBasics)
    moviesWithCast.show(200, false)

    // Requirement 3
    val movieTitles = CalculateData.getMostDifferentTitles(topMoviesIds, titleBasics)
    movieTitles.show(200, false)
  }
}

package interview.toyota.requirements

import interview.toyota.model.{MovieRank, MovieRating}
import org.apache.spark.sql.Dataset
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import setup.SharedSparkContext

class CalculateDataTest extends AnyFunSuite  with Matchers with SharedSparkContext {

  import spark.implicits._

  test("get top movies returns the correct data") {

    val data: Dataset[MovieRating] = Seq(MovieRating("tt0000001", 5.7, 1965),
      MovieRating("tt0000002", 5.8, 263),
      MovieRating("tt0000003", 6.5, 1809),
      MovieRating("tt0000004", 5.6, 178),
      MovieRating("tt0000005", 6.2, 2606)).toDS()

    val result: Dataset[MovieRank] = CalculateData.getTopMovies(data, 2, 200)
    result.collect() should contain theSameElementsAs Seq(MovieRank("tt0000005", 6.2, 2606, 11.843717929922299),
      MovieRank("tt0000003", 6.5, 1809, 8.619337340565899))
  }

  test("get most credited returns correct data") {

    val topMovies = Seq("tt0000001")
    val titlePrincipals = Seq(("tt0000001", "nm1588970"),
      ("tt0000001", "nm1588971"),
      ("tt0000001", "nm0374658"),
      ("tt0000002", "nm0721526")).toDF("movieId", "castId").as[(String, String)]
    val nameBasics = Seq(("nm1588971", "Fred Astaire"),
        ("nm1588970", "Lauren Bacall"),
        ("nm0000003", "Brigitte Bardot"),
        ("nm0374658", "John Belushi")).toDF("castId", "primaryName").as[(String, String)]

    val result = CalculateData.getMostCredited(topMovies, titlePrincipals, nameBasics)
    result.collect() should contain allElementsOf Seq(("nm1588971", "Fred Astaire", "tt0000001"),
      ("nm1588970", "Lauren Bacall", "tt0000001"),
      ("nm0374658", "John Belushi", "tt0000001"))
  }

}

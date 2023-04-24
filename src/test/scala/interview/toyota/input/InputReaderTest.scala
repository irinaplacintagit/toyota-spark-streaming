package interview.toyota.input

import interview.toyota.model.MovieRating
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import setup.SharedSparkContext

class InputReaderTest extends AnyFunSuite  with Matchers with SharedSparkContext {

  test("getTitleRatings returns the correct data") {
    val data = InputReader.getTitleRatings("src/test/resources/title.test.ratings.tsv")

    data.collect() should contain theSameElementsAs Seq(MovieRating("tt0000001", 5.7, 1965),
      MovieRating("tt0000002", 5.8, 263),
      MovieRating("tt0000003", 6.5, 1809),
      MovieRating("tt0000004", 5.6, 178),
      MovieRating("tt0000005", 6.2, 2606))
  }

  test("getTitleRatings throws exception when path invalid") {
    val thrown = intercept [org.apache.spark.sql.AnalysisException] {
      InputReader.getTitleRatings("src/test/resources/title111.test.ratings.tsv")
    }
    assert(thrown.getMessage.startsWith("Path does not exist"))
  }

  test("getTitlePrincipals returns the correct data") {
    val data = InputReader.getTitlePrincipals("src/test/resources/title.test.principals.tsv")

    data.collect() should contain theSameElementsAs Seq(("tt0000001", "nm1588970"),
      ("tt0000001", "nm0005690"),
      ("tt0000001", "nm0374658"),
      ("tt0000002", "nm0721526"))
  }

  test("getNameBasics returns the correct data") {
    val data = InputReader.getNameBasics("src/test/resources/name.test.basics.tsv")
    data.show(false)

    data.collect() should contain theSameElementsAs Seq(("nm0000001", "Fred Astaire"),
      ("nm0000002", "Lauren Bacall"),
      ("nm0000003", "Brigitte Bardot"),
      ("nm0000004", "John Belushi"))
  }

  test("getTitleBasics returns the correct data") {
    val data = InputReader.getTitleBasics("src/test/resources/title.test.basics.tsv")

    data.collect() should contain theSameElementsAs Seq(("tt0000001", "Carmencita", "Carmencita"),
      ("tt0000002", "Le clown et ses chiens", "Le clown et ses chiens"),
      ("tt0000003", "Pauvre Pierrot", "Pauvre Pierrot"),
      ("tt0000004", "Un bon bock", "Un bon bock"))
  }
}

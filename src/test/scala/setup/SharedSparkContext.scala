package setup

import org.apache.spark.sql.SparkSession

trait SharedSparkContext {
  implicit val spark: SparkSession = {
    val session = SparkSession.builder()
      .config("spark.ui.showConsoleProgress", false)
      .master("local[*]")
      .getOrCreate()
    session.sparkContext.setLogLevel("ERROR")
    session
  }
}

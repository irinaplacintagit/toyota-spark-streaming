package interview.toyota.config

import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession

object SparkSetup {

  def getSparkSession(): SparkSession = {
    val sparkConf = new SparkConf()

    val sparkSession = SparkSession
      .builder()
      .config(sparkConf)
      .master("local")
      .appName("SparkStructuredPOC")
      .getOrCreate()
    sparkSession.sparkContext.setLogLevel("ERROR")

    sparkSession
  }

}

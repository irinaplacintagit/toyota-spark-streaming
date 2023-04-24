import sbt._

object Dependencies {
  val Versions: Map[String, String] = Map(
    "logback-contrib" -> "0.1.5",
    "sparkDeltaVersion" -> "1.0.0",
    "sparkVersion" -> "3.1.2"
  )

  lazy val logbackClassic = "ch.qos.logback" % "logback-classic" % "1.2.3"
  lazy val logbackJackson = "ch.qos.logback.contrib" % "logback-jackson" % Versions("logback-contrib")
  lazy val logbackJsonClassic = "ch.qos.logback.contrib" % "logback-json-classic" % Versions("logback-contrib")
  lazy val scalaLogging = "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2"
  lazy val scalaCollectionCompat = "org.scala-lang.modules" %% "scala-collection-compat" % "2.5.0"
  lazy val sparkDelta = "io.delta" %% "delta-core" % Versions("sparkDeltaVersion")
  lazy val sparkCore = "org.apache.spark" %% "spark-core" % Versions("sparkVersion")
  lazy val sparkSQL = "org.apache.spark" %% "spark-sql" % Versions("sparkVersion")
  lazy val scalaTest = "org.scalatest" %% "scalatest" % "3.2.9"

  val dependencies = Seq(
    logbackClassic,
    logbackJackson,
    logbackJsonClassic,
    scalaLogging,
    scalaCollectionCompat,
    sparkDelta,
    sparkCore,
    sparkSQL,
    scalaTest)
}


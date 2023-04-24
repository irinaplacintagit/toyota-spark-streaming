import Dependencies._
import sbt.Keys.libraryDependencies
import sbtrelease.ReleaseStateTransformations.{checkSnapshotDependencies, inquireVersions, runClean, runTest, setNextVersion, setReleaseVersion}

name := "toyota-spark-streaming"

scalaVersion := "2.12.12"

organization := "irina"

lazy val root = (project in file("."))
  .enablePlugins(BuildInfoPlugin)
  .settings(
    buildInfoKeys := Seq[BuildInfoKey](version),
    buildInfoPackage := "irina"
  )

libraryDependencies ++= dependencies

ThisBuild / scalacOptions ++= Seq(
  "-deprecation", // Emit warning and location for usages of deprecated APIs.
  "-explaintypes", // Explain type errors in more detail.
  "-unchecked", // Enable additional warnings where generated code depends on assumptions.
  "-Ywarn-dead-code", // Warn when dead code is identified.
  "-Ywarn-extra-implicit", // Warn when more than one implicit parameter section is defined.
  "-Ywarn-unused:implicits", // Warn if an implicit parameter is unused.
  "-Ywarn-unused:imports", // Warn if an import selector is not referenced.
  "-Ywarn-unused:locals", // Warn if a local definition is unused.
  "-Ywarn-unused:params", // Warn if a value parameter is unused.
  "-Ywarn-unused:patvars", // Warn if a variable bound in a pattern is unused.
  "-Ywarn-unused:privates", // Warn if a private member is unused.
  "-Ywarn-value-discard" // Warn when non-Unit expression results are unused.
)

ThisBuild / resolvers ++= Seq(
  Resolver.defaultLocal,
  Resolver.mavenLocal
)

assembly / assemblyMergeStrategy := {
  case PathList("META-INF", _) => MergeStrategy.discard
  case PathList("module-info.class") => MergeStrategy.discard
  case x if x.endsWith("/module-info.class") => MergeStrategy.discard
  case _ => MergeStrategy.first
}

Compile / assembly / artifact := {
  val art = (Compile / assembly / artifact).value
  art.withClassifier(Some("assembly"))
}

addArtifact(Compile / assembly / artifact, assembly)

credentials += Credentials(sys.env.get("SBT_CREDENTIALS") match {
  case None => Path.userHome / ".sbt" / "credentials"
  case Some(file) => new File(file)
})

// Release plugin
releaseVersionBump := sbtrelease.Version.Bump.Minor

releaseProcess := Seq[ReleaseStep](
  checkSnapshotDependencies,
  inquireVersions,
  runClean,
  runTest,
  setReleaseVersion,
  setNextVersion
)


addCommandAlias("coverageAll", "; coverage; test")

Test / parallelExecution := false
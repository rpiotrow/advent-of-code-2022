import Dependencies._
import TestDependencies._

ThisBuild / scalaVersion     := "3.2.1"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "io.github.rpiotrow"

val RegressionConfig = config("regression") extend IntegrationTest

lazy val root = project
  .in(file("."))
  .configs(RegressionConfig)
  .settings(
    name := "advent-of-code-2022",
    libraryDependencies ++= Seq(zio, zioStreams, scopt, zioTest, zioTestSbt),
    inConfig(RegressionConfig)(Defaults.testSettings),
    RegressionConfig  / classpathConfiguration := Test
  )

testFrameworks += new TestFramework("zio.test.sbt.ZTestFramework")

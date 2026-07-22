ThisBuild / scalaVersion := "2.13.15"
ThisBuild / organization := "com.github.chrishuang"
ThisBuild / version := "0.1.0-example"

lazy val example = (project in file("."))
  .settings(
    name := "quality-gates-example",
    libraryDependencies += "org.scalameta" %% "munit" % "1.0.2" % Test,
    testFrameworks += new TestFramework("munit.Framework")
  )

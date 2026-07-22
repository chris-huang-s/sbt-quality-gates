ThisBuild / organization := "com.github.chrishuang"
ThisBuild / version := "0.1.0"
ThisBuild / scalaVersion := "2.12.20"

lazy val `sbt-quality-gates` = (project in file("."))
  .enablePlugins(SbtPlugin)
  .settings(
    name := "sbt-quality-gates",
    sbtPlugin := true,
    addSbtPlugin("org.scalameta" % "sbt-scalafmt" % "2.5.2"),
    addSbtPlugin("ch.epfl.scala" % "sbt-scalafix" % "0.13.0")
  )

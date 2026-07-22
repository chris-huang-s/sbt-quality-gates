package com.github.chrishuang.sbtqualitygates

import sbt._
import sbt.Keys._
import org.scalafmt.sbt.ScalafmtPlugin
import org.scalafmt.sbt.ScalafmtPlugin.autoImport._
import scalafix.sbt.ScalafixPlugin
import scalafix.sbt.ScalafixPlugin.autoImport._

/** Opinionated quality gate that runs Scalafmt, Scalafix, then tests. */
object SbtQualityGatesPlugin extends AutoPlugin {

  override def requires: Plugins = ScalafmtPlugin && ScalafixPlugin
  override def trigger: PluginTrigger = allRequirements

  object autoImport {
    val qualityGates = taskKey[Unit](
      "Run Scalafmt check, Scalafix check, and test as a single quality gate."
    )
  }

  import autoImport._

  override def projectSettings: Seq[Setting[_]] = Seq(
    semanticdbEnabled := true,
    semanticdbVersion := scalafixSemanticdb.revision,
    qualityGates := {
      val log = streams.value.log
      log.info("qualityGates: scalafmtCheckAll")
      scalafmtCheckAll.value
      log.info("qualityGates: scalafixAll --check")
      scalafixAll.toTask(" --check").value
      log.info("qualityGates: test")
      (Test / test).value
      log.info("qualityGates: all gates passed")
    }
  )
}

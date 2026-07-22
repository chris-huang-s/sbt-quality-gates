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
      "Run Scalafmt check, Scalafix check, and optionally test as a single quality gate."
    )

    /** When true, `qualityGates` skips the test step after format/lint checks. */
    val qualityGatesSkipTests = settingKey[Boolean](
      "If true, qualityGates runs only Scalafmt and Scalafix checks (default: false)."
    )

    /** When true, Scalafix --check is included in qualityGates (default: true). */
    val qualityGatesScalafix = settingKey[Boolean](
      "If true, qualityGates runs scalafixAll --check (default: true)."
    )
  }

  import autoImport._

  override def projectSettings: Seq[Setting[_]] = Seq(
    semanticdbEnabled := true,
    semanticdbVersion := scalafixSemanticdb.revision,
    qualityGatesSkipTests := false,
    qualityGatesScalafix := true,
    qualityGates := {
      val log = streams.value.log
      val skipTests = qualityGatesSkipTests.value
      val runScalafix = qualityGatesScalafix.value

      log.info("qualityGates: scalafmtCheckAll")
      scalafmtCheckAll.value

      if (runScalafix) {
        log.info("qualityGates: scalafixAll --check")
        scalafixAll.toTask(" --check").value
      } else {
        log.info("qualityGates: scalafix skipped (qualityGatesScalafix := false)")
      }

      if (skipTests) {
        log.info("qualityGates: test skipped (qualityGatesSkipTests := true)")
      } else {
        log.info("qualityGates: test")
        (Test / test).value
      }

      log.info("qualityGates: all gates passed")
    }
  )
}

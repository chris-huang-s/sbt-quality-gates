# sbt-quality-gates

Reusable sbt quality gates and GitHub Actions for Scala projects.

## Problem

Scala CI setups often copy the same checklist into every repository: format with Scalafmt, lint with Scalafix, then run tests. That duplication drifts quickly—plugin versions diverge, gate order differs, and pull requests fail for inconsistent reasons.

## Approach

This repository packages a small, opinionated stack:

1. **`SbtQualityGatesPlugin`** — an sbt AutoPlugin that wires `qualityGates` to run Scalafmt check, Scalafix check, and `test` in a fixed order.
2. **Composite GitHub Action** — a reusable workflow step that installs a JDK, caches coursier/sbt, and invokes `sbt qualityGates`.
3. **Example project** — a minimal Scala 2.13 module that consumes the plugin so the gates can be exercised end-to-end in CI.

Dependencies stay light: sbt 1.x, Scalafmt, Scalafix, and MUnit for the sample tests.

## Usage

### As an sbt plugin (local / published)

In `project/plugins.sbt`:

```scala
addSbtPlugin("com.github.chrishuang" % "sbt-quality-gates" % "0.1.0")
```

In `build.sbt` (or rely on auto-trigger):

```scala
enablePlugins(SbtQualityGatesPlugin)
```

Run the full gate suite:

```bash
sbt qualityGates
```

Individual tasks remain available:

```bash
sbt scalafmtCheckAll
sbt "scalafixAll --check"
sbt test
```

### As a GitHub Action

```yaml
jobs:
  quality:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - uses: chris-huang-s/sbt-quality-gates/.github/actions/quality-gates@main
```

The action expects an sbt project at the repository root (or set `working-directory`).


### Plugin options

Override defaults in `build.sbt` when a repo needs a narrower gate:

```scala
qualityGatesSkipTests := false   // set true to run format+lint only
qualityGatesScalafix  := true    // set false to skip Scalafix --check
```

| Setting | Default | Effect |
| --- | --- | --- |
| `qualityGatesSkipTests` | `false` | Skip the `test` step after format/lint |
| `qualityGatesScalafix` | `true` | Include `scalafixAll --check` in the gate |

SemanticDB is enabled automatically so Scalafix can run without extra boilerplate.

### Action inputs

| Input | Default | Description |
| --- | --- | --- |
| `java-version` | `21` | Temurin JDK major version |
| `working-directory` | `.` | Directory that contains the sbt build |
| `sbt-args` | _(empty)_ | Extra arguments passed to sbt before `qualityGates` |

Example with a non-root build and JDK 17:

```yaml
- uses: chris-huang-s/sbt-quality-gates/.github/actions/quality-gates@main
  with:
    java-version: "17"
    working-directory: example
    sbt-args: "-batch"
```

### Example module

```bash
cd example
sbt qualityGates
```

## CI

Pull requests and pushes to `main` / `develop` run `.github/workflows/ci.yml`, which:

- runs a dedicated **Scalafmt** gate on the plugin and `example/` projects first
- builds the plugin with `sbt compile` on a **JDK 17 / 21** matrix
- runs `qualityGates` inside `example/` on the same JDK matrix

Gate failures fail the job; green means format, lint, and tests all passed on supported JDKs.

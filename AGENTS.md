# Agent guide for buildplan-maven-plugin

This file is for automated agents and human contributors working on this repository. It records
what the code and the POMs do not make obvious.

## Layout

- One module with the goals `list` (`ListMojo`), `list-phase` (`ListPhaseMojo`), `list-plugin`
  (`ListPluginMojo`) and `report` (`ReportMojo`), in `org.codehaus.mojo.buildplan`.
  `AbstractLifecycleMojo` computes the lifecycle executions they share; the table output is in
  `display`.
- The site is built from `src/site`.

## Build gates

- Main code must use the Java 8 API: mojo-parent sets `mojo.java.target` to 8 and turns it into
  `maven.compiler.release`; this POM does not override it.
- Spotless runs on JDK 17 and later only. It applies the formatting in `process-sources` on a local
  build and checks it when the `CI` environment variable is set. It formats Java with
  palantir-java-format and also reformats Markdown, so run `mvn spotless:apply` after editing the
  README or this file.
- `license-maven-plugin` checks the file headers during the build. The Java files carry an Apache
  2.0 header owned by Jean-Christophe Gay; keep each file's header as it is. New Java files take
  the MojoHaus header in `src/license/mojohaus-header.txt`; list each one under the second
  `licenseSet` and in the first set's excludes in `pom.xml`. `src/it` and `src/site` are excluded.
- The plugin compiles against Maven 3.9.16 (`mavenVersion`) and declares a literal
  `<prerequisites><maven>3.9.0</maven>`; keep the two apart, so raising `mavenVersion` does not
  raise the prerequisite.
- `.github/workflows/maven.yml` calls the MojoHaus `maven-verify.yml` workflow in `mojohaus/.github`, which
  wraps the shared Maven verify workflow with the MojoHaus matrix: Linux on JDK 8, 21 and 25, Windows and
  macOS on fewer JDKs, each on Maven 3.10 and on Maven 4. Branches in this repository build under the push
  event, so a pull request from a branch here shows the push run.
- `mvnw` pins the Maven version of the wrapper (3.9.16).

## Tests

- JUnit 5 and AssertJ. The unit tests (`*Test`) are in `src/test/java`, next to the code they test.
- The integration tests are maven-invoker-plugin projects in `src/it/<name>`, with `invoker.properties`,
  an optional `test.properties` (`-D` values) and a `verify.groovy` that asserts on `build.log`. They
  run in the `run-its` profile, which the CI workflow activates with `-P ?run-its`. A plain `mvn verify`
  runs none of them:

  ```shell
  mvn -B -P run-its verify -Dinvoker.test=list-with-default-cli
  ```
- Maven 4 reports phases under its own names (`after:resources` for `process-resources`); `PhaseNames`
  maps them back, so assertions on the phase column use the Maven 3 names on both lines. Run the
  integration tests on a Maven 4 install too before changing anything in `display`:

  ```shell
  ~/.sdkman/candidates/maven/4.0.0-rc-7/bin/mvn -B -P run-its verify -DskipTests
  ```
- The ITs keep their own repository in `target/local-repo`, so nothing they install reaches your
  local repository. `src/it/settings.xml` lets them read your local repository first, and the
  plugins only the fixtures use, at their pinned versions and at the default lifecycle versions of
  Maven 3.x and Maven 4, are listed as `extraArtifacts` of the invoker `install` goal: the main build
  resolves them into your local repository, which CI caches, and the install goal copies them over.
  When a fixture needs a plugin version that is not on that list, add it there, or every CI job
  downloads it from Central again.
- Add an integration test only for what needs a real Maven build, such as the lifecycle computed for
  a reactor or the site report; prefer a unit test otherwise.

## Dependencies

- Deliberate pins in `.github/dependabot.yml`: `maven-artifact`, `maven-core` and
  `maven-plugin-api` stay below 3.10.0, the Maven 3.9.x line being the plugin baseline.

## Pull requests

- Pull requests are squash-, merge- or rebase-merged. Release Drafter builds the release notes from
  the titles and labels, so the title should describe the change for a user.


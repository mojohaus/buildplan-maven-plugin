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

- Main code must use the Java 8 API: `mojo.java.target` is 8 and mojo-parent turns it into
  `maven.compiler.release`.
- Spotless runs on JDK 17 and later only. It applies the formatting in `process-sources` on a local
  build and checks it when the `CI` environment variable is set. It formats Java with
  palantir-java-format and also reformats Markdown, so run `mvn spotless:apply` after editing the
  README or this file.
- `license-maven-plugin` checks the file headers during the build. The Java files carry an Apache
  2.0 header owned by Jean-Christophe Gay; keep each file's header as it is, and copy that header
  into new Java files. Fixtures under `src/test/resources-its` and `src/site` are excluded.
- The plugin compiles against Maven 3.9.16 (`mavenVersion`) and declares a literal
  `<prerequisites><maven>3.9.0</maven>`; keep the two apart, so raising `mavenVersion` does not
  raise the prerequisite.
- `.github/workflows/maven.yml` calls the shared `apache/maven-gh-actions-shared`
  `maven-verify.yml@v5` workflow. Branches in this repository build under the push event.
- `mvnw` pins the Maven version of the wrapper (3.9.16).

## Tests

- JUnit 5 and AssertJ. The unit tests (`*Test`) are in `src/test/java`, next to the code they test.
- The integration tests (`*IT`) use the Maven Integration Testing Framework (ITF) for JUnit 5 and run
  under Failsafe. Each `@MavenTest` method builds a fixture project from
  `src/test/resources-its/<IT class>/<nested class>/<project name>`, where the project name is the
  test method name or `maven_project` for a `@MavenProject` shared by a nested class, and asserts
  on the output with `MavenITAssertions`. `itf-maven-plugin` installs the plugin and copies the
  fixtures in `pre-integration-test`.
- Run one integration test class (the unit tests are skipped by the `-Dtest` filter):

  ```shell
  mvn -B verify -Dtest=NoSuchTest -Dsurefire.failIfNoSpecifiedTests=false -Dit.test=ListPluginIT
  ```
- Add an integration test only for what needs a real Maven build, such as the lifecycle computed for
  a reactor or the site report; prefer a unit test otherwise.

## Dependencies

- Deliberate pins in `.github/dependabot.yml`: `maven-artifact`, `maven-core` and
  `maven-plugin-api` stay below 3.10.0, the Maven 3.9.x line being the plugin baseline.

## Pull requests

- Pull requests are squash-, merge- or rebase-merged. Release Drafter builds the release notes from
  the titles and labels, so the title should describe the change for a user.


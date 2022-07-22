/*
 * Copyright (C) 2012 Jean-Christophe Gay (contact@jeanchristophegay.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.codehaus.mojo.buildplan;

import static com.soebes.itf.extension.assertj.MavenITAssertions.assertThat;

import com.soebes.itf.jupiter.extension.MavenGoal;
import com.soebes.itf.jupiter.extension.MavenJupiterExtension;
import com.soebes.itf.jupiter.extension.MavenProject;
import com.soebes.itf.jupiter.extension.MavenTest;
import com.soebes.itf.jupiter.extension.SystemProperty;
import com.soebes.itf.jupiter.maven.MavenExecutionResult;
import org.junit.jupiter.api.Nested;

@MavenJupiterExtension
class ListPhaseIT {

    @Nested
    @MavenProject
    @MavenGoal("${project.groupId}:${project.artifactId}:${project.version}:list-phase")
    class WithoutPluginDeclaration {

        @MavenTest
        void list_phase_with_default_cli(MavenExecutionResult result) {
            assertThat(result).isSuccessful()
                .out()
                .plain()
                .containsSequence(
                    "process-resources ---------------------------------------------------",
                    "    + maven-resources-plugin | default-resources     | resources    ",
                    "compile -------------------------------------------------------------",
                    "    + maven-compiler-plugin  | default-compile       | compile      ",
                    "process-test-resources ----------------------------------------------",
                    "    + maven-resources-plugin | default-testResources | testResources",
                    "test-compile --------------------------------------------------------",
                    "    + maven-compiler-plugin  | default-testCompile   | testCompile  ",
                    "test ----------------------------------------------------------------",
                    "    + maven-surefire-plugin  | default-test          | test         ",
                    "package -------------------------------------------------------------",
                    "    + maven-jar-plugin       | default-jar           | jar          ",
                    "install -------------------------------------------------------------",
                    "    + maven-install-plugin   | default-install       | install      ",
                    "deploy --------------------------------------------------------------",
                    "    + maven-deploy-plugin    | default-deploy        | deploy       "
                );
        }

        @MavenTest
        @SystemProperty(value = "buildplan.tasks", content = "clean,versions:set,install")
        @SystemProperty(value = "buildplan.showAllPhases", content = "true")
        @SystemProperty(value = "buildplan.showLifecycles", content = "true")
        void list_phase_with_defined_tasks_and_configuration_with_cli(MavenExecutionResult result) {
            assertThat(result).isSuccessful()
                .out()
                .plain()
                .containsSequence(
                    "[clean]",
                    "pre-clean -----------------------------------------------------------",
                    "clean ---------------------------------------------------------------",
                    "    + maven-clean-plugin     | default-clean         | clean        ",
                    "post-clean ----------------------------------------------------------",
                    "",
                    "[]",
                    "<no phase> ----------------------------------------------------------",
                    "    + versions-maven-plugin  | default-cli           | set          ",
                    "",
                    "[default]",
                    "validate ------------------------------------------------------------",
                    "initialize ----------------------------------------------------------",
                    "generate-sources ----------------------------------------------------",
                    "process-sources -----------------------------------------------------",
                    "generate-resources --------------------------------------------------",
                    "process-resources ---------------------------------------------------",
                    "    + maven-resources-plugin | default-resources     | resources    ",
                    "compile -------------------------------------------------------------",
                    "    + maven-compiler-plugin  | default-compile       | compile      ",
                    "process-classes -----------------------------------------------------",
                    "generate-test-sources -----------------------------------------------",
                    "process-test-sources ------------------------------------------------",
                    "generate-test-resources ---------------------------------------------",
                    "process-test-resources ----------------------------------------------",
                    "    + maven-resources-plugin | default-testResources | testResources",
                    "test-compile --------------------------------------------------------",
                    "    + maven-compiler-plugin  | default-testCompile   | testCompile  ",
                    "process-test-classes ------------------------------------------------",
                    "test ----------------------------------------------------------------",
                    "    + maven-surefire-plugin  | default-test          | test         ",
                    "prepare-package -----------------------------------------------------",
                    "package -------------------------------------------------------------",
                    "    + maven-jar-plugin       | default-jar           | jar          ",
                    "pre-integration-test ------------------------------------------------",
                    "integration-test ----------------------------------------------------",
                    "post-integration-test -----------------------------------------------",
                    "verify --------------------------------------------------------------",
                    "install -------------------------------------------------------------",
                    "    + maven-install-plugin   | default-install       | install      ",
                    "deploy --------------------------------------------------------------"
                );
        }

        @MavenTest
        @SystemProperty(value = "buildplan.skip", content = "true")
        void skip_list_phase(MavenExecutionResult result) {
            assertThat(result).isSuccessful()
                .out()
                .info()
                .contains("Skipping build plan execution.");
        }

        @MavenTest
        @SystemProperty(value = "buildplan.tasks", content = "clean,deploy")
        @SystemProperty(value = "buildplan.showAllPhases", content = "true")
        void list_phase_show_all_phases(MavenExecutionResult result) {
            assertThat(result).isSuccessful()
                .out()
                .plain()
                .containsSequence(
                    "pre-clean -----------------------------------------------------------",
                    "clean ---------------------------------------------------------------",
                    "    + maven-clean-plugin     | default-clean         | clean        ",
                    "post-clean ----------------------------------------------------------",
                    "validate ------------------------------------------------------------",
                    "initialize ----------------------------------------------------------",
                    "generate-sources ----------------------------------------------------",
                    "process-sources -----------------------------------------------------",
                    "generate-resources --------------------------------------------------",
                    "process-resources ---------------------------------------------------",
                    "    + maven-resources-plugin | default-resources     | resources    ",
                    "compile -------------------------------------------------------------",
                    "    + maven-compiler-plugin  | default-compile       | compile      ",
                    "process-classes -----------------------------------------------------",
                    "generate-test-sources -----------------------------------------------",
                    "process-test-sources ------------------------------------------------",
                    "generate-test-resources ---------------------------------------------",
                    "process-test-resources ----------------------------------------------",
                    "    + maven-resources-plugin | default-testResources | testResources",
                    "test-compile --------------------------------------------------------",
                    "    + maven-compiler-plugin  | default-testCompile   | testCompile  ",
                    "process-test-classes ------------------------------------------------",
                    "test ----------------------------------------------------------------",
                    "    + maven-surefire-plugin  | default-test          | test         ",
                    "prepare-package -----------------------------------------------------",
                    "package -------------------------------------------------------------",
                    "    + maven-jar-plugin       | default-jar           | jar          ",
                    "pre-integration-test ------------------------------------------------",
                    "integration-test ----------------------------------------------------",
                    "post-integration-test -----------------------------------------------",
                    "verify --------------------------------------------------------------",
                    "install -------------------------------------------------------------",
                    "    + maven-install-plugin   | default-install       | install      ",
                    "deploy --------------------------------------------------------------",
                    "    + maven-deploy-plugin    | default-deploy        | deploy       "
                );
        }

        @MavenTest
        @SystemProperty(value = "buildplan.tasks", content = "clean,deploy")
        @SystemProperty(value = "buildplan.showLifecycles", content = "true")
        void list_phase_show_lifecycle(MavenExecutionResult result) {
            assertThat(result).isSuccessful()
                .out()
                .plain()
                .containsSequence(
                    "[clean]",
                    "clean ---------------------------------------------------------------",
                    "    + maven-clean-plugin     | default-clean         | clean        ",
                    "",
                    "[default]",
                    "process-resources ---------------------------------------------------",
                    "    + maven-resources-plugin | default-resources     | resources    ",
                    "compile -------------------------------------------------------------",
                    "    + maven-compiler-plugin  | default-compile       | compile      ",
                    "process-test-resources ----------------------------------------------",
                    "    + maven-resources-plugin | default-testResources | testResources",
                    "test-compile --------------------------------------------------------",
                    "    + maven-compiler-plugin  | default-testCompile   | testCompile  ",
                    "test ----------------------------------------------------------------",
                    "    + maven-surefire-plugin  | default-test          | test         ",
                    "package -------------------------------------------------------------",
                    "    + maven-jar-plugin       | default-jar           | jar          ",
                    "install -------------------------------------------------------------",
                    "    + maven-install-plugin   | default-install       | install      ",
                    "deploy --------------------------------------------------------------",
                    "    + maven-deploy-plugin    | default-deploy        | deploy       "
                );
        }
    }

    @Nested
    class WithPluginDeclaration {

        @MavenTest
        @MavenGoal("validate")
        void list_phase_with_plugin_defined_in_pom(MavenExecutionResult result) {
            assertThat(result).isSuccessful()
                .out()
                .plain()
                .containsSequence(
                    "process-resources ---------------------------------------------------",
                    "    + maven-resources-plugin | default-resources     | resources    ",
                    "compile -------------------------------------------------------------",
                    "    + maven-compiler-plugin  | default-compile       | compile      ",
                    "process-test-resources ----------------------------------------------",
                    "    + maven-resources-plugin | default-testResources | testResources",
                    "test-compile --------------------------------------------------------",
                    "    + maven-compiler-plugin  | default-testCompile   | testCompile  ",
                    "test ----------------------------------------------------------------",
                    "    + maven-surefire-plugin  | default-test          | test         ",
                    "package -------------------------------------------------------------",
                    "    + maven-jar-plugin       | default-jar           | jar          ",
                    "install -------------------------------------------------------------",
                    "    + maven-install-plugin   | default-install       | install      ",
                    "deploy --------------------------------------------------------------",
                    "    + maven-deploy-plugin    | default-deploy        | deploy       "
                );
        }

        @MavenTest
        @MavenGoal("validate")
        void skip_list_phase_with_plugin_defined_in_pom(MavenExecutionResult result) {
            assertThat(result).isSuccessful()
                .out()
                .info()
                .contains("Skipping build plan execution.");
        }
    }
}

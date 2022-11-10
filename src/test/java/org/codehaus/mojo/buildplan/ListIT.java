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
import java.io.File;
import org.junit.jupiter.api.Nested;

@MavenJupiterExtension
class ListIT {

    @Nested
    @MavenProject
    @MavenGoal("${project.groupId}:${project.artifactId}:${project.version}:list")
    class MultiModuleProject {

        @MavenTest
        void list_with_default_cli(MavenExecutionResult result) {
            assertThat(result)
                    .isSuccessful()
                    .out()
                    .plain()
                    .containsSequence(
                            "[INFO] Build Plan for list-multimodule: ",
                            "--------------------------------------------------------------------",
                            "PHASE   | PLUGIN               | VERSION | GOAL    | EXECUTION ID   ",
                            "--------------------------------------------------------------------",
                            "install | maven-install-plugin | 2.4     | install | default-install",
                            "deploy  | maven-deploy-plugin  | 2.7     | deploy  | default-deploy ")
                    .containsSequence(
                            "[INFO] Build Plan for list-multimodule-module-a: ",
                            "-------------------------------------------------------------------------------------------------",
                            "PHASE                  | PLUGIN                 | VERSION | GOAL          | EXECUTION ID         ",
                            "-------------------------------------------------------------------------------------------------",
                            "process-resources      | maven-resources-plugin | 2.6     | resources     | default-resources    ",
                            "compile                | maven-compiler-plugin  | 3.1     | compile       | default-compile      ",
                            "process-test-resources | maven-resources-plugin | 2.6     | testResources | default-testResources",
                            "test-compile           | maven-compiler-plugin  | 3.1     | testCompile   | default-testCompile  ",
                            "test                   | maven-surefire-plugin  | 2.12.4  | test          | default-test         ",
                            "package                | maven-jar-plugin       | 2.4     | jar           | default-jar          ",
                            "install                | maven-install-plugin   | 2.4     | install       | default-install      ",
                            "deploy                 | maven-deploy-plugin    | 2.7     | deploy        | default-deploy       ")
                    .containsSequence(
                            "[INFO] Build Plan for list-multimodule-module-b: ",
                            "----------------------------------------------------------------------------------------------------",
                            "PHASE                  | PLUGIN                 | VERSION | GOAL             | EXECUTION ID         ",
                            "----------------------------------------------------------------------------------------------------",
                            "process-resources      | maven-resources-plugin | 2.6     | resources        | default-resources    ",
                            "compile                | maven-compiler-plugin  | 3.1     | compile          | default-compile      ",
                            "process-test-resources | maven-resources-plugin | 2.6     | testResources    | default-testResources",
                            "test-compile           | maven-compiler-plugin  | 3.1     | testCompile      | default-testCompile  ",
                            "test                   | maven-surefire-plugin  | 2.12.4  | test             | default-test         ",
                            "package                | maven-jar-plugin       | 2.4     | jar              | default-jar          ",
                            "integration-test       | maven-failsafe-plugin  | 2.19.1  | integration-test | default              ",
                            "verify                 | maven-failsafe-plugin  | 2.19.1  | verify           | default              ",
                            "install                | maven-install-plugin   | 2.4     | install          | default-install      ",
                            "deploy                 | maven-deploy-plugin    | 2.7     | deploy           | default-deploy       ");
        }

        @MavenTest
        @SystemProperty(value = "buildplan.tasks", content = "clean,versions:set,install")
        @SystemProperty(value = "buildplan.showLifecycles", content = "true")
        void list_with_custom_tasks_and_show_lifecycles(MavenExecutionResult result) {
            assertThat(result)
                    .isSuccessful()
                    .out()
                    .plain()
                    .containsSequence(
                            "[INFO] Build Plan for list-multimodule: ",
                            "---------------------------------------------------------------------------------",
                            "LIFECYCLE | PHASE   | PLUGIN                | VERSION | GOAL    | EXECUTION ID   ",
                            "---------------------------------------------------------------------------------",
                            "clean     | clean   | maven-clean-plugin    | 2.5     | clean   | default-clean  ",
                            "          |         | versions-maven-plugin | 2.13.0  | set     | default-cli    ",
                            "default   | install | maven-install-plugin  | 2.4     | install | default-install")
                    .containsSequence(
                            "[INFO] Build Plan for list-multimodule-module-a: ",
                            "-------------------------------------------------------------------------------------------------------------",
                            "LIFECYCLE | PHASE                  | PLUGIN                 | VERSION | GOAL          | EXECUTION ID         ",
                            "-------------------------------------------------------------------------------------------------------------",
                            "clean     | clean                  | maven-clean-plugin     | 2.5     | clean         | default-clean        ",
                            "          |                        | versions-maven-plugin  | 2.13.0  | set           | default-cli          ",
                            "default   | process-resources      | maven-resources-plugin | 2.6     | resources     | default-resources    ",
                            "default   | compile                | maven-compiler-plugin  | 3.1     | compile       | default-compile      ",
                            "default   | process-test-resources | maven-resources-plugin | 2.6     | testResources | default-testResources",
                            "default   | test-compile           | maven-compiler-plugin  | 3.1     | testCompile   | default-testCompile  ",
                            "default   | test                   | maven-surefire-plugin  | 2.12.4  | test          | default-test         ",
                            "default   | package                | maven-jar-plugin       | 2.4     | jar           | default-jar          ",
                            "default   | install                | maven-install-plugin   | 2.4     | install       | default-install      ")
                    .containsSequence(
                            "[INFO] Build Plan for list-multimodule-module-b: ",
                            "----------------------------------------------------------------------------------------------------------------",
                            "LIFECYCLE | PHASE                  | PLUGIN                 | VERSION | GOAL             | EXECUTION ID         ",
                            "----------------------------------------------------------------------------------------------------------------",
                            "clean     | clean                  | maven-clean-plugin     | 2.5     | clean            | default-clean        ",
                            "          |                        | versions-maven-plugin  | 2.13.0  | set              | default-cli          ",
                            "default   | process-resources      | maven-resources-plugin | 2.6     | resources        | default-resources    ",
                            "default   | compile                | maven-compiler-plugin  | 3.1     | compile          | default-compile      ",
                            "default   | process-test-resources | maven-resources-plugin | 2.6     | testResources    | default-testResources",
                            "default   | test-compile           | maven-compiler-plugin  | 3.1     | testCompile      | default-testCompile  ",
                            "default   | test                   | maven-surefire-plugin  | 2.12.4  | test             | default-test         ",
                            "default   | package                | maven-jar-plugin       | 2.4     | jar              | default-jar          ",
                            "default   | integration-test       | maven-failsafe-plugin  | 2.19.1  | integration-test | default              ",
                            "default   | verify                 | maven-failsafe-plugin  | 2.19.1  | verify           | default              ",
                            "default   | install                | maven-install-plugin   | 2.4     | install          | default-install      ");
        }

        @MavenTest
        @SystemProperty(value = "buildplan.outputFile", content = "list.txt")
        void list_with_multiple_outputfile(MavenExecutionResult result) {
            assertThat(result).isSuccessful();

            assertThat(new File(result.getMavenProjectResult().getTargetProjectDirectory(), "list.txt"))
                    .hasContent("\n"
                            + "Build Plan for list-multimodule: \n"
                            + "--------------------------------------------------------------------\n"
                            + "PHASE   | PLUGIN               | VERSION | GOAL    | EXECUTION ID   \n"
                            + "--------------------------------------------------------------------\n"
                            + "install | maven-install-plugin | 2.4     | install | default-install\n"
                            + "deploy  | maven-deploy-plugin  | 2.7     | deploy  | default-deploy \n");
            assertThat(new File(result.getMavenProjectResult().getTargetProjectDirectory(), "module-a/list.txt"))
                    .hasContent(
                            "\n"
                                    + "Build Plan for list-multimodule-module-a: \n"
                                    + "-------------------------------------------------------------------------------------------------\n"
                                    + "PHASE                  | PLUGIN                 | VERSION | GOAL          | EXECUTION ID         \n"
                                    + "-------------------------------------------------------------------------------------------------\n"
                                    + "process-resources      | maven-resources-plugin | 2.6     | resources     | default-resources    \n"
                                    + "compile                | maven-compiler-plugin  | 3.1     | compile       | default-compile      \n"
                                    + "process-test-resources | maven-resources-plugin | 2.6     | testResources | default-testResources\n"
                                    + "test-compile           | maven-compiler-plugin  | 3.1     | testCompile   | default-testCompile  \n"
                                    + "test                   | maven-surefire-plugin  | 2.12.4  | test          | default-test         \n"
                                    + "package                | maven-jar-plugin       | 2.4     | jar           | default-jar          \n"
                                    + "install                | maven-install-plugin   | 2.4     | install       | default-install      \n"
                                    + "deploy                 | maven-deploy-plugin    | 2.7     | deploy        | default-deploy       \n");

            assertThat(new File(result.getMavenProjectResult().getTargetProjectDirectory(), "module-b/list.txt"))
                    .hasContent(
                            "\n"
                                    + "Build Plan for list-multimodule-module-b: \n"
                                    + "----------------------------------------------------------------------------------------------------\n"
                                    + "PHASE                  | PLUGIN                 | VERSION | GOAL             | EXECUTION ID         \n"
                                    + "----------------------------------------------------------------------------------------------------\n"
                                    + "process-resources      | maven-resources-plugin | 2.6     | resources        | default-resources    \n"
                                    + "compile                | maven-compiler-plugin  | 3.1     | compile          | default-compile      \n"
                                    + "process-test-resources | maven-resources-plugin | 2.6     | testResources    | default-testResources\n"
                                    + "test-compile           | maven-compiler-plugin  | 3.1     | testCompile      | default-testCompile  \n"
                                    + "test                   | maven-surefire-plugin  | 2.12.4  | test             | default-test         \n"
                                    + "package                | maven-jar-plugin       | 2.4     | jar              | default-jar          \n"
                                    + "integration-test       | maven-failsafe-plugin  | 2.19.1  | integration-test | default              \n"
                                    + "verify                 | maven-failsafe-plugin  | 2.19.1  | verify           | default              \n"
                                    + "install                | maven-install-plugin   | 2.4     | install          | default-install      \n"
                                    + "deploy                 | maven-deploy-plugin    | 2.7     | deploy           | default-deploy       \n");
        }

        @MavenTest
        @SystemProperty(value = "buildplan.outputFile", content = "${session.executionRootDirectory}/list-single.txt")
        @SystemProperty(value = "buildplan.appendOutput", content = "true")
        void list_with_single_outputfile(MavenExecutionResult result) {
            assertThat(result)
                    .isSuccessful()
                    .out()
                    .info()
                    .contains("Wrote build plan output to: "
                            + result.getMavenProjectResult()
                                    .getTargetBaseDirectory()
                                    .getAbsolutePath()
                            + "/project/list-single.txt");

            assertThat(new File(result.getMavenProjectResult().getTargetBaseDirectory(), "/project/list-single.txt"))
                    .hasContent(
                            "\n"
                                    + "Build Plan for list-multimodule: \n"
                                    + "--------------------------------------------------------------------\n"
                                    + "PHASE   | PLUGIN               | VERSION | GOAL    | EXECUTION ID   \n"
                                    + "--------------------------------------------------------------------\n"
                                    + "install | maven-install-plugin | 2.4     | install | default-install\n"
                                    + "deploy  | maven-deploy-plugin  | 2.7     | deploy  | default-deploy \n"
                                    + "\n"
                                    + "Build Plan for list-multimodule-module-a: \n"
                                    + "-------------------------------------------------------------------------------------------------\n"
                                    + "PHASE                  | PLUGIN                 | VERSION | GOAL          | EXECUTION ID         \n"
                                    + "-------------------------------------------------------------------------------------------------\n"
                                    + "process-resources      | maven-resources-plugin | 2.6     | resources     | default-resources    \n"
                                    + "compile                | maven-compiler-plugin  | 3.1     | compile       | default-compile      \n"
                                    + "process-test-resources | maven-resources-plugin | 2.6     | testResources | default-testResources\n"
                                    + "test-compile           | maven-compiler-plugin  | 3.1     | testCompile   | default-testCompile  \n"
                                    + "test                   | maven-surefire-plugin  | 2.12.4  | test          | default-test         \n"
                                    + "package                | maven-jar-plugin       | 2.4     | jar           | default-jar          \n"
                                    + "install                | maven-install-plugin   | 2.4     | install       | default-install      \n"
                                    + "deploy                 | maven-deploy-plugin    | 2.7     | deploy        | default-deploy       \n"
                                    + "\n"
                                    + "Build Plan for list-multimodule-module-b: \n"
                                    + "----------------------------------------------------------------------------------------------------\n"
                                    + "PHASE                  | PLUGIN                 | VERSION | GOAL             | EXECUTION ID         \n"
                                    + "----------------------------------------------------------------------------------------------------\n"
                                    + "process-resources      | maven-resources-plugin | 2.6     | resources        | default-resources    \n"
                                    + "compile                | maven-compiler-plugin  | 3.1     | compile          | default-compile      \n"
                                    + "process-test-resources | maven-resources-plugin | 2.6     | testResources    | default-testResources\n"
                                    + "test-compile           | maven-compiler-plugin  | 3.1     | testCompile      | default-testCompile  \n"
                                    + "test                   | maven-surefire-plugin  | 2.12.4  | test             | default-test         \n"
                                    + "package                | maven-jar-plugin       | 2.4     | jar              | default-jar          \n"
                                    + "integration-test       | maven-failsafe-plugin  | 2.19.1  | integration-test | default              \n"
                                    + "verify                 | maven-failsafe-plugin  | 2.19.1  | verify           | default              \n"
                                    + "install                | maven-install-plugin   | 2.4     | install          | default-install      \n"
                                    + "deploy                 | maven-deploy-plugin    | 2.7     | deploy           | default-deploy       \n");
        }

        @MavenTest
        @SystemProperty(value = "buildplan.tasks", content = "clean,deploy")
        @SystemProperty(value = "buildplan.showLifecycles", content = "true")
        void list_with_lifecycle(MavenExecutionResult result) {
            assertThat(result)
                    .isSuccessful()
                    .out()
                    .plain()
                    .containsSequence(
                            "[INFO] Build Plan for list-multimodule: ",
                            "--------------------------------------------------------------------------------",
                            "LIFECYCLE | PHASE   | PLUGIN               | VERSION | GOAL    | EXECUTION ID   ",
                            "--------------------------------------------------------------------------------",
                            "clean     | clean   | maven-clean-plugin   | 2.5     | clean   | default-clean  ",
                            "default   | install | maven-install-plugin | 2.4     | install | default-install",
                            "default   | deploy  | maven-deploy-plugin  | 2.7     | deploy  | default-deploy ")
                    .containsSequence(
                            "[INFO] Build Plan for list-multimodule-module-a: ",
                            "-------------------------------------------------------------------------------------------------------------",
                            "LIFECYCLE | PHASE                  | PLUGIN                 | VERSION | GOAL          | EXECUTION ID         ",
                            "-------------------------------------------------------------------------------------------------------------",
                            "clean     | clean                  | maven-clean-plugin     | 2.5     | clean         | default-clean        ",
                            "default   | process-resources      | maven-resources-plugin | 2.6     | resources     | default-resources    ",
                            "default   | compile                | maven-compiler-plugin  | 3.1     | compile       | default-compile      ",
                            "default   | process-test-resources | maven-resources-plugin | 2.6     | testResources | default-testResources",
                            "default   | test-compile           | maven-compiler-plugin  | 3.1     | testCompile   | default-testCompile  ",
                            "default   | test                   | maven-surefire-plugin  | 2.12.4  | test          | default-test         ",
                            "default   | package                | maven-jar-plugin       | 2.4     | jar           | default-jar          ",
                            "default   | install                | maven-install-plugin   | 2.4     | install       | default-install      ",
                            "default   | deploy                 | maven-deploy-plugin    | 2.7     | deploy        | default-deploy       ")
                    .containsSequence(
                            "[INFO] Build Plan for list-multimodule-module-b: ",
                            "----------------------------------------------------------------------------------------------------------------",
                            "LIFECYCLE | PHASE                  | PLUGIN                 | VERSION | GOAL             | EXECUTION ID         ",
                            "----------------------------------------------------------------------------------------------------------------",
                            "clean     | clean                  | maven-clean-plugin     | 2.5     | clean            | default-clean        ",
                            "default   | process-resources      | maven-resources-plugin | 2.6     | resources        | default-resources    ",
                            "default   | compile                | maven-compiler-plugin  | 3.1     | compile          | default-compile      ",
                            "default   | process-test-resources | maven-resources-plugin | 2.6     | testResources    | default-testResources",
                            "default   | test-compile           | maven-compiler-plugin  | 3.1     | testCompile      | default-testCompile  ",
                            "default   | test                   | maven-surefire-plugin  | 2.12.4  | test             | default-test         ",
                            "default   | package                | maven-jar-plugin       | 2.4     | jar              | default-jar          ",
                            "default   | integration-test       | maven-failsafe-plugin  | 2.19.1  | integration-test | default              ",
                            "default   | verify                 | maven-failsafe-plugin  | 2.19.1  | verify           | default              ",
                            "default   | install                | maven-install-plugin   | 2.4     | install          | default-install      ",
                            "default   | deploy                 | maven-deploy-plugin    | 2.7     | deploy           | default-deploy       ");
        }
    }
}

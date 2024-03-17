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

import com.soebes.itf.jupiter.extension.MavenGoal;
import com.soebes.itf.jupiter.extension.MavenJupiterExtension;
import com.soebes.itf.jupiter.extension.MavenProject;
import com.soebes.itf.jupiter.extension.MavenTest;
import com.soebes.itf.jupiter.extension.SystemProperty;
import com.soebes.itf.jupiter.maven.MavenExecutionResult;
import org.junit.jupiter.api.Nested;

import static com.soebes.itf.extension.assertj.MavenITAssertions.assertThat;

@MavenJupiterExtension
class ListPluginIT {

    @Nested
    @MavenProject
    @MavenGoal("${project.groupId}:${project.artifactId}:${project.version}:list-plugin")
    class SimpleProject {

        @MavenTest
        void list_plugin_with_default_cli(MavenExecutionResult result) {
            assertThat(result)
                    .isSuccessful()
                    .out()
                    .plain()
                    .containsSequence(
                            "maven-resources-plugin ----------------------------------------------",
                            "    + process-resources      | resources     | default-resources    ",
                            "    + process-test-resources | testResources | default-testResources",
                            "maven-compiler-plugin -----------------------------------------------",
                            "    + compile                | compile       | default-compile      ",
                            "    + test-compile           | testCompile   | default-testCompile  ",
                            "maven-surefire-plugin -----------------------------------------------",
                            "    + test                   | test          | default-test         ",
                            "maven-jar-plugin ----------------------------------------------------",
                            "    + package                | jar           | default-jar          ",
                            "maven-install-plugin ------------------------------------------------",
                            "    + install                | install       | default-install      ",
                            "maven-deploy-plugin -------------------------------------------------",
                            "    + deploy                 | deploy        | default-deploy       ");
        }

        @MavenTest
        @SystemProperty(value = "buildplan.skip", content = "true")
        void skip_execution(MavenExecutionResult result) {
            assertThat(result).isSuccessful().out().info().contains("Skipping build plan execution.");
        }
    }
}

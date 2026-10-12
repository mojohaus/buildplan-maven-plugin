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
class ListPhaseIT {

    @Nested
    @MavenProject
    @MavenGoal("${project.groupId}:${project.artifactId}:${project.version}:list-phase")
    class WithoutPluginDeclaration {

        @MavenTest
        void list_phase_with_default_cli(MavenExecutionResult result) {
            assertThat(result).isSuccessful().out().plain().containsSequence(defaultPlan());
        }

        @MavenTest
        @SystemProperty(value = "buildplan.tasks", content = "clean,versions:set,install")
        @SystemProperty(value = "buildplan.showAllPhases", content = "true")
        @SystemProperty(value = "buildplan.showLifecycles", content = "true")
        void list_phase_with_defined_tasks_and_configuration_with_cli(MavenExecutionResult result) {
            assertThat(result).isSuccessful().out().plain().containsSequence(tasksWithLifecycles());
        }

        @MavenTest
        @SystemProperty(value = "buildplan.skip", content = "true")
        void skip_list_phase(MavenExecutionResult result) {
            assertThat(result).isSuccessful().out().info().contains("Skipping build plan execution.");
        }

        @MavenTest
        @SystemProperty(value = "buildplan.tasks", content = "clean,deploy")
        @SystemProperty(value = "buildplan.showAllPhases", content = "true")
        void list_phase_show_all_phases(MavenExecutionResult result) {
            assertThat(result).isSuccessful().out().plain().containsSequence(allPhases());
        }

        @MavenTest
        @SystemProperty(value = "buildplan.tasks", content = "clean,deploy")
        @SystemProperty(value = "buildplan.showLifecycles", content = "true")
        void list_phase_show_lifecycle(MavenExecutionResult result) {
            assertThat(result).isSuccessful().out().plain().containsSequence(lifecycleOnly());
        }
    }

    @Nested
    class WithPluginDeclaration {

        @MavenTest
        @MavenGoal("validate")
        void list_phase_with_plugin_defined_in_pom(MavenExecutionResult result) {
            assertThat(result).isSuccessful().out().plain().containsSequence(defaultPlan());
        }

        @MavenTest
        @MavenGoal("validate")
        void skip_list_phase_with_plugin_defined_in_pom(MavenExecutionResult result) {
            assertThat(result).isSuccessful().out().info().contains("Skipping build plan execution.");
        }
    }

    private static boolean isMaven4() {
        return System.getProperty("maven.version", "").startsWith("4.");
    }

    private static final String RESOURCES = "    + maven-resources-plugin | resources     | default-resources    ";
    private static final String COMPILE = "    + maven-compiler-plugin  | compile       | default-compile      ";
    private static final String TEST_RESOURCES = "    + maven-resources-plugin | testResources | default-testResources";
    private static final String TEST_COMPILE = "    + maven-compiler-plugin  | testCompile   | default-testCompile  ";
    private static final String TEST = "    + maven-surefire-plugin  | test          | default-test         ";
    private static final String JAR = "    + maven-jar-plugin       | jar           | default-jar          ";
    private static final String INSTALL = "    + maven-install-plugin   | install       | default-install      ";
    private static final String DEPLOY = "    + maven-deploy-plugin    | deploy        | default-deploy       ";
    private static final String CLEAN = "    + maven-clean-plugin     | clean         | default-clean        ";
    private static final String VERSIONS_SET = "    + versions-maven-plugin  | set           | default-cli          ";

    /** Maven 4 binds resources to the after: phases and has a different phase set (before:/after: phases). */
    private static final String[] MAVEN4_CLEAN_PHASES = {"before:clean", "clean", CLEAN, "after:clean"};

    private static final String[] MAVEN4_DEFAULT_PHASES = {
        "before:all",
        "before:initialize",
        "before:validate",
        "validate",
        "after:validate",
        "initialize",
        "after:initialize",
        "before:build",
        "before:sources",
        "sources",
        "after:sources",
        "before:resources",
        "resources",
        "after:resources",
        RESOURCES,
        "before:compile",
        "compile",
        COMPILE,
        "after:compile",
        "before:ready",
        "ready",
        "after:ready",
        "before:test-sources",
        "test-sources",
        "after:test-sources",
        "before:test-resources",
        "test-resources",
        "after:test-resources",
        TEST_RESOURCES,
        "before:test-compile",
        "test-compile",
        TEST_COMPILE,
        "after:test-compile",
        "before:test",
        "test",
        TEST,
        "after:test",
        "before:unit-test",
        "unit-test",
        "after:unit-test",
        "before:package",
        "package",
        JAR,
        "after:package",
        "build",
        "after:build",
        "before:verify",
        "before:integration-test",
        "integration-test",
        "after:integration-test",
        "verify",
        "after:verify",
        "before:install",
        "install",
        INSTALL,
        "after:install",
        "before:deploy",
        "deploy",
        DEPLOY,
        "after:deploy",
        "all",
        "after:all"
    };

    private static String header(String phase) {
        return phase + " " + new String(new char[68 - phase.length()]).replace('\0', '-');
    }

    private static boolean isPluginLine(String line) {
        return line.startsWith("    + ");
    }

    /** Phases (and the mojos bound to them) in output order, without lifecycle names. */
    private static java.util.List<String> flat(String[]... phaseGroups) {
        java.util.List<String> lines = new java.util.ArrayList<>();
        for (String[] group : phaseGroups) {
            for (String entry : group) {
                lines.add(isPluginLine(entry) ? entry : header(entry));
            }
        }
        return lines;
    }

    /** Maven 4 prints a "[lifecycle]" line before every phase and a blank line between phases. */
    private static String[] maven4Blocks(java.util.List<String[]> phaseLists, java.util.List<String> lifecycles) {
        java.util.List<String> lines = new java.util.ArrayList<>();
        for (int i = 0; i < phaseLists.size(); i++) {
            for (String entry : phaseLists.get(i)) {
                if (!isPluginLine(entry)) {
                    if (!lines.isEmpty()) {
                        lines.add("");
                    }
                    lines.add("[" + lifecycles.get(i) + "]");
                    lines.add(header(entry));
                } else {
                    lines.add(entry);
                }
            }
        }
        return lines.toArray(new String[0]);
    }

    private static String[] defaultPlan() {
        if (!isMaven4()) {
            return maven3DefaultPlan();
        }
        return flat(new String[] {
                    "after:resources",
                    RESOURCES,
                    "compile",
                    COMPILE,
                    "after:test-resources",
                    TEST_RESOURCES,
                    "test-compile",
                    TEST_COMPILE,
                    "test",
                    TEST,
                    "package",
                    JAR,
                    "install",
                    INSTALL,
                    "deploy",
                    DEPLOY
                })
                .toArray(new String[0]);
    }

    private static String[] tasksWithLifecycles() {
        if (!isMaven4()) {
            return maven3TasksWithLifecycles();
        }
        String[] noPhase = {"<no phase>", VERSIONS_SET};
        return maven4Blocks(
                java.util.Arrays.asList(MAVEN4_CLEAN_PHASES, noPhase, withoutDeploy()),
                java.util.Arrays.asList("clean", "", "default"));
    }

    /** The deploy goal is not requested by the test, so its mojo is absent from the plan. */
    private static String[] withoutDeploy() {
        return java.util.Arrays.stream(MAVEN4_DEFAULT_PHASES)
                .filter(entry -> !entry.equals(DEPLOY))
                .toArray(String[]::new);
    }

    private static String[] allPhases() {
        if (!isMaven4()) {
            return maven3AllPhases();
        }
        return flat(MAVEN4_CLEAN_PHASES, MAVEN4_DEFAULT_PHASES).toArray(new String[0]);
    }

    private static String[] lifecycleOnly() {
        if (!isMaven4()) {
            return maven3LifecycleOnly();
        }
        String[] clean = {"clean", CLEAN};
        String[] def = {
            "after:resources",
            RESOURCES,
            "compile",
            COMPILE,
            "after:test-resources",
            TEST_RESOURCES,
            "test-compile",
            TEST_COMPILE,
            "test",
            TEST,
            "package",
            JAR,
            "install",
            INSTALL,
            "deploy",
            DEPLOY
        };
        // Maven 4 reports every phase with its own "[default]" header here
        return maven4Blocks(java.util.Arrays.asList(clean, def), java.util.Arrays.asList("clean", "default"));
    }

    private static String[] maven3LifecycleOnly() {
        return new String[] {
            "[clean]",
            "clean ---------------------------------------------------------------",
            "    + maven-clean-plugin     | clean         | default-clean        ",
            "",
            "[default]",
            "process-resources ---------------------------------------------------",
            "    + maven-resources-plugin | resources     | default-resources    ",
            "compile -------------------------------------------------------------",
            "    + maven-compiler-plugin  | compile       | default-compile      ",
            "process-test-resources ----------------------------------------------",
            "    + maven-resources-plugin | testResources | default-testResources",
            "test-compile --------------------------------------------------------",
            "    + maven-compiler-plugin  | testCompile   | default-testCompile  ",
            "test ----------------------------------------------------------------",
            "    + maven-surefire-plugin  | test          | default-test         ",
            "package -------------------------------------------------------------",
            "    + maven-jar-plugin       | jar           | default-jar          ",
            "install -------------------------------------------------------------",
            "    + maven-install-plugin   | install       | default-install      ",
            "deploy --------------------------------------------------------------",
            "    + maven-deploy-plugin    | deploy        | default-deploy       "
        };
    }

    private static String[] maven3AllPhases() {
        return new String[] {
            "pre-clean -----------------------------------------------------------",
            "clean ---------------------------------------------------------------",
            "    + maven-clean-plugin     | clean         | default-clean        ",
            "post-clean ----------------------------------------------------------",
            "validate ------------------------------------------------------------",
            "initialize ----------------------------------------------------------",
            "generate-sources ----------------------------------------------------",
            "process-sources -----------------------------------------------------",
            "generate-resources --------------------------------------------------",
            "process-resources ---------------------------------------------------",
            "    + maven-resources-plugin | resources     | default-resources    ",
            "compile -------------------------------------------------------------",
            "    + maven-compiler-plugin  | compile       | default-compile      ",
            "process-classes -----------------------------------------------------",
            "generate-test-sources -----------------------------------------------",
            "process-test-sources ------------------------------------------------",
            "generate-test-resources ---------------------------------------------",
            "process-test-resources ----------------------------------------------",
            "    + maven-resources-plugin | testResources | default-testResources",
            "test-compile --------------------------------------------------------",
            "    + maven-compiler-plugin  | testCompile   | default-testCompile  ",
            "process-test-classes ------------------------------------------------",
            "test ----------------------------------------------------------------",
            "    + maven-surefire-plugin  | test          | default-test         ",
            "prepare-package -----------------------------------------------------",
            "package -------------------------------------------------------------",
            "    + maven-jar-plugin       | jar           | default-jar          ",
            "pre-integration-test ------------------------------------------------",
            "integration-test ----------------------------------------------------",
            "post-integration-test -----------------------------------------------",
            "verify --------------------------------------------------------------",
            "install -------------------------------------------------------------",
            "    + maven-install-plugin   | install       | default-install      ",
            "deploy --------------------------------------------------------------",
            "    + maven-deploy-plugin    | deploy        | default-deploy       "
        };
    }

    private static String[] maven3TasksWithLifecycles() {
        return new String[] {
            "[clean]",
            "pre-clean -----------------------------------------------------------",
            "clean ---------------------------------------------------------------",
            "    + maven-clean-plugin     | clean         | default-clean        ",
            "post-clean ----------------------------------------------------------",
            "",
            "[]",
            "<no phase> ----------------------------------------------------------",
            "    + versions-maven-plugin  | set           | default-cli          ",
            "",
            "[default]",
            "validate ------------------------------------------------------------",
            "initialize ----------------------------------------------------------",
            "generate-sources ----------------------------------------------------",
            "process-sources -----------------------------------------------------",
            "generate-resources --------------------------------------------------",
            "process-resources ---------------------------------------------------",
            "    + maven-resources-plugin | resources     | default-resources    ",
            "compile -------------------------------------------------------------",
            "    + maven-compiler-plugin  | compile       | default-compile      ",
            "process-classes -----------------------------------------------------",
            "generate-test-sources -----------------------------------------------",
            "process-test-sources ------------------------------------------------",
            "generate-test-resources ---------------------------------------------",
            "process-test-resources ----------------------------------------------",
            "    + maven-resources-plugin | testResources | default-testResources",
            "test-compile --------------------------------------------------------",
            "    + maven-compiler-plugin  | testCompile   | default-testCompile  ",
            "process-test-classes ------------------------------------------------",
            "test ----------------------------------------------------------------",
            "    + maven-surefire-plugin  | test          | default-test         ",
            "prepare-package -----------------------------------------------------",
            "package -------------------------------------------------------------",
            "    + maven-jar-plugin       | jar           | default-jar          ",
            "pre-integration-test ------------------------------------------------",
            "integration-test ----------------------------------------------------",
            "post-integration-test -----------------------------------------------",
            "verify --------------------------------------------------------------",
            "install -------------------------------------------------------------",
            "    + maven-install-plugin   | install       | default-install      ",
            "deploy --------------------------------------------------------------"
        };
    }

    private static String[] maven3DefaultPlan() {
        return new String[] {
            "process-resources ---------------------------------------------------",
            "    + maven-resources-plugin | resources     | default-resources    ",
            "compile -------------------------------------------------------------",
            "    + maven-compiler-plugin  | compile       | default-compile      ",
            "process-test-resources ----------------------------------------------",
            "    + maven-resources-plugin | testResources | default-testResources",
            "test-compile --------------------------------------------------------",
            "    + maven-compiler-plugin  | testCompile   | default-testCompile  ",
            "test ----------------------------------------------------------------",
            "    + maven-surefire-plugin  | test          | default-test         ",
            "package -------------------------------------------------------------",
            "    + maven-jar-plugin       | jar           | default-jar          ",
            "install -------------------------------------------------------------",
            "    + maven-install-plugin   | install       | default-install      ",
            "deploy --------------------------------------------------------------",
            "    + maven-deploy-plugin    | deploy        | default-deploy       "
        };
    }
}

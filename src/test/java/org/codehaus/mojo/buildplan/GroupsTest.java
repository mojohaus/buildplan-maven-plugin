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

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.codehaus.mojo.buildplan.model.builder.MojoExecutionBuilder.aMojoExecution;

import java.util.Map;
import org.apache.maven.plugin.MojoExecution;
import org.codehaus.mojo.buildplan.Groups.ByPhase;
import org.codehaus.mojo.buildplan.Groups.ByPlugin;
import org.codehaus.mojo.buildplan.util.Multimap;
import org.junit.Test;

public class GroupsTest {

    @Test
    public void should_order_mojo_execution_by_phase_name() {

        MojoExecution pluginA = aMojoExecution().withArtifactId("plugin-a")
                                                .withExecutionId("a")
                                                .withGoal("goal-a")
                                                .withLifecyclePhase("phase")
                                                .build();
        MojoExecution pluginB = aMojoExecution().withArtifactId("plugin-b")
                                                .withExecutionId("b")
                                                .withGoal("goal-b")
                                                .withLifecyclePhase("phase")
                                                .build();
        MojoExecution pluginC = aMojoExecution().withArtifactId("plugin-c")
                                                .withExecutionId("c")
                                                .withGoal("goal-c")
                                                .withLifecyclePhase("a-phase")
                                                .build();
        MojoExecution pluginD = aMojoExecution().withArtifactId("plugin-d")
                                                .withExecutionId("d")
                                                .withGoal("goal-d")
                                                .withLifecyclePhase("d-phase")
                                                .build();

        Multimap<String, MojoExecution> result = Groups.ByPhase.of(asList(pluginD, pluginA, pluginC, pluginB));

        assertThat(result.keySet()).containsOnly("phase", "a-phase", "d-phase");
        assertThat(result.get("phase")).containsOnly(pluginA, pluginB);
        assertThat(result.get("a-phase")).containsOnly(pluginC);
        assertThat(result.get("d-phase")).containsOnly(pluginD);
    }

    @Test
    public void should_order_mojo_execution_by_artifact_id() {

        MojoExecution pluginB = aMojoExecution().withArtifactId("plugin-b")
                                                .withExecutionId("b")
                                                .withGoal("goal-b")
                                                .withLifecyclePhase("phase-b")
                                                .build();
        MojoExecution pluginA = aMojoExecution().withArtifactId("plugin-a")
                                                .withExecutionId("a-a")
                                                .withGoal("goal-a-a")
                                                .withLifecyclePhase("phase-a")
                                                .build();
        MojoExecution pluginC = aMojoExecution().withArtifactId("plugin-c")
                                                .withExecutionId("c")
                                                .withGoal("goal-c")
                                                .withLifecyclePhase("phase-c")
                                                .build();
        MojoExecution pluginAA = aMojoExecution().withArtifactId("plugin-a")
                                                .withExecutionId("a-b")
                                                .withGoal("goal-a-b")
                                                .withLifecyclePhase("phase-a-b")
                                                .build();

        Multimap<String, MojoExecution> result = Groups.ByPlugin.of(asList(pluginA, pluginB, pluginC, pluginAA));

        assertThat(result.keySet()).containsOnly("plugin-a", "plugin-b", "plugin-c");
        assertThat(result.get("plugin-a")).containsOnly(pluginA, pluginAA);
        assertThat(result.get("plugin-b")).containsOnly(pluginB);
        assertThat(result.get("plugin-c")).containsOnly(pluginC);
    }

    @Test
    public void should_filter_mojo_execution_by_phase_name() {

        MojoExecution pluginA = aMojoExecution().withArtifactId("plugin-a")
                                                .withExecutionId("a")
                                                .withGoal("goal-a")
                                                .withLifecyclePhase("phase-a")
                                                .build();
        MojoExecution pluginB = aMojoExecution().withArtifactId("plugin-b")
                                                .withExecutionId("b")
                                                .withGoal("goal-b")
                                                .withLifecyclePhase("phase-b")
                                                .build();

        Multimap<String, MojoExecution> result = Groups.ByPhase.of(asList(pluginA, pluginB), "phase-a");

        assertThat(result.keySet()).containsOnly("phase-a");
    }

    @Test
    public void should_filter_mojo_execution_by_artifactId() {

        MojoExecution pluginA = aMojoExecution().withArtifactId("plugin-a")
                                                .withExecutionId("a")
                                                .withGoal("goal-a")
                                                .withLifecyclePhase("phase-a")
                                                .build();
        MojoExecution pluginB = aMojoExecution().withArtifactId("plugin-b")
                                                .withExecutionId("b")
                                                .withGoal("goal-b")
                                                .withLifecyclePhase("phase-b")
                                                .build();

        Multimap<String, MojoExecution> result = Groups.ByPlugin.of(asList(pluginA, pluginB), "plugin-a");

        assertThat(result.keySet()).containsOnly("plugin-a");
    }

    @Test
    public void should_not_fail_when_grouping_mojo_execution_by_phase_with_null_phase() {

        MojoExecution pluginA = aMojoExecution().withArtifactId("plugin-a")
                                                .withExecutionId("a")
                                                .withGoal("goal-a")
                                                .withoutMojoDescriptor()
                                                .build();

        Multimap<String, MojoExecution> result = Groups.ByPhase.of(asList(pluginA));

        assertThat(result.keySet()).containsOnly("<no phase>");
    }

    @Test
    public void should_keep_phase_order_when_grouping_execution_by_phase() {
        MojoExecution pluginA = aMojoExecution().withArtifactId("plugin-a")
                .withExecutionId("a")
                .withGoal("goal-a")
                .withLifecyclePhase("phase")
                .build();
        MojoExecution pluginB = aMojoExecution().withArtifactId("plugin-b")
                .withExecutionId("b")
                .withGoal("goal-b")
                .withLifecyclePhase("phase")
                .build();
        MojoExecution pluginC = aMojoExecution().withArtifactId("plugin-c")
                .withExecutionId("c")
                .withGoal("goal-c")
                .withLifecyclePhase("a-phase")
                .build();
        MojoExecution pluginD = aMojoExecution().withArtifactId("plugin-d")
                .withExecutionId("d")
                .withGoal("goal-d")
                .withLifecyclePhase("d-phase")
                .build();

        Multimap<String,MojoExecution> result = Groups.ByPhase.of(asList(pluginA, pluginB, pluginC, pluginD));

        assertThat(result.asMap().entrySet())
                .extracting(Map.Entry::getKey)
                .containsExactly("phase", "a-phase", "d-phase");

        assertThat(result.asMap().entrySet())
                .flatExtracting(Map.Entry::getValue)
                .extracting("goal")
                .containsExactly("goal-a", "goal-b", "goal-c", "goal-d");
    }

    @Test
    public void should_keep_phase_order_when_grouping_execution_by_artifactId() {

        MojoExecution pluginA = aMojoExecution().withArtifactId("plugin-a")
                .withExecutionId("a-a")
                .withGoal("goal-a-a")
                .withLifecyclePhase("phase-a")
                .build();
        MojoExecution pluginAA = aMojoExecution().withArtifactId("plugin-a")
                .withExecutionId("a-b")
                .withGoal("goal-a-b")
                .withLifecyclePhase("phase-a-b")
                .build();
        MojoExecution pluginB = aMojoExecution().withArtifactId("plugin-b")
                .withExecutionId("b")
                .withGoal("goal-b")
                .withLifecyclePhase("phase-b")
                .build();
        MojoExecution pluginC = aMojoExecution().withArtifactId("plugin-c")
                .withExecutionId("c")
                .withGoal("goal-c")
                .withLifecyclePhase("phase-c")
                .build();

        Multimap<String, MojoExecution> result = Groups.ByPlugin.of(asList(pluginA, pluginB, pluginC, pluginAA));

        assertThat(result.asMap().entrySet())
                .extracting(Map.Entry::getKey)
                .containsExactly("plugin-a", "plugin-b", "plugin-c");

        assertThat(result.asMap().entrySet())
                .flatExtracting(Map.Entry::getValue)
                .extracting("lifecyclePhase")
                .containsExactly("phase-a", "phase-a-b", "phase-b", "phase-c");
    }
}

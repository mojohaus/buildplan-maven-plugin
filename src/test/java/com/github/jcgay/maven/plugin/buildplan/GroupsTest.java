package com.github.jcgay.maven.plugin.buildplan;

import com.google.common.collect.Multimap;
import org.apache.maven.plugin.MojoExecution;
import org.junit.Test;

import java.util.Arrays;

import static com.github.jcgay.maven.plugin.buildplan.model.builder.MojoExecutionBuilder.aMojoExecution;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

public class GroupsTest {

    @Test
    public void should_order_mojo_execution_by_phase_name() {

        MojoExecution pluginA = aMojoExecution().withArtifactId("plugin-a")
                                                .withExecutionId("a")
                                                .withGoal("goal-a")
                                                .withPhase("phase")
                                                .build();
        MojoExecution pluginB = aMojoExecution().withArtifactId("plugin-b")
                                                .withExecutionId("b")
                                                .withGoal("goal-b")
                                                .withPhase("phase")
                                                .build();
        MojoExecution pluginC = aMojoExecution().withArtifactId("plugin-c")
                                                .withExecutionId("c")
                                                .withGoal("goal-c")
                                                .withPhase("a-phase")
                                                .build();
        MojoExecution pluginD = aMojoExecution().withArtifactId("plugin-d")
                                                .withExecutionId("d")
                                                .withGoal("goal-d")
                                                .withPhase("d-phase")
                                                .build();

        Multimap<String,MojoExecution> result = Groups.ByPhase.of(asList(pluginD, pluginA, pluginC, pluginB));

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
                                                .withPhase("phase-b")
                                                .build();
        MojoExecution pluginA = aMojoExecution().withArtifactId("plugin-a")
                                                .withExecutionId("a-a")
                                                .withGoal("goal-a-a")
                                                .withPhase("phase-a")
                                                .build();
        MojoExecution pluginC = aMojoExecution().withArtifactId("plugin-c")
                                                .withExecutionId("c")
                                                .withGoal("goal-c")
                                                .withPhase("phase-c")
                                                .build();
        MojoExecution pluginAA = aMojoExecution().withArtifactId("plugin-a")
                                                .withExecutionId("a-b")
                                                .withGoal("goal-a-b")
                                                .withPhase("phase-a-b")
                                                .build();

        Multimap<String, MojoExecution> result = Groups.ByPlugin.of(Arrays.asList(pluginA, pluginB, pluginC, pluginAA));

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
                                                .withPhase("phase-a")
                                                .build();
        MojoExecution pluginB = aMojoExecution().withArtifactId("plugin-b")
                                                .withExecutionId("b")
                                                .withGoal("goal-b")
                                                .withPhase("phase-b")
                                                .build();

        Multimap<String, MojoExecution> result = Groups.ByPhase.of(Arrays.asList(pluginA, pluginB), "phase-a");

        assertThat(result.keySet()).containsOnly("phase-a");
    }

    @Test
    public void should_filter_mojo_execution_by_artifactId() {

        MojoExecution pluginA = aMojoExecution().withArtifactId("plugin-a")
                                                .withExecutionId("a")
                                                .withGoal("goal-a")
                                                .withPhase("phase-a")
                                                .build();
        MojoExecution pluginB = aMojoExecution().withArtifactId("plugin-b")
                                                .withExecutionId("b")
                                                .withGoal("goal-b")
                                                .withPhase("phase-b")
                                                .build();

        Multimap<String, MojoExecution> result = Groups.ByPlugin.of(Arrays.asList(pluginA, pluginB), "plugin-a");

        assertThat(result.keySet()).containsOnly("plugin-a");
    }

    @Test
    public void should_not_fail_when_grouping_mojo_execution_by_phase_with_null_phase() {

        MojoExecution pluginA = aMojoExecution().withArtifactId("plugin-a")
                                                .withExecutionId("a")
                                                .withGoal("goal-a")
                                                .withoutMojoDescriptor()
                                                .build();

        Multimap<String, MojoExecution> result = Groups.ByPhase.of(Arrays.asList(pluginA));

        assertThat(result.keySet()).containsOnly("default-phase");
    }
}

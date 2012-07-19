package com.github.jcgay.maven.plugin.lifecycle.listphase;

import com.google.common.collect.Multimap;
import org.apache.maven.plugin.MojoExecution;
import org.junit.Test;

import static com.github.jcgay.maven.plugin.lifecycle.model.builder.MojoExecutionBuilder.aMojoExecution;
import static java.util.Arrays.asList;
import static org.fest.assertions.api.Assertions.assertThat;

public class MavenPhaseGroupsTest {

    @Test
    public void should_ordered_mojo_execution_by_phase_name() {

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

        Multimap<String,MojoExecution> result = MavenPhaseGroups.of(asList(pluginD, pluginA, pluginC, pluginB));

        assertThat(result.keySet()).containsOnly("phase", "a-phase", "d-phase");
        assertThat(result.get("phase")).containsOnly(pluginA, pluginB);
        assertThat(result.get("a-phase")).containsOnly(pluginC);
        assertThat(result.get("d-phase")).containsOnly(pluginD);
    }

}

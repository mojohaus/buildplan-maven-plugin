package com.github.jcgay.maven.plugin.lifecycle.display;

import org.apache.maven.plugin.MojoExecution;
import org.junit.Test;

import static com.github.jcgay.maven.plugin.lifecycle.model.builder.MojoExecutionBuilder.aMojoExecution;
import static org.assertj.core.api.Assertions.assertThat;

public class MojoExecutionDisplayTest {

    @Test
    public void should_get_non_null_values_from_mojo_execution() {

        MojoExecution execution = aMojoExecution().withArtifactId("artifactId")
                                                  .withExecutionId("executionId")
                                                  .withGoal("goal")
                                                  .withPhase("phase")
                                                  .build();

        MojoExecutionDisplay result = new MojoExecutionDisplay(execution);

        assertThat(result.getArtifactId()).isEqualTo("artifactId");
        assertThat(result.getExecutionId()).isEqualTo("executionId");
        assertThat(result.getGoal()).isEqualTo("goal");
        assertThat(result.getPhase()).isEqualTo("phase");
    }

    @Test
    public void should_get_empty_string_from_mojo_execution_when_a_value_is_null() {

        MojoExecution execution = aMojoExecution().withArtifactId(null)
                                                  .withExecutionId(null)
                                                  .withGoal(null)
                                                  .withPhase(null)
                                                  .build();

        MojoExecutionDisplay result = new MojoExecutionDisplay(execution);

        assertThat(result.getArtifactId()).isEmpty();
        assertThat(result.getExecutionId()).isEmpty();
        assertThat(result.getGoal()).isEmpty();
        assertThat(result.getPhase()).isEmpty();
    }
}

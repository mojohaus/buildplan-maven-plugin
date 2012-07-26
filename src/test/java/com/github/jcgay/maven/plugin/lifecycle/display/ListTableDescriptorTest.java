package com.github.jcgay.maven.plugin.lifecycle.display;

import org.apache.maven.plugin.MojoExecution;
import org.junit.Test;

import static com.github.jcgay.maven.plugin.lifecycle.model.builder.MojoExecutionBuilder.aMojoExecution;
import static java.util.Arrays.asList;
import static org.fest.assertions.api.Assertions.assertThat;

public class ListTableDescriptorTest {

    @Test
    public void should_add_all_size_and_the_separator_size_to_get_descriptor_width() {

        ListTableDescriptor descriptor = new ListTableDescriptor().setExecutionIdSize(1)
                                                                  .setGoalSize(2)
                                                                  .setPluginSize(3)
                                                                  .setPhaseSize(4);

        assertThat(descriptor.width()).isEqualTo(10 + ListTableDescriptor.SEPARATOR_SIZE);
    }

    @Test
    public void should_calculate_column_size_from_longest_element_in_each_column() {

        MojoExecution executionA = aMojoExecution().withArtifactId("plugin-a")
                                                   .withPhase("short-phase")
                                                   .withExecutionId("short-execution-id")
                                                   .withGoal("short-goal")
                                                   .build();
        MojoExecution executionB = aMojoExecution().withArtifactId("plugin-b-longer-than-a")
                                                   .withPhase("a-very-very-very-long-phase")
                                                   .withExecutionId("a-very-very-very-long-execution-id")
                                                   .withGoal("a-very-very-very-long-goal")
                                                   .build();

        ListTableDescriptor descriptor = ListTableDescriptor.of(asList(executionA, executionB));

        assertThat(descriptor.getPluginSize()).isEqualTo(executionB.getArtifactId().length());
        assertThat(descriptor.getExecutionIdSize()).isEqualTo(executionB.getExecutionId().length());
        assertThat(descriptor.getGoalSize()).isEqualTo(executionB.getGoal().length());
        assertThat(descriptor.getPhaseSize()).isEqualTo(executionB.getMojoDescriptor().getPhase().length());
    }

    @Test
    public void should_not_failed_when_finding_max_size_with_a_mojo_execution_with_a_null_mojo_descriptor() {

        MojoExecution executionA = aMojoExecution().withArtifactId("plugin-a")
                                                   .withPhase("phase-a")
                                                   .withExecutionId("execution-id-a")
                                                   .withGoal("goal-a")
                                                   .build();

        MojoExecution executionB = aMojoExecution().withArtifactId("plugin-b-longer-than-a")
                                                   .withExecutionId("execution-id-b-longer-than-b")
                                                   .withGoal("goal-b-longer-than-a")
                                                   .withoutMojoDescriptor()
                                                   .build();

        MojoExecution executionC = aMojoExecution().withArtifactId("plugin-c")
                                                   .withPhase(null)
                                                   .withExecutionId("execution-id-c")
                                                   .withGoal("goal-c")
                                                   .build();

        ListTableDescriptor result = ListTableDescriptor.of(asList(executionA, executionB, executionC));

        assertThat(result.getExecutionIdSize()).isEqualTo(executionB.getExecutionId().length());
        assertThat(result.getPluginSize()).isEqualTo(executionB.getArtifactId().length());
        assertThat(result.getGoalSize()).isEqualTo(executionB.getGoal().length());
        assertThat(result.getPhaseSize()).isEqualTo(executionA.getMojoDescriptor().getPhase().length());
    }

    @Test
    public void should_build_a_row_format_for_a_list_table_descriptor() {

        ListTableDescriptor descriptor = new ListTableDescriptor().setPluginSize(1)
                                                                  .setPhaseSize(2)
                                                                  .setExecutionIdSize(3)
                                                                  .setGoalSize(4);

        String result = descriptor.rowFormat();

        assertThat(result).isEqualTo("%-1s | %-2s | %-3s | %-4s");
    }
}

package com.github.jcgay.maven.plugin.lifecycle.display;

import com.github.jcgay.maven.plugin.lifecycle.display.model.TableDescriptor;
import org.apache.maven.plugin.MojoExecution;
import org.junit.Test;

import java.util.Arrays;

import static com.github.jcgay.maven.plugin.lifecycle.model.builder.MojoExecutionBuilder.aMojoExecution;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.fest.assertions.api.Assertions.fail;

public class TableDisplayConfiguratorTest {

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

        TableDescriptor descriptor = TableDisplayConfigurator.findMaxSize(Arrays.asList(executionA, executionB));

        assertThat(descriptor.getPluginSize()).isGreaterThan(executionB.getArtifactId().length());
        assertThat(descriptor.getExecutionIdSize()).isGreaterThan(executionB.getExecutionId().length());
        assertThat(descriptor.getGoalSize()).isGreaterThan(executionB.getMojoDescriptor().getGoal().length());
        assertThat(descriptor.getPhaseSize()).isGreaterThan(executionB.getMojoDescriptor().getPhase().length());
    }

    @Test
    public void should_build_a_row_format_for_a_list_table_descriptor() {

        TableDescriptor descriptor = new TableDescriptor().setPluginSize(1)
                                                          .setPhaseSize(2)
                                                          .setExecutionIdSize(3)
                                                          .setGoalSize(4);

        String result = TableDisplayConfigurator.buildRowFormatForList(descriptor);

        assertThat(result).contains("%-1s").contains("%-2s").contains("%-3s").contains("%-4s");
    }

    @Test
    public void should_build_a_row_format_for_a_list_phase_table_descriptor() {

        TableDescriptor descriptor = new TableDescriptor().setPluginSize(1)
                                                          .setExecutionIdSize(2)
                                                          .setGoalSize(3);

        String result = TableDisplayConfigurator.buildRowFormatForListPhase(descriptor);

        assertThat(result).isEqualTo("    + %-1s|%-2s|%-3s");
    }
}

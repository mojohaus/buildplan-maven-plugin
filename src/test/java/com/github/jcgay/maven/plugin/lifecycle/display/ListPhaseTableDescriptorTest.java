package com.github.jcgay.maven.plugin.lifecycle.display;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ListPhaseTableDescriptorTest {


    @Test
    public void should_build_a_row_format_for_a_list_phase_table_descriptor() {

        ListPhaseTableDescriptor descriptor = new ListPhaseTableDescriptor().setPluginSize(1)
                                                                            .setExecutionIdSize(2)
                                                                            .setGoalSize(3);

        String result = descriptor.rowFormat();

        assertThat(result).isEqualTo("    + %-1s | %-2s | %-3s");
    }
}

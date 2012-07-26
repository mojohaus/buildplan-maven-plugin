package com.github.jcgay.maven.plugin.lifecycle.display;

import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

public class ListPluginTableDescriptorTest {

    @Test
    public void should_build_a_row_format_for_a_list_plugin_table_descriptor() {

        ListPluginTableDescriptor descriptor = new ListPluginTableDescriptor().setPhaseSize(1)
                                                                              .setExecutionIdSize(2)
                                                                              .setGoalSize(3);

        String result = descriptor.rowFormat();

        assertThat(result).isEqualTo("    + %-1s | %-2s | %-3s");
    }
}

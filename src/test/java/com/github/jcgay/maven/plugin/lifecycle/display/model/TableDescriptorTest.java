package com.github.jcgay.maven.plugin.lifecycle.display.model;

import org.fest.assertions.api.Assertions;
import org.junit.Test;

public class TableDescriptorTest {

    @Test
    public void shoud_add_all_size_and_1_by_separator_to_get_descriptor_width() {

        TableDescriptor descriptor = new TableDescriptor().setExecutionIdSize(1)
                                                          .setGoalSize(2)
                                                          .setPluginSize(3)
                                                          .setPhaseSize(4);

        Assertions.assertThat(descriptor.width()).isEqualTo(10 + TableDescriptor.SEPARATOR_SIZE);
    }

}

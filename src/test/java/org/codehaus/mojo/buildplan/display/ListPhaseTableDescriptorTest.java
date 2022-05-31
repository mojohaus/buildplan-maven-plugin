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
package org.codehaus.mojo.buildplan.display;

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

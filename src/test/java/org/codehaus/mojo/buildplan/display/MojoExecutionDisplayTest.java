/**
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

import org.apache.maven.plugin.MojoExecution;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.codehaus.mojo.buildplan.model.builder.MojoExecutionBuilder.aMojoExecution;

public class MojoExecutionDisplayTest {

    @Test
    public void should_get_non_null_values_from_mojo_execution() {

        MojoExecution execution = aMojoExecution().withArtifactId("artifactId")
                                                  .withExecutionId("executionId")
                                                  .withGoal("goal")
                                                  .withLifecyclePhase("phase")
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
                                                  .withLifecyclePhase(null)
                                                  .build();

        MojoExecutionDisplay result = new MojoExecutionDisplay(execution);

        assertThat(result.getArtifactId()).isEmpty();
        assertThat(result.getExecutionId()).isEmpty();
        assertThat(result.getGoal()).isEmpty();
        assertThat(result.getPhase()).isEmpty();
    }

    @Test
    public void should_get_mojo_lifecycle_phase_when_descriptor_phase_is_null() {

        MojoExecution execution = aMojoExecution().withArtifactId("plugin-a")
                .withLifecyclePhase("phase-a")
                .withDescriptorPhase("phase-b")
                .withExecutionId("execution-id-a")
                .withGoal("goal-a")
                .build();

        MojoExecutionDisplay result = new MojoExecutionDisplay(execution);

        assertThat(result.getPhase()).isEqualTo("phase-a");
    }
}

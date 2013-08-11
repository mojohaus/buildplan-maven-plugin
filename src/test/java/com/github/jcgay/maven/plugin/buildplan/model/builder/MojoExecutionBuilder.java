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
package com.github.jcgay.maven.plugin.buildplan.model.builder;

import org.apache.maven.model.Plugin;
import org.apache.maven.plugin.MojoExecution;
import org.apache.maven.plugin.descriptor.MojoDescriptor;
import org.apache.maven.plugin.descriptor.PluginDescriptor;

public class MojoExecutionBuilder {

    private String artifactId;
    private String goal;
    private String phase;
    private String executionId;
    private boolean useMojoDescriptorConstructor = true;

    public static MojoExecutionBuilder aMojoExecution() {
        return new MojoExecutionBuilder();
    }

    public MojoExecutionBuilder withoutMojoDescriptor() {
        useMojoDescriptorConstructor = false;
        return this;
    }

    public MojoExecutionBuilder withExecutionId(String executionId) {
        this.executionId = executionId;
        return this;
    }

    public MojoExecutionBuilder withArtifactId(String artifactId) {
        this.artifactId = artifactId;
        return this;
    }

    public MojoExecutionBuilder withGoal(String goal) {
        this.goal = goal;
        return this;
    }

    public MojoExecutionBuilder withPhase(String phase) {
        this.phase = phase;
        return this;
    }

    public MojoExecution build() {

        if (useMojoDescriptorConstructor) {

            MojoDescriptor descriptor = new MojoDescriptor();
            descriptor.setGoal(goal);
            descriptor.setPhase(phase);
            PluginDescriptor pluginDescriptor = new PluginDescriptor();
            pluginDescriptor.setArtifactId(artifactId);
            descriptor.setPluginDescriptor(pluginDescriptor);

            return new MojoExecution(descriptor, executionId);
        } else {

            Plugin plugin = new Plugin();
            plugin.setArtifactId(artifactId);
            return new MojoExecution(plugin, goal, executionId);
        }
    }
}

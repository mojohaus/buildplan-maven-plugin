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

import org.apache.maven.lifecycle.DefaultLifecycles;
import org.apache.maven.lifecycle.Lifecycle;
import org.apache.maven.plugin.MojoExecution;
import org.apache.maven.plugin.descriptor.MojoDescriptor;

import static org.codehaus.plexus.util.StringUtils.defaultString;

public class MojoExecutionDisplay {

    protected final MojoExecution execution;

    private final DefaultLifecycles defaultLifecycles;

    public MojoExecutionDisplay(MojoExecution execution) {
        this(execution, null);
    }

    public MojoExecutionDisplay(MojoExecution execution, DefaultLifecycles defaultLifecycles) {
        this.execution = execution;
        this.defaultLifecycles = defaultLifecycles;
    }

    /**
     * The phase this execution runs in: the one bound in the POM when there is one, otherwise the goal's
     * default phase from its descriptor. Maven 4 phase names are shown under their Maven 3 name.
     */
    public String getPhase() {
        if (execution.getLifecyclePhase() != null) {
            return PhaseNames.toMaven3Name(execution.getLifecyclePhase(), defaultLifecycles);
        }
        MojoDescriptor mojoDescriptor = execution.getMojoDescriptor();
        if (mojoDescriptor != null && mojoDescriptor.getPhase() != null) {
            return mojoDescriptor.getPhase();
        }
        return "";
    }

    public String getLifecycle(DefaultLifecycles defaultLifecycles) {
        Lifecycle lifecycle = defaultLifecycles.get(execution.getLifecyclePhase());
        return defaultString(lifecycle == null ? null : lifecycle.getId());
    }

    public String getArtifactId() {
        if (execution.getArtifactId() == null) {
            return "";
        }
        return MavenPluginPatterns.mayHighlightPrefix(execution.getArtifactId());
    }

    public String getVersion() {
        return defaultString(execution.getVersion());
    }

    public String getGoal() {
        return defaultString(execution.getGoal());
    }

    public String getExecutionId() {
        return defaultString(execution.getExecutionId());
    }
}

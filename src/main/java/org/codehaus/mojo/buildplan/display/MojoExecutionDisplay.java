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

import static org.codehaus.plexus.util.StringUtils.defaultString;

import org.apache.maven.lifecycle.DefaultLifecycles;
import org.apache.maven.lifecycle.Lifecycle;
import org.apache.maven.plugin.MojoExecution;
import org.apache.maven.plugin.descriptor.MojoDescriptor;

public class MojoExecutionDisplay {

    private final MojoExecution execution;

    public MojoExecutionDisplay(MojoExecution execution) {
        this.execution = execution;
    }

    public String getPhase() {
        MojoDescriptor mojoDescriptor = execution.getMojoDescriptor();
        if (mojoDescriptor != null && mojoDescriptor.getPhase() != null) {
            return mojoDescriptor.getPhase();
        }
        return defaultString(execution.getLifecyclePhase());
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

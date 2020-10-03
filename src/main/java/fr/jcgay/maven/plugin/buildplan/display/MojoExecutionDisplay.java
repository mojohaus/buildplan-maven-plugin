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
package fr.jcgay.maven.plugin.buildplan.display;

import org.apache.maven.plugin.MojoExecution;

import static com.google.common.base.Strings.nullToEmpty;

public class MojoExecutionDisplay {

    private final MojoExecution execution;

    public MojoExecutionDisplay(MojoExecution execution) {
        this.execution = execution;
    }

    public String getPhase() {
        return nullToEmpty(execution.getLifecyclePhase());
    }

    public String getArtifactId() {
        return nullToEmpty(execution.getArtifactId());
    }

    public String getGoal() {
        return nullToEmpty(execution.getGoal());
    }

    public String getExecutionId() {
        return nullToEmpty(execution.getExecutionId());
    }
}

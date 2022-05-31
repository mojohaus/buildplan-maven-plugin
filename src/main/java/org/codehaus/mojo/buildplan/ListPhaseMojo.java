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
package org.codehaus.mojo.buildplan;

import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;
import static org.codehaus.mojo.buildplan.display.Output.lineSeparator;

import java.util.Collection;
import java.util.Map;
import org.apache.maven.lifecycle.Lifecycle;
import org.apache.maven.plugin.MojoExecution;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.codehaus.mojo.buildplan.display.ListPhaseTableDescriptor;
import org.codehaus.mojo.buildplan.display.MojoExecutionDisplay;
import org.codehaus.mojo.buildplan.display.TableDescriptor;
import org.codehaus.mojo.buildplan.util.Multimap;
import org.codehaus.plexus.util.StringUtils;

/**
 * List plugin executions by phase for the current project.
 */
@Mojo(name = "list-phase",
      threadSafe = true,
      requiresProject = true)
public class ListPhaseMojo extends AbstractLifecycleMojo {

    /** Display plugin executions only for the specified phase. */
    @Parameter(property = "buildplan.phase")
    private String phase;

    @Parameter(property = "buildplan.showLifecycles", defaultValue = "false")
    private boolean showLifecycles;

    @Parameter(property = "buildplan.showAllPhases", defaultValue = "false")
    private boolean showAllPhases;

    public void executeInternal() throws MojoFailureException {
        Groups.Options options = new Groups.Options(defaultLifecycles);
        if (showAllPhases) {
            options = options.showingAllPhases();
        }
        if (showLifecycles) {
            options = options.showingLifecycles();
        }

        Multimap<String,MojoExecution> phases = Groups.ByPhase.of(calculateExecutionPlan().getMojoExecutions(), options);

        if (!phases.isEmpty()) {
            TableDescriptor descriptor = ListPhaseTableDescriptor.of(phases.values(), defaultLifecycles);
            Lifecycle currentLifecycle = null;
            StringBuilder output = new StringBuilder();
            for (Map.Entry<String, Collection<MojoExecution>> currentPhase : phases.asMap().entrySet()) {
                if (showLifecycles) {
                    Lifecycle lifecycleForPhase = defaultLifecycles.get(currentPhase.getKey());
                    if (lifecycleForPhase == null) {
                        lifecycleForPhase = new Lifecycle("", emptyList(), emptyMap());
                    }
                    if (!lifecycleForPhase.equals(currentLifecycle)) {
                        currentLifecycle = lifecycleForPhase;
                        output.append(lineSeparator()).append(lineSeparator()).append("[")
                            .append(currentLifecycle.getId()).append("]");
                    }
                }
                output.append(lineSeparator())
                    .append(phaseTitleLine(descriptor, currentPhase.getKey()));
                currentPhase.getValue().stream()
                    .filter(execution -> execution != NoMojoExecution.INSTANCE)
                    .forEach(execution -> output.append(lineSeparator()).append(line(descriptor.rowFormat(), execution)));
            }
            handleOutput(output.toString());
        } else {
            getLog().warn("No plugin execution found within phase: " + phase);
        }
    }

    private String line(String rowFormat, MojoExecution execution) {
        MojoExecutionDisplay display = new MojoExecutionDisplay(execution);

        return String.format(rowFormat, display.getArtifactId(),
                                        display.getExecutionId(),
                                        display.getGoal());
    }

    private String phaseTitleLine(TableDescriptor descriptor, String key) {
        return key + " " + StringUtils.repeat("-", descriptor.width() - key.length());
    }
}

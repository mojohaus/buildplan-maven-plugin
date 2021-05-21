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
package fr.jcgay.maven.plugin.buildplan;

import com.google.common.base.Strings;
import com.google.common.collect.Multimap;
import fr.jcgay.maven.plugin.buildplan.display.ListPhaseTableDescriptor;
import fr.jcgay.maven.plugin.buildplan.display.MojoExecutionDisplay;
import fr.jcgay.maven.plugin.buildplan.display.TableDescriptor;
import org.apache.maven.lifecycle.Lifecycle;
import org.apache.maven.lifecycle.MavenExecutionPlan;
import org.apache.maven.plugin.MojoExecution;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import static fr.jcgay.maven.plugin.buildplan.display.Output.lineSeparator;

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

    public void executeInternal() throws MojoExecutionException, MojoFailureException {

        MavenExecutionPlan executionPlan = calculateExecutionPlan();
        Multimap<String,MojoExecution> executionsByPhase = Groups.ByPhase.of(executionPlan.getMojoExecutions(), phase);

        Collection<String> phases;
        if (showAllPhases && phase == null) {
            phases = new ArrayList<String>();
            for (String phase : executionsByPhase.asMap().keySet()) {
                if (!phases.contains(phase)) {
                    Lifecycle lifecycle = defaultLifecycles.get(phase);
                    if (lifecycle != null) {
                      phases.addAll(lifecycle.getPhases());
                    } else {
                      phases.add(phase);
                    }
                }
            }
        } else {
            phases = executionsByPhase.asMap().keySet();
        }

        if (!phases.isEmpty()) {
            TableDescriptor descriptor = ListPhaseTableDescriptor.of(executionsByPhase.values(), defaultLifecycles);
            Lifecycle currentLifecycle = null;
            StringBuilder output = new StringBuilder();
            for (String phase : phases) {
                if (showLifecycles) {
                  Lifecycle lifecycleForPhase = defaultLifecycles.get(phase);
                  if (lifecycleForPhase == null) {
                      lifecycleForPhase = new Lifecycle("", Collections.EMPTY_LIST, Collections.EMPTY_MAP);
                  }
                  if (!lifecycleForPhase.equals(currentLifecycle)) {
                      currentLifecycle = lifecycleForPhase;
                      output.append(lineSeparator()).append(lineSeparator()).append("[")
                              .append(currentLifecycle.getId()).append("]");
                  }
                }
                output.append(lineSeparator())
                        .append(phaseTitleLine(descriptor, phase));
                Collection<MojoExecution> executions = executionsByPhase.get(phase);
                if (executions.isEmpty()) {
                    output.append(lineSeparator())
                            .append(empty(descriptor.rowFormat()));
                } else {
                    for (MojoExecution execution : executions) {
                        output.append(lineSeparator())
                                .append(line(descriptor.rowFormat(), execution));
                    }
                }
            }
            handleOutput(output.toString());
        } else {
            getLog().warn("No plugin execution found within phase: " + phase);
        }
    }

    private String empty(String rowFormat) {
        return String.format(rowFormat, "", "", "");
    }

    private String line(String rowFormat, MojoExecution execution) {

        MojoExecutionDisplay display = new MojoExecutionDisplay(execution);

        return String.format(rowFormat, display.getArtifactId(),
                                        display.getExecutionId(),
                                        display.getGoal());
    }

    private String phaseTitleLine(TableDescriptor descriptor, String key) {
        return key + " " + Strings.repeat("-", descriptor.width() - key.length());
    }
}

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
import org.apache.maven.plugin.MojoExecution;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.util.Collection;
import java.util.Map;

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

    public void execute() throws MojoExecutionException, MojoFailureException {

        Multimap<String,MojoExecution> phases = Groups.ByPhase.of(calculateExecutionPlan().getMojoExecutions(), phase);

        if (!phases.isEmpty()) {
            TableDescriptor descriptor = ListPhaseTableDescriptor.of(phases.values());
            StringBuilder output = new StringBuilder();
            for (Map.Entry<String, Collection<MojoExecution>> currentPhase : phases.asMap().entrySet()) {
                output.append(lineSeparator())
                        .append(phaseTitleLine(descriptor, currentPhase.getKey()));
                for (MojoExecution execution : currentPhase.getValue()) {
                    output.append(lineSeparator())
                            .append(line(descriptor.rowFormat(), execution));
                }
            }
            getLog().info(output.toString());
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
        return key + " " + Strings.repeat("-", descriptor.width() - key.length());
    }
}

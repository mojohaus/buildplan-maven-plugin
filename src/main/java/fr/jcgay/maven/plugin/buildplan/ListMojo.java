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
import fr.jcgay.maven.plugin.buildplan.display.ListTableDescriptor;
import fr.jcgay.maven.plugin.buildplan.display.MojoExecutionDisplay;
import fr.jcgay.maven.plugin.buildplan.display.TableDescriptor;
import org.apache.maven.lifecycle.MavenExecutionPlan;
import org.apache.maven.plugin.MojoExecution;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;

import static fr.jcgay.maven.plugin.buildplan.display.Output.lineSeparator;
import static fr.jcgay.maven.plugin.buildplan.display.TableColumn.ARTIFACT_ID;
import static fr.jcgay.maven.plugin.buildplan.display.TableColumn.EXECUTION_ID;
import static fr.jcgay.maven.plugin.buildplan.display.TableColumn.GOAL;
import static fr.jcgay.maven.plugin.buildplan.display.TableColumn.PHASE;

/**
 * List plugin executions for the current project.
 */
@Mojo(name = "list",
      threadSafe = true,
      requiresProject = true)
public class ListMojo extends AbstractLifecycleMojo {

    public void execute() throws MojoExecutionException, MojoFailureException {

        MavenExecutionPlan plan = calculateExecutionPlan();

        TableDescriptor descriptor = ListTableDescriptor.of(plan.getMojoExecutions());
        String row = descriptor.rowFormat();

        StringBuilder output = new StringBuilder()
                .append(titleSeparator(descriptor))
                .append(lineSeparator())
                .append(tableHead(row))
                .append(lineSeparator())
                .append(titleSeparator(descriptor));

        for (MojoExecution execution : plan.getMojoExecutions()) {
            output.append(lineSeparator())
                    .append(tableRow(row, execution));
        }

        handleOutput(output.toString());
    }

    private String tableRow(String row, MojoExecution execution) {

        MojoExecutionDisplay display = new MojoExecutionDisplay(execution);

        return String.format(row,
                display.getArtifactId(),
                display.getPhase(),
                display.getExecutionId(),
                display.getGoal());
    }

    private String tableHead(String row) {
        return String.format(row, ARTIFACT_ID.title(), PHASE.title(), EXECUTION_ID.title(), GOAL.title());
    }

    private String titleSeparator(TableDescriptor descriptor) {
        return Strings.repeat("-", descriptor.width());
    }
}

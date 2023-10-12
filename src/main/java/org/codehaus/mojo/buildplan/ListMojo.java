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

import static java.lang.System.lineSeparator;
import static java.util.Objects.isNull;
import static org.codehaus.mojo.buildplan.display.TableColumn.ARTIFACT_ID;
import static org.codehaus.mojo.buildplan.display.TableColumn.EXECUTION_ID;
import static org.codehaus.mojo.buildplan.display.TableColumn.GOAL;
import static org.codehaus.mojo.buildplan.display.TableColumn.LIFECYCLE;
import static org.codehaus.mojo.buildplan.display.TableColumn.PHASE;
import static org.codehaus.mojo.buildplan.display.TableColumn.VERSION;

import org.apache.maven.lifecycle.MavenExecutionPlan;
import org.apache.maven.plugin.MojoExecution;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.codehaus.mojo.buildplan.display.ListTableDescriptor;
import org.codehaus.mojo.buildplan.display.MojoExecutionDisplay;
import org.codehaus.mojo.buildplan.display.PlainTextMojoExecutionDisplay;
import org.codehaus.mojo.buildplan.display.TableDescriptor;
import org.codehaus.plexus.util.StringUtils;

/** List plugin executions for the current project. */
@Mojo(name = "list", threadSafe = true)
public class ListMojo extends AbstractLifecycleMojo {

    /** Will show in which lifecycle a phase was defined (if any) */
    @Parameter(property = "buildplan.showLifecycles", defaultValue = "false")
    private boolean showLifecycles;

    public void executeInternal() throws MojoFailureException {

        MavenExecutionPlan plan = calculateExecutionPlan();

        ListTableDescriptor descriptor = ListTableDescriptor.of(plan.getMojoExecutions(), defaultLifecycles);
        if (!showLifecycles) {
            descriptor.hideLifecycle();
        }

        descriptor.setFileOutput(!isNull(outputFile));

        String row = descriptor.rowFormat();
        String head = descriptor.titleFormat();

        StringBuilder output = new StringBuilder()
                .append(lineSeparator())
                .append(titleSeparator(descriptor))
                .append(lineSeparator())
                .append(tableHead(head))
                .append(lineSeparator())
                .append(titleSeparator(descriptor));

        for (MojoExecution execution : plan.getMojoExecutions()) {
            output.append(lineSeparator()).append(tableRow(row, execution));
        }

        handleOutput(output.toString());
    }

    private String tableRow(String row, MojoExecution execution) {

        MojoExecutionDisplay display =
                isNull(outputFile) ? new MojoExecutionDisplay(execution) : new PlainTextMojoExecutionDisplay(execution);

        if (showLifecycles) {
            return String.format(
                    row,
                    display.getLifecycle(defaultLifecycles),
                    display.getPhase(),
                    display.getArtifactId(),
                    display.getVersion(),
                    display.getGoal(),
                    display.getExecutionId());
        } else {
            return String.format(
                    row,
                    display.getPhase(),
                    display.getArtifactId(),
                    display.getVersion(),
                    display.getGoal(),
                    display.getExecutionId());
        }
    }

    private String tableHead(String row) {
        if (showLifecycles) {
            return String.format(
                    row,
                    LIFECYCLE.title(),
                    PHASE.title(),
                    ARTIFACT_ID.title(),
                    VERSION.title(),
                    GOAL.title(),
                    EXECUTION_ID.title());
        } else {
            return String.format(
                    row, PHASE.title(), ARTIFACT_ID.title(), VERSION.title(), GOAL.title(), EXECUTION_ID.title());
        }
    }

    private String titleSeparator(TableDescriptor descriptor) {
        return StringUtils.repeat("-", descriptor.width());
    }
}

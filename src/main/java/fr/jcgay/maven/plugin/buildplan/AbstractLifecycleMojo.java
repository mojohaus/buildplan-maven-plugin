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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.maven.execution.MavenSession;
import org.apache.maven.lifecycle.LifecycleExecutor;
import org.apache.maven.lifecycle.MavenExecutionPlan;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import fr.jcgay.maven.plugin.buildplan.display.Output;

public abstract class AbstractLifecycleMojo extends AbstractMojo {

    @Parameter(defaultValue = "${session}", readonly = true)
    private MavenSession session;

    @Component
    private LifecycleExecutor lifecycleExecutor;

    /** Allow to specify which tasks will be used to calculate execution plan. */
    @Parameter(property = "buildplan.tasks", defaultValue = "deploy")
    private String[] tasks;
    
    /** Allow to specify an output file to bypass console output */
    @Parameter(property = "buildplan.outputFile")
    private File outputFile;

    /** Allow to specify appending to the output file */
    @Parameter(property = "buildplan.appendOutput", defaultValue = "false")
    private boolean appendOutput;

    protected MavenExecutionPlan calculateExecutionPlan() throws MojoFailureException {
        try {
            return lifecycleExecutor.calculateExecutionPlan(session, tasks);
        } catch (Exception e) {
            throw new MojoFailureException(String.format("Cannot calculate Maven execution plan, caused by: %s", e.getMessage()), e);
        }
    }
    
    protected void handleOutput(final String output) {
        if (outputFile == null) {
            getLog().info(output);
        } else {
            try {
                final FileWriter writer = new FileWriter(outputFile, appendOutput);
                writer.write(output);
                writer.write(Output.lineSeparator());
                writer.close();
                getLog().info("Wrote buildplan output to " + outputFile);
            } catch (IOException e) {
                getLog().warn("Unable to write to output file", e);
            }
        }
    }
}

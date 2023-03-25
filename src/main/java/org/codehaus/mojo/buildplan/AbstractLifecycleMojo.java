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

import static java.util.Objects.isNull;

import java.io.File;
import java.io.IOException;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.lifecycle.DefaultLifecycles;
import org.apache.maven.lifecycle.LifecycleExecutor;
import org.apache.maven.lifecycle.MavenExecutionPlan;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

public abstract class AbstractLifecycleMojo extends AbstractMojo {

    @Component(role = DefaultLifecycles.class)
    protected DefaultLifecycles defaultLifecycles;

    @Parameter(defaultValue = "${session}", readonly = true)
    private MavenSession session;

    @Parameter(defaultValue = "${project}", readonly = true)
    private MavenProject project;

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

    /** Flag to easily skip all checks */
    @Parameter(property = "buildplan.skip", defaultValue = "false")
    private boolean skip;

    protected MavenExecutionPlan calculateExecutionPlan() throws MojoFailureException {
        try {
            return lifecycleExecutor.calculateExecutionPlan(session, tasks);
        } catch (Exception e) {
            throw new MojoFailureException(
                    String.format("Cannot calculate Maven execution plan, caused by: %s", e.getMessage()), e);
        }
    }

    protected void handleOutput(final String output) {
        String outputWithTitle = "Build Plan for " + project.getName() + ": " + output;
        if (isNull(outputFile)) {
            getLog().info(outputWithTitle);
            return;
        }

        try {
            SynchronizedFileReporter.write(outputWithTitle, outputFile, appendOutput);
            getLog().info("Wrote build plan output to: " + outputFile);
        } catch (IOException e) {
            getLog().warn("Unable to write to output file: " + outputFile, e);
        }
    }

    @Override
    public final void execute() throws MojoExecutionException, MojoFailureException {
        if (skip) {
            getLog().info("Skipping build plan execution.");
            return;
        }
        executeInternal();
    }

    protected abstract void executeInternal() throws MojoFailureException;
}

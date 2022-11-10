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

import static org.codehaus.mojo.buildplan.display.TableColumn.ARTIFACT_ID;
import static org.codehaus.mojo.buildplan.display.TableColumn.EXECUTION_ID;
import static org.codehaus.mojo.buildplan.display.TableColumn.GOAL;
import static org.codehaus.mojo.buildplan.display.TableColumn.LIFECYCLE;
import static org.codehaus.mojo.buildplan.display.TableColumn.PHASE;

import java.util.Locale;
import org.apache.maven.doxia.sink.Sink;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.lifecycle.DefaultLifecycles;
import org.apache.maven.lifecycle.LifecycleExecutor;
import org.apache.maven.lifecycle.MavenExecutionPlan;
import org.apache.maven.plugin.MojoExecution;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.reporting.AbstractMavenReport;
import org.apache.maven.reporting.MavenReportException;
import org.codehaus.mojo.buildplan.display.MojoExecutionDisplay;
import org.codehaus.mojo.buildplan.display.TableColumn;

/** Report plugin executions for the current project. */
@Mojo(name = "report", defaultPhase = LifecyclePhase.SITE)
public class ReportMojo extends AbstractMavenReport {

    @Component
    private LifecycleExecutor lifecycleExecutor;

    @Component(role = DefaultLifecycles.class)
    DefaultLifecycles defaultLifecycles;

    @Parameter(defaultValue = "${session}", readonly = true)
    private MavenSession session;

    @Override
    protected void executeReport(Locale locale) throws MavenReportException {

        MavenExecutionPlan plan;
        try {
            plan = calculateExecutionPlan();
        } catch (MojoFailureException e) {
            throw new MavenReportException("Cannot calculate execution plan...", e);
        }

        Log logger = getLog();
        logger.info(
                "Generating " + getOutputName() + ".html" + " for " + project.getName() + " " + project.getVersion());

        // Get the Maven Doxia Sink, which will be used to generate the
        // various elements of the document
        Sink mainSink = getSink();
        if (mainSink == null) {
            throw new MavenReportException("Could not get the Doxia sink");
        }

        // Page title
        mainSink.head();
        mainSink.title();
        mainSink.text("Build Plan for " + project.getName() + " " + project.getVersion());
        mainSink.title_();
        mainSink.head_();

        mainSink.body();

        mainSink.header();
        mainSink.rawText("<h1 id=\"titleContent\">" + getDescription(locale) + "</h1>");
        mainSink.header_();

        mainSink.table();

        mainSink.tableRow();
        tableHead(mainSink, LIFECYCLE);
        tableHead(mainSink, PHASE);
        tableHead(mainSink, ARTIFACT_ID);
        tableHead(mainSink, GOAL);
        tableHead(mainSink, EXECUTION_ID);
        mainSink.tableRow_();

        for (MojoExecution execution : plan.getMojoExecutions()) {
            MojoExecutionDisplay display = new MojoExecutionDisplay(execution);
            mainSink.tableRow();
            tableCell(mainSink, display.getLifecycle(defaultLifecycles));
            tableCell(mainSink, display.getPhase());
            tableCell(mainSink, display.getArtifactId());
            tableCell(mainSink, display.getGoal());
            tableCell(mainSink, display.getExecutionId());
            mainSink.tableRow_();
        }

        mainSink.table_();

        mainSink.body_();
    }

    private void tableCell(Sink mainSink, String content) {
        mainSink.tableCell();
        mainSink.text(content);
        mainSink.tableCell_();
    }

    private void tableHead(Sink mainSink, TableColumn lifecycle) {
        mainSink.tableHeaderCell();
        mainSink.text(lifecycle.title());
        mainSink.tableHeaderCell_();
    }

    @Override
    public String getOutputName() {
        return "buildplan-report";
    }

    @Override
    public String getName(Locale locale) {
        return "Build Plan";
    }

    @Override
    public String getDescription(Locale locale) {
        return "List plugin executions for " + currentProject();
    }

    private String currentProject() {
        return session.getCurrentProject().getGroupId()
                + ":"
                + session.getCurrentProject().getArtifactId();
    }

    protected MavenExecutionPlan calculateExecutionPlan() throws MojoFailureException {
        try {
            return lifecycleExecutor.calculateExecutionPlan(session, "deploy");
        } catch (Exception e) {
            throw new MojoFailureException(
                    String.format("Cannot calculate Maven execution plan, caused by: %s", e.getMessage()), e);
        }
    }
}

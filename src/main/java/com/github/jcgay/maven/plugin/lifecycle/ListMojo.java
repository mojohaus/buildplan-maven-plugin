package com.github.jcgay.maven.plugin.lifecycle;

import com.github.jcgay.maven.plugin.lifecycle.display.model.TableDescriptor;
import com.google.common.base.Strings;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.lifecycle.LifecycleExecutor;
import org.apache.maven.lifecycle.MavenExecutionPlan;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecution;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Mojo;

import static com.github.jcgay.maven.plugin.lifecycle.display.TableDisplayConfigurator.buildRowFormat;
import static com.github.jcgay.maven.plugin.lifecycle.display.TableDisplayConfigurator.findMaxSize;

@Mojo(name = "list", threadSafe = true)
public class ListMojo extends AbstractMojo {

    @Component
    private MavenSession session;

    @Component
    private LifecycleExecutor lifecycleExecutor;

    public void execute() throws MojoExecutionException, MojoFailureException {

        MavenExecutionPlan plan = calculateExecutionPlan();

        TableDescriptor descriptor = findMaxSize(plan.getMojoExecutions());
        String row = buildRowFormat(descriptor);

        getLog().info(lineSeparator(descriptor));
        getLog().info(tableHead(row));
        getLog().info(lineSeparator(descriptor));

        for (MojoExecution execution : plan.getMojoExecutions()) {
            getLog().info(tableRow(row, execution));
        }
    }

    private String tableRow(String row, MojoExecution execution) {
        return String.format(row,
                execution.getArtifactId(),
                execution.getMojoDescriptor().getPhase(),
                execution.getExecutionId(),
                execution.getMojoDescriptor().getGoal());
    }

    private String tableHead(String row) {
        return String.format(row, "PLUGIN", "PHASE", "ID", "GOAL");
    }

    private String lineSeparator(TableDescriptor descriptor) {
        return Strings.repeat("-", descriptor.width());
    }

    private MavenExecutionPlan calculateExecutionPlan() throws MojoFailureException {
        try {
            return lifecycleExecutor.calculateExecutionPlan(session, "deploy");
        } catch (Exception e) {
            throw new MojoFailureException(String.format("Cannot calculate Maven execution plan, caused by: %s", e.getMessage()));
        }
    }
}

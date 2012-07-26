package com.github.jcgay.maven.plugin.lifecycle;

import com.github.jcgay.maven.plugin.lifecycle.display.TableDescriptor;
import com.github.jcgay.maven.plugin.lifecycle.display.model.ListTableDescriptor;
import com.github.jcgay.maven.plugin.lifecycle.display.model.MojoExecutionDisplay;
import com.google.common.base.Strings;
import org.apache.maven.lifecycle.MavenExecutionPlan;
import org.apache.maven.plugin.MojoExecution;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;

@Mojo(name = "list", threadSafe = true)
public class ListMojo extends AbstractLifecycleMojo {

    public void execute() throws MojoExecutionException, MojoFailureException {

        MavenExecutionPlan plan = calculateExecutionPlan();

        TableDescriptor descriptor = ListTableDescriptor.of(plan.getMojoExecutions());
        String row = descriptor.rowFormat();

        getLog().info(lineSeparator(descriptor));
        getLog().info(tableHead(row));
        getLog().info(lineSeparator(descriptor));

        for (MojoExecution execution : plan.getMojoExecutions()) {
            getLog().info(tableRow(row, execution));
        }
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
        return String.format(row, "PLUGIN", "PHASE", "ID", "GOAL");
    }

    private String lineSeparator(TableDescriptor descriptor) {
        return Strings.repeat("-", descriptor.width());
    }
}

package com.github.jcgay.maven.plugin.lifecycle;

import com.github.jcgay.maven.plugin.lifecycle.display.TableDescriptor;
import com.github.jcgay.maven.plugin.lifecycle.display.model.ListPhaseTableDescriptor;
import com.github.jcgay.maven.plugin.lifecycle.display.model.MojoExecutionDisplay;
import com.google.common.base.Strings;
import com.google.common.collect.Multimap;
import org.apache.maven.plugin.MojoExecution;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.util.Collection;
import java.util.Map;

@Mojo(name = "list-phase", threadSafe = true)
public class ListPhaseMojo extends AbstractLifecycleMojo {

    @Parameter(property = "lifecycle.phase")
    private String phase;

    public void execute() throws MojoExecutionException, MojoFailureException {

        Multimap<String,MojoExecution> phases = Groups.ByPhase.of(calculateExecutionPlan().getMojoExecutions(), phase);
        TableDescriptor descriptor = ListPhaseTableDescriptor.of(phases.values());

        for (Map.Entry<String, Collection<MojoExecution>> phase : phases.asMap().entrySet()) {
            getLog().info(phaseTitleLine(descriptor, phase.getKey()));
            for (MojoExecution execution : phase.getValue()) {
                getLog().info(line(descriptor.rowFormat(), execution));
            }
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

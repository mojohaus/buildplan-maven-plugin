package com.github.jcgay.maven.plugin.lifecycle;

import com.github.jcgay.maven.plugin.lifecycle.display.TableDisplayConfigurator;
import com.github.jcgay.maven.plugin.lifecycle.display.model.TableDescriptor;
import com.google.common.base.Strings;
import com.google.common.collect.Multimap;
import org.apache.maven.plugin.MojoExecution;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;

import java.util.Collection;
import java.util.Map;

@Mojo(name = "list-phase", threadSafe = true)
public class ListPhaseMojo extends AbstractLifecycleMojo {

    public void execute() throws MojoExecutionException, MojoFailureException {

        Multimap<String,MojoExecution> phases = Groups.ByPhase.of(calculateExecutionPlan().getMojoExecutions());

        TableDescriptor descriptor = TableDisplayConfigurator.findMaxSize(phases.values());
        String rowFormat = TableDisplayConfigurator.buildRowFormatForListPhase(descriptor);

        for (Map.Entry<String, Collection<MojoExecution>> phase : phases.asMap().entrySet()) {
            getLog().info(phaseTitleLine(descriptor, phase.getKey()));
            for (MojoExecution execution : phase.getValue()) {
                getLog().info(line(rowFormat, execution));
            }
        }
    }

    private String line(String row, MojoExecution execution) {
        return String.format(row, execution.getArtifactId(),
                                  execution.getExecutionId(),
                                  execution.getMojoDescriptor().getGoal());
    }

    private String phaseTitleLine(TableDescriptor descriptor, String key) {
        int descriptorSize = descriptor.getGoalSize() + descriptor.getExecutionIdSize() + descriptor.getPluginSize() + 5;
        return key + " " + Strings.repeat("-", descriptorSize - key.length());
    }
}

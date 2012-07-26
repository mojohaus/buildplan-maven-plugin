package com.github.jcgay.maven.plugin.lifecycle;

import com.github.jcgay.maven.plugin.lifecycle.display.TableDescriptor;
import com.github.jcgay.maven.plugin.lifecycle.display.model.ListPluginTableDescriptor;
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

@Mojo(name = "list-plugin", threadSafe = true)
public class ListPluginMojo extends AbstractLifecycleMojo {

    @Parameter(property = "lifecycle.plugin")
    private String plugin;

    public void execute() throws MojoExecutionException, MojoFailureException {

        Multimap<String,MojoExecution> plan = Groups.ByPlugin.of(calculateExecutionPlan().getMojoExecutions(), plugin);

        if (!plan.isEmpty()) {

            TableDescriptor descriptor = ListPluginTableDescriptor.of(plan.values());
            for (Map.Entry<String, Collection<MojoExecution>> executions : plan.asMap().entrySet()) {
                getLog().info(pluginTitleLine(descriptor, executions.getKey()));
                for (MojoExecution execution : executions.getValue()) {
                    getLog().info(line(descriptor.rowFormat(), execution));
                }
            }
        }

        getLog().warn("No plugin found with artifactId: " + plugin);
    }

    private String line(String rowFormat, MojoExecution execution) {

        MojoExecutionDisplay display = new MojoExecutionDisplay(execution);

        return String.format(rowFormat, display.getPhase(),
                                        display.getExecutionId(),
                                        display.getGoal());
    }

    private String pluginTitleLine(TableDescriptor descriptor, String key) {
        return key + " " + Strings.repeat("-", descriptor.width() - key.length());
    }
}

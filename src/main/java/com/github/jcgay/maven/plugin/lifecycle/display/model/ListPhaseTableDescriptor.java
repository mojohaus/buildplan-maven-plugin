package com.github.jcgay.maven.plugin.lifecycle.display.model;

import com.github.jcgay.maven.plugin.lifecycle.display.TableDescriptor;
import org.apache.maven.plugin.MojoExecution;

import java.util.Collection;

public class ListPhaseTableDescriptor implements TableDescriptor {

    private int pluginSize;
    private int executionIdSize;
    private int goalSize;

    public static ListPhaseTableDescriptor of(Collection<MojoExecution> executions) {

        int sizePlugin = 0, sizeGoal = 0, sizeId = 0;

        for (MojoExecution execution : executions) {
            sizeId = Math.max(sizeId, execution.getExecutionId().length());
            sizePlugin = Math.max(sizePlugin, execution.getArtifactId().length());
            sizeGoal = Math.max(sizeGoal, execution.getGoal().length());
        }

        return new ListPhaseTableDescriptor().setPluginSize(sizePlugin)
                                             .setGoalSize(sizeGoal)
                                             .setExecutionIdSize(sizeId);
    }

    public String rowFormat() {
        StringBuilder builder = new StringBuilder();
        builder.append("    + ")
                .append(FORMAT_LEFT_ALIGN).append(getPluginSize()).append(FORMAT_STRING)
                .append(SEPARATOR)
                .append(FORMAT_LEFT_ALIGN).append(getExecutionIdSize()).append(FORMAT_STRING)
                .append(SEPARATOR)
                .append(FORMAT_LEFT_ALIGN).append(getGoalSize()).append(FORMAT_STRING);
        return builder.toString();
    }

    public int width() {
        return getExecutionIdSize() + getGoalSize() + getPluginSize() + 5;
    }

    public TableDescriptor plus(int size) {
        return null;

    }

    public int getPluginSize() {
        return pluginSize;
    }

    public int getExecutionIdSize() {
        return executionIdSize;
    }

    public int getGoalSize() {
        return goalSize;
    }

    public ListPhaseTableDescriptor setPluginSize(int pluginSize) {
        this.pluginSize = pluginSize;
        return this;
    }

    public ListPhaseTableDescriptor setExecutionIdSize(int executionIdSize) {
        this.executionIdSize = executionIdSize;
        return this;
    }

    public ListPhaseTableDescriptor setGoalSize(int goalSize) {
        this.goalSize = goalSize;
        return this;
    }
}

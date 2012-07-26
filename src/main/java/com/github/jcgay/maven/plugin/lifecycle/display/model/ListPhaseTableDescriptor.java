package com.github.jcgay.maven.plugin.lifecycle.display.model;

import org.apache.maven.plugin.MojoExecution;

import java.util.Collection;
import java.util.Map;

public class ListPhaseTableDescriptor extends AbstractTableDescriptor {

    static int SEPARATOR_SIZE = ROW_START.length() + 2 * SEPARATOR.length();

    private int pluginSize;
    private int executionIdSize;
    private int goalSize;

    public static ListPhaseTableDescriptor of(Collection<MojoExecution> executions) {

        Map<TableColumn,Integer> maxSize = findMaxSize(executions, TableColumn.ARTIFACT_ID, TableColumn.EXECUTION_ID, TableColumn.GOAL);

        return new ListPhaseTableDescriptor().setPluginSize(maxSize.get(TableColumn.ARTIFACT_ID))
                                             .setGoalSize(maxSize.get(TableColumn.GOAL))
                                             .setExecutionIdSize(maxSize.get(TableColumn.EXECUTION_ID));
    }

    public String rowFormat() {
        StringBuilder builder = new StringBuilder();
        builder.append(ROW_START)
               .append(FORMAT_LEFT_ALIGN).append(getPluginSize()).append(FORMAT_STRING)
               .append(SEPARATOR)
               .append(FORMAT_LEFT_ALIGN).append(getExecutionIdSize()).append(FORMAT_STRING)
               .append(SEPARATOR)
               .append(FORMAT_LEFT_ALIGN).append(getGoalSize()).append(FORMAT_STRING);
        return builder.toString();
    }

    public int width() {
        return getExecutionIdSize() + getGoalSize() + getPluginSize() + SEPARATOR_SIZE;
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

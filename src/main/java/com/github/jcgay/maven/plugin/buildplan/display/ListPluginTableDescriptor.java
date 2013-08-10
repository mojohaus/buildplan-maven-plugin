package com.github.jcgay.maven.plugin.buildplan.display;

import org.apache.maven.plugin.MojoExecution;

import java.util.Collection;
import java.util.Map;

public class ListPluginTableDescriptor extends AbstractTableDescriptor {

    static int SEPARATOR_SIZE = ROW_START.length() + 2 * SEPARATOR.length();

    private int phaseSize;
    private int executionIdSize;
    private int goalSize;

    public String rowFormat() {
        StringBuilder builder = new StringBuilder();
        builder.append(ROW_START)
                .append(FORMAT_LEFT_ALIGN).append(getPhaseSize()).append(FORMAT_STRING)
                .append(SEPARATOR)
                .append(FORMAT_LEFT_ALIGN).append(getExecutionIdSize()).append(FORMAT_STRING)
                .append(SEPARATOR)
                .append(FORMAT_LEFT_ALIGN).append(getGoalSize()).append(FORMAT_STRING);
        return builder.toString();
    }

    public int width() {
        return getExecutionIdSize() + getGoalSize() + getPhaseSize() + SEPARATOR_SIZE;
    }

    public int getPhaseSize() {
        return phaseSize;
    }

    public int getExecutionIdSize() {
        return executionIdSize;
    }

    public int getGoalSize() {
        return goalSize;
    }

    public ListPluginTableDescriptor setPhaseSize(int phaseSize) {
        this.phaseSize = phaseSize;
        return this;
    }

    public ListPluginTableDescriptor setExecutionIdSize(int executionIdSize) {
        this.executionIdSize = executionIdSize;
        return this;
    }

    public ListPluginTableDescriptor setGoalSize(int goalSize) {
        this.goalSize = goalSize;
        return this;
    }

    public static ListPluginTableDescriptor of(Collection<MojoExecution> executions) {

        Map<TableColumn,Integer> maxSize = findMaxSize(executions, TableColumn.PHASE, TableColumn.EXECUTION_ID, TableColumn.GOAL);

        return new ListPluginTableDescriptor().setPhaseSize(maxSize.get(TableColumn.PHASE))
                                              .setGoalSize(maxSize.get(TableColumn.GOAL))
                                              .setExecutionIdSize(maxSize.get(TableColumn.EXECUTION_ID));
    }
}

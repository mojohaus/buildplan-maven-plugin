package com.github.jcgay.maven.plugin.buildplan.display;

import org.apache.maven.plugin.MojoExecution;

import java.util.Collection;
import java.util.Map;

import static com.google.common.base.Objects.toStringHelper;

public class ListTableDescriptor extends AbstractTableDescriptor {

    static final int SEPARATOR_SIZE = 3 * SEPARATOR.length();

    private int pluginSize;
    private int phaseSize;
    private int executionIdSize;
    private int goalSize;

    public static ListTableDescriptor of(Collection<MojoExecution> executions) {

        Map<TableColumn,Integer> maxSize = findMaxSize(executions, TableColumn.values());

        return new ListTableDescriptor().setPluginSize(maxSize.get(TableColumn.ARTIFACT_ID))
                                        .setPhaseSize(maxSize.get(TableColumn.PHASE))
                                        .setGoalSize(maxSize.get(TableColumn.GOAL))
                                        .setExecutionIdSize(maxSize.get(TableColumn.EXECUTION_ID));
    }

    public String rowFormat() {
        StringBuilder builder = new StringBuilder();
        builder.append(FORMAT_LEFT_ALIGN).append(getPluginSize()).append(FORMAT_STRING)
               .append(SEPARATOR)
               .append(FORMAT_LEFT_ALIGN).append(getPhaseSize()).append(FORMAT_STRING)
               .append(SEPARATOR)
               .append(FORMAT_LEFT_ALIGN).append(getExecutionIdSize()).append(FORMAT_STRING)
               .append(SEPARATOR)
               .append(FORMAT_LEFT_ALIGN).append(getGoalSize()).append(FORMAT_STRING);
        return builder.toString();
    }

    public int width() {
        return getPluginSize() + getPhaseSize() + getExecutionIdSize() + getGoalSize() + SEPARATOR_SIZE;
    }

    @Override
    public String toString() {
        return toStringHelper(this).add("Plugin column size", getPluginSize())
                                   .add("Phase column size", getPhaseSize())
                                   .add("Execution ID column size", getExecutionIdSize())
                                   .add("Goal column size", getGoalSize())
                                   .add("width", width())
                                   .toString();
    }

    public int getPluginSize() {
        return pluginSize;
    }

    public ListTableDescriptor setPluginSize(int pluginSize) {
        this.pluginSize = pluginSize;
        return this;
    }

    public int getPhaseSize() {
        return phaseSize;
    }

    public ListTableDescriptor setPhaseSize(int phaseSize) {
        this.phaseSize = phaseSize;
        return this;
    }

    public int getExecutionIdSize() {
        return executionIdSize;
    }

    public ListTableDescriptor setExecutionIdSize(int executionIdSize) {
        this.executionIdSize = executionIdSize;
        return this;
    }

    public int getGoalSize() {
        return goalSize;
    }

    public ListTableDescriptor setGoalSize(int goalSize) {
        this.goalSize = goalSize;
        return this;
    }
}

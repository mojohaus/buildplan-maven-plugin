package com.github.jcgay.maven.plugin.lifecycle.display.model;

import static com.google.common.base.Objects.toStringHelper;

public class TableDescriptor {

    static final int SEPARATOR_SIZE = 3;

    private int pluginSize;
    private int phaseSize;
    private int executionIdSize;
    private int goalSize;

    public TableDescriptor plus(int size) {
        return new TableDescriptor().setPluginSize(getPluginSize() + size)
                                    .setPhaseSize(getPhaseSize() + size)
                                    .setExecutionIdSize(getExecutionIdSize() + size)
                                    .setGoalSize(getGoalSize() + size);
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

    public TableDescriptor setPluginSize(int pluginSize) {
        this.pluginSize = pluginSize;
        return this;
    }

    public int getPhaseSize() {
        return phaseSize;
    }

    public TableDescriptor setPhaseSize(int phaseSize) {
        this.phaseSize = phaseSize;
        return this;
    }

    public int getExecutionIdSize() {
        return executionIdSize;
    }

    public TableDescriptor setExecutionIdSize(int executionIdSize) {
        this.executionIdSize = executionIdSize;
        return this;
    }

    public int getGoalSize() {
        return goalSize;
    }

    public TableDescriptor setGoalSize(int goalSize) {
        this.goalSize = goalSize;
        return this;
    }
}

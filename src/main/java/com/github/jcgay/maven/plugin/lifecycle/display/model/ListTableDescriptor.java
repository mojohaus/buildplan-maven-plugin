package com.github.jcgay.maven.plugin.lifecycle.display.model;

import com.github.jcgay.maven.plugin.lifecycle.display.TableDescriptor;
import org.apache.maven.plugin.MojoExecution;

import java.util.Collection;

import static com.google.common.base.Objects.toStringHelper;

public class ListTableDescriptor implements TableDescriptor {

    static final int SEPARATOR_SIZE = 3;

    private int pluginSize;
    private int phaseSize;
    private int executionIdSize;
    private int goalSize;

    public static ListTableDescriptor of(Collection<MojoExecution> executions) {

        int sizePlugin = 0, sizePhase = 0, sizeGoal = 0, sizeId = 0;

        for (MojoExecution execution : executions) {
            sizeId = max(sizeId, execution.getExecutionId().length());
            sizePlugin = max(sizePlugin, execution.getArtifactId().length());
            sizeGoal = max(sizeGoal, execution.getGoal().length());
            if (execution.getMojoDescriptor() != null && execution.getMojoDescriptor().getPhase() != null) {
                sizePhase = max(sizePhase, execution.getMojoDescriptor().getPhase().length());
            }
        }

        return new ListTableDescriptor().setPluginSize(sizePlugin)
                                        .setPhaseSize(sizePhase)
                                        .setGoalSize(sizeGoal)
                                        .setExecutionIdSize(sizeId)
                                        .plus(2);
    }

    private static int max(int size, int anotherSize) {
        if (size >= anotherSize) {
            return size;
        } else {
            return anotherSize;
        }
    }

    public ListTableDescriptor plus(int size) {
        return new ListTableDescriptor().setPluginSize(getPluginSize() + size)
                                        .setPhaseSize(getPhaseSize() + size)
                                        .setExecutionIdSize(getExecutionIdSize() + size)
                                        .setGoalSize(getGoalSize() + size);
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

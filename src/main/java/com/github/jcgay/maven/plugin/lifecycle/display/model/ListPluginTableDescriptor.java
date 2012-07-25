package com.github.jcgay.maven.plugin.lifecycle.display.model;

import com.github.jcgay.maven.plugin.lifecycle.display.TableDescriptor;
import org.apache.maven.plugin.MojoExecution;

import java.util.Collection;

public class ListPluginTableDescriptor implements TableDescriptor {

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

        int sizePhase = 0, sizeGoal = 0, sizeId = 0;

        for (MojoExecution execution : executions) {
            sizeId = Math.max(sizeId, execution.getExecutionId().length());
            sizeGoal = Math.max(sizeGoal, execution.getGoal().length());
            if (execution.getMojoDescriptor() != null && execution.getMojoDescriptor().getPhase() != null) {
                sizePhase = Math.max(sizePhase, execution.getMojoDescriptor().getPhase().length());
            }
        }

        return new ListPluginTableDescriptor().setPhaseSize(sizePhase)
                                              .setGoalSize(sizeGoal)
                                              .setExecutionIdSize(sizeId);
    }
}

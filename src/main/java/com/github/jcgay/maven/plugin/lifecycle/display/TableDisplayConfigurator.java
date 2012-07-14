package com.github.jcgay.maven.plugin.lifecycle.display;

import com.github.jcgay.maven.plugin.lifecycle.display.model.TableDescriptor;
import org.apache.maven.plugin.MojoExecution;

import java.util.List;

public class TableDisplayConfigurator {

    private static final Character SEPARATOR = '|';
    private static final String FORMAT_LEFT_ALIGN = "%-";
    private static final Character FORMAT_STRING = 's';

    public static TableDescriptor findMaxSize(List<MojoExecution> executions) {

        int sizePlugin = 0, sizePhase = 0, sizeGoal = 0, sizeId = 0;

        for (MojoExecution execution : executions) {
            sizePlugin = max(sizePlugin, execution.getArtifactId().length());
            sizePhase = max(sizePhase, execution.getMojoDescriptor().getPhase().length());
            sizeGoal = max(sizeGoal, execution.getMojoDescriptor().getGoal().length());
            sizeId = max(sizeId, execution.getExecutionId().length());
        }

        return new TableDescriptor().setPluginSize(sizePlugin)
                                    .setPhaseSize(sizePhase)
                                    .setGoalSize(sizeGoal)
                                    .setExecutionIdSize(sizeId)
                                    .plus(2);
    }

    public static String buildRowFormat(TableDescriptor descriptor) {

        StringBuilder builder = new StringBuilder();
        builder.append(FORMAT_LEFT_ALIGN).append(descriptor.getPluginSize()).append(FORMAT_STRING)
               .append(SEPARATOR)
               .append(FORMAT_LEFT_ALIGN).append(descriptor.getPhaseSize()).append(FORMAT_STRING)
               .append(SEPARATOR)
               .append(FORMAT_LEFT_ALIGN).append(descriptor.getExecutionIdSize()).append(FORMAT_STRING)
               .append(SEPARATOR)
               .append(FORMAT_LEFT_ALIGN).append(descriptor.getGoalSize()).append(FORMAT_STRING);
        return builder.toString();
    }

    private static int max(int size, int anotherSize) {
        if (size >= anotherSize) {
            return size;
        } else {
            return anotherSize;
        }
    }
}

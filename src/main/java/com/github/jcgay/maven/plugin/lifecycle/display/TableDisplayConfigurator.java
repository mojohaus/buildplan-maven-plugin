package com.github.jcgay.maven.plugin.lifecycle.display;

import com.github.jcgay.maven.plugin.lifecycle.display.model.TableDescriptor;
import com.google.common.collect.Multimap;
import org.apache.maven.plugin.MojoExecution;

import java.util.Collection;
import java.util.List;

public class TableDisplayConfigurator {

    private static final Character SEPARATOR = '|';
    private static final String FORMAT_LEFT_ALIGN = "%-";
    private static final Character FORMAT_STRING = 's';

    public static TableDescriptor findMaxSize(Collection<MojoExecution> executions) {

        int sizePlugin = 0, sizePhase = 0, sizeGoal = 0, sizeId = 0;

        for (MojoExecution execution : executions) {
            sizeId = max(sizeId, execution.getExecutionId().length());
            sizePlugin = max(sizePlugin, execution.getArtifactId().length());
            sizeGoal = max(sizeGoal, execution.getGoal().length());
            if (execution.getMojoDescriptor() != null) {
                sizePhase = max(sizePhase, execution.getMojoDescriptor().getPhase().length());
            }
        }

        return new TableDescriptor().setPluginSize(sizePlugin)
                                    .setPhaseSize(sizePhase)
                                    .setGoalSize(sizeGoal)
                                    .setExecutionIdSize(sizeId)
                                    .plus(2);
    }

    public static String buildRowFormatForList(TableDescriptor descriptor) {

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

    public static String buildRowFormatForListPhase(TableDescriptor descriptor) {

        StringBuilder builder = new StringBuilder();
        builder.append("    + ")
               .append(FORMAT_LEFT_ALIGN).append(descriptor.getPluginSize()).append(FORMAT_STRING)
               .append(SEPARATOR)
               .append(FORMAT_LEFT_ALIGN).append(descriptor.getExecutionIdSize()).append(FORMAT_STRING)
               .append(SEPARATOR)
               .append(FORMAT_LEFT_ALIGN).append(descriptor.getGoalSize()).append(FORMAT_STRING);
        return builder.toString();
    }

    public static String buildRowFormatForListPlugin(TableDescriptor descriptor) {

        StringBuilder builder = new StringBuilder();
        builder.append("    + ")
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

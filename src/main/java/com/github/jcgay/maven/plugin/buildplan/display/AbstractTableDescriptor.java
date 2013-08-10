package com.github.jcgay.maven.plugin.buildplan.display;

import com.google.common.base.Strings;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.apache.maven.plugin.MojoExecution;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractTableDescriptor implements TableDescriptor {

    protected static Map<TableColumn, Integer> findMaxSize(Collection<MojoExecution> executions, TableColumn... columns) {

        Map<TableColumn, Integer> result = new HashMap<TableColumn, Integer>();

        Multimap<TableColumn, Integer> count = ArrayListMultimap.create();
        for (MojoExecution execution : executions) {
            for (TableColumn column : columns) {
                switch (column) {
                    case ARTIFACT_ID:
                        count.put(column, safeLength(execution.getArtifactId()));
                        break;
                    case EXECUTION_ID:
                        count.put(column, safeLength(execution.getExecutionId()));
                        break;
                    case GOAL:
                        count.put(column, safeLength(execution.getGoal()));
                        break;
                    case PHASE:
                        if (execution.getMojoDescriptor() != null) {
                            count.put(column, safeLength(execution.getMojoDescriptor().getPhase()));
                        }
                }
            }
        }

        for (TableColumn key : count.keySet()) {
            result.put(key, Collections.max(count.get(key)));
        }

        return result;
    }

    private static int safeLength(String string) {
        return Strings.nullToEmpty(string).length();
    }
}

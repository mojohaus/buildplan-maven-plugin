/**
 * Copyright (C) 2012 Jean-Christophe Gay (contact@jeanchristophegay.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package fr.jcgay.maven.plugin.buildplan.display;

import com.google.common.base.Strings;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.apache.maven.lifecycle.DefaultLifecycles;
import org.apache.maven.lifecycle.Lifecycle;
import org.apache.maven.plugin.MojoExecution;
import org.apache.maven.plugin.descriptor.MojoDescriptor;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractTableDescriptor implements TableDescriptor {

    protected static Map<TableColumn, Integer> findMaxSize(Collection<MojoExecution> executions, DefaultLifecycles defaultLifecycles, TableColumn... columns) {

        Map<TableColumn, Integer> result = new HashMap<>();

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
                        count.put(column, safeLength(phase(execution)));
                        break;
                    case LIFECYCLE:
                        Lifecycle lifecycle = defaultLifecycles.get(phase(execution));
                        count.put(column, lifecycle == null ? 0 : safeLength(lifecycle.getId()));
                }
            }
        }
        for (TableColumn column : TableColumn.values()) {
            count.put(column, column.title().length());
        }

        for (TableColumn key : count.keySet()) {
            result.put(key, Collections.max(count.get(key)));
        }

        return result;
    }

    private static String phase(MojoExecution execution) {
        MojoDescriptor mojoDescriptor = execution.getMojoDescriptor();
        if (mojoDescriptor != null && mojoDescriptor.getPhase() != null) {
            return mojoDescriptor.getPhase();
        }
        return execution.getLifecyclePhase();
    }

    private static int safeLength(String string) {
        return Strings.nullToEmpty(string).length();
    }
}

/*
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
package org.codehaus.mojo.buildplan.display;

import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

import org.apache.maven.lifecycle.DefaultLifecycles;
import org.apache.maven.lifecycle.Lifecycle;
import org.apache.maven.plugin.MojoExecution;
import org.codehaus.mojo.buildplan.util.LinkedMultimap;
import org.codehaus.mojo.buildplan.util.Multimap;

import static org.codehaus.plexus.util.StringUtils.defaultString;

public abstract class AbstractTableDescriptor implements TableDescriptor {

    protected static Map<TableColumn, Integer> findMaxSize(
            Collection<MojoExecution> executions, DefaultLifecycles defaultLifecycles, TableColumn... columns) {

        Map<TableColumn, Integer> result = new EnumMap<>(TableColumn.class);

        Multimap<TableColumn, Integer> count = new LinkedMultimap<>();
        executions.stream().map(MojoExecutionDisplay::new).forEach(execution -> {
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
                        count.put(column, safeLength(execution.getPhase()));
                        break;
                    case LIFECYCLE:
                        Lifecycle lifecycle = defaultLifecycles.get(execution.getPhase());
                        count.put(column, lifecycle == null ? 0 : safeLength(lifecycle.getId()));
                        break;
                    case VERSION:
                        count.put(column, safeLength(execution.getVersion()));
                        break;
                }
            }
        });
        for (TableColumn column : TableColumn.values()) {
            count.put(column, column.title().length());
        }

        for (TableColumn key : count.keySet()) {
            result.put(key, Collections.max(count.get(key)));
        }

        return result;
    }

    private static int safeLength(String string) {
        return defaultString(string).length();
    }
}

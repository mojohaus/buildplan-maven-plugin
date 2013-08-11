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

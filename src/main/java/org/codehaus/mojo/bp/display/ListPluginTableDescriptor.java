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
package org.codehaus.mojo.bp.display;

import org.apache.maven.lifecycle.DefaultLifecycles;
import org.apache.maven.plugin.MojoExecution;

import java.util.Collection;
import java.util.Map;

public class ListPluginTableDescriptor extends AbstractTableDescriptor {

    private static final int SEPARATOR_SIZE = ROW_START.length() + 2 * SEPARATOR.length();

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

    public static ListPluginTableDescriptor of(Collection<MojoExecution> executions, DefaultLifecycles defaultLifecycles) {

        Map<TableColumn,Integer> maxSize = findMaxSize(executions, defaultLifecycles, TableColumn.PHASE, TableColumn.EXECUTION_ID, TableColumn.GOAL);

        return new ListPluginTableDescriptor().setPhaseSize(maxSize.get(TableColumn.PHASE))
                                              .setGoalSize(maxSize.get(TableColumn.GOAL))
                                              .setExecutionIdSize(maxSize.get(TableColumn.EXECUTION_ID));
    }
}

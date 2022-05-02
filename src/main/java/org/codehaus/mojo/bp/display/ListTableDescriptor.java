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

import static com.google.common.base.Objects.toStringHelper;

public class ListTableDescriptor extends AbstractTableDescriptor {

    static final int SEPARATOR_SIZE = 4 * SEPARATOR.length();

    private int pluginSize;
    private int phaseSize;
    private int lifecycleSize;
    private int executionIdSize;
    private int goalSize;

    public static ListTableDescriptor of(Collection<MojoExecution> executions, DefaultLifecycles defaultLifecycles) {

        Map<TableColumn,Integer> maxSize = findMaxSize(executions, defaultLifecycles, TableColumn.values());

        return new ListTableDescriptor().setPluginSize(maxSize.get(TableColumn.ARTIFACT_ID))
                                        .setPhaseSize(maxSize.get(TableColumn.PHASE))
                                        .setLifecycleSize(maxSize.get(TableColumn.LIFECYCLE))
                                        .setGoalSize(maxSize.get(TableColumn.GOAL))
                                        .setExecutionIdSize(maxSize.get(TableColumn.EXECUTION_ID));
    }

    public String rowFormat() {
        StringBuilder builder = new StringBuilder();
        builder.append(FORMAT_LEFT_ALIGN).append(getPluginSize()).append(FORMAT_STRING)
               .append(SEPARATOR)
               .append(FORMAT_LEFT_ALIGN).append(getPhaseSize()).append(FORMAT_STRING)
               .append(SEPARATOR);
        if (lifecycleSize > 0) {
            builder.append(FORMAT_LEFT_ALIGN).append(getLifecycleSize()).append(FORMAT_STRING)
                   .append(SEPARATOR);
        }
        builder.append(FORMAT_LEFT_ALIGN).append(getExecutionIdSize()).append(FORMAT_STRING)
               .append(SEPARATOR)
               .append(FORMAT_LEFT_ALIGN).append(getGoalSize()).append(FORMAT_STRING);
        return builder.toString();
    }

    public int width() {
        return getPluginSize() + getPhaseSize() + getLifecycleSize() + getExecutionIdSize() + getGoalSize() + SEPARATOR_SIZE;
    }

    @Override
    public String toString() {
        return toStringHelper(this).add("Plugin column size", getPluginSize())
                                   .add("Phase column size", getPhaseSize())
                                   .add("Lifecycle column size", getLifecycleSize())
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

    public int getLifecycleSize() {
        return lifecycleSize;
    }

    public ListTableDescriptor setLifecycleSize(int lifecycleSize) {
        this.lifecycleSize = lifecycleSize;
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

    public void hideLifecycle() {
        this.lifecycleSize = -1 * SEPARATOR.length();
    }
}

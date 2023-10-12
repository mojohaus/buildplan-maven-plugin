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

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.StringJoiner;
import org.apache.maven.lifecycle.DefaultLifecycles;
import org.apache.maven.plugin.MojoExecution;
import org.apache.maven.shared.utils.logging.MessageUtils;

public class ListTableDescriptor extends AbstractTableDescriptor {

    private static final int ANSI_COLOR_CODES_LENGTH =
            MessageUtils.buffer().mojo("").toString().length();

    private int pluginSize;
    private int versionSize;
    private int phaseSize;
    private int lifecycleSize;
    private int executionIdSize;
    private int goalSize;

    private boolean isFileOutput;

    public void setFileOutput(boolean fileOutput) {
        isFileOutput = fileOutput;
    }

    public static ListTableDescriptor of(Collection<MojoExecution> executions, DefaultLifecycles defaultLifecycles) {

        Map<TableColumn, Integer> maxSize = findMaxSize(executions, defaultLifecycles, TableColumn.values());

        return new ListTableDescriptor()
                .setPluginSize(maxSize.get(TableColumn.ARTIFACT_ID))
                .setVersionSize(maxSize.get(TableColumn.VERSION))
                .setPhaseSize(maxSize.get(TableColumn.PHASE))
                .setLifecycleSize(maxSize.get(TableColumn.LIFECYCLE))
                .setGoalSize(maxSize.get(TableColumn.GOAL))
                .setExecutionIdSize(maxSize.get(TableColumn.EXECUTION_ID));
    }

    private boolean isEscCodeEnabled() {
        return !isFileOutput && MessageUtils.isColorEnabled();
    }

    public String rowFormat() {
        return columns(getPluginSize());
    }

    private String columns(int pluginSize) {
        StringBuilder builder = new StringBuilder();
        if (lifecycleSize > 0) {
            builder.append(FORMAT_LEFT_ALIGN)
                    .append(getLifecycleSize())
                    .append(FORMAT_STRING)
                    .append(SEPARATOR);
        }
        builder.append(FORMAT_LEFT_ALIGN)
                .append(getPhaseSize())
                .append(FORMAT_STRING)
                .append(SEPARATOR)
                .append(FORMAT_LEFT_ALIGN)
                .append(pluginSize)
                .append(FORMAT_STRING)
                .append(SEPARATOR)
                .append(FORMAT_LEFT_ALIGN)
                .append(getVersionSize())
                .append(FORMAT_STRING)
                .append(SEPARATOR);

        builder.append(FORMAT_LEFT_ALIGN)
                .append(getGoalSize())
                .append(FORMAT_STRING)
                .append(SEPARATOR)
                .append(FORMAT_LEFT_ALIGN)
                .append(getExecutionIdSize())
                .append(FORMAT_STRING);
        return builder.toString();
    }

    public String titleFormat() {
        if (isEscCodeEnabled()) {
            return columns(getPluginSize() - ANSI_COLOR_CODES_LENGTH);
        }
        return columns(getPluginSize());
    }

    public int width() {
        return withSeparator(
                getPluginSize(),
                getVersionSize(),
                getPhaseSize(),
                getLifecycleSize(),
                getExecutionIdSize(),
                getGoalSize());
    }

    private int withSeparator(int... ints) {
        int width = Arrays.stream(ints).sum() + (ints.length - 1) * SEPARATOR.length();
        return isEscCodeEnabled() ? width - ANSI_COLOR_CODES_LENGTH : width;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", ListTableDescriptor.class.getSimpleName() + "[", "]")
                .add("Plugin column size=" + pluginSize)
                .add("Plugin version size=" + versionSize)
                .add("Phase column size=" + phaseSize)
                .add("Lifecycle column size=" + lifecycleSize)
                .add("Execution ID column size=" + executionIdSize)
                .add("Goal column size=" + goalSize)
                .add("width=" + width())
                .toString();
    }

    public int getPluginSize() {
        return pluginSize;
    }

    public ListTableDescriptor setPluginSize(int pluginSize) {
        this.pluginSize = pluginSize;
        return this;
    }

    private int getVersionSize() {
        return versionSize;
    }

    public ListTableDescriptor setVersionSize(int versionSize) {
        this.versionSize = versionSize;
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

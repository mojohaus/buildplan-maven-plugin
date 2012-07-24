package com.github.jcgay.maven.plugin.lifecycle.display.model;

import org.apache.maven.plugin.MojoExecution;

import static com.google.common.base.Strings.nullToEmpty;

public class MojoExecutionDisplay {

    private MojoExecution execution;

    public MojoExecutionDisplay(MojoExecution execution) {
        this.execution = execution;
    }

    public String getPhase() {
        return nullToEmpty(execution.getMojoDescriptor().getPhase());
    }

    public String getArtifactId() {
        return nullToEmpty(execution.getArtifactId());
    }

    public String getGoal() {
        return nullToEmpty(execution.getGoal());
    }

    public String getExecutionId() {
        return nullToEmpty(execution.getExecutionId());
    }
}

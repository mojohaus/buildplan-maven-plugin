package com.github.jcgay.maven.plugin.lifecycle;

import org.apache.maven.execution.MavenSession;
import org.apache.maven.lifecycle.LifecycleExecutor;
import org.apache.maven.lifecycle.MavenExecutionPlan;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;

public abstract class AbstractLifecycleMojo extends AbstractMojo {

    @Component
    protected MavenSession session;

    @Component
    protected LifecycleExecutor lifecycleExecutor;

    protected MavenExecutionPlan calculateExecutionPlan() throws MojoFailureException {
        try {
            return lifecycleExecutor.calculateExecutionPlan(session, "deploy");
        } catch (Exception e) {
            throw new MojoFailureException(String.format("Cannot calculate Maven execution plan, caused by: %s", e.getMessage()));
        }
    }
}

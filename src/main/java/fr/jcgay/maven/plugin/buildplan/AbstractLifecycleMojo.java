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
package fr.jcgay.maven.plugin.buildplan;

import org.apache.maven.execution.MavenSession;
import org.apache.maven.lifecycle.LifecycleExecutor;
import org.apache.maven.lifecycle.MavenExecutionPlan;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;

public abstract class AbstractLifecycleMojo extends AbstractMojo {

    @Component
    private MavenSession session;

    @Component
    private LifecycleExecutor lifecycleExecutor;

    protected MavenExecutionPlan calculateExecutionPlan() throws MojoFailureException {
        try {
            return lifecycleExecutor.calculateExecutionPlan(session, "deploy");
        } catch (Exception e) {
            throw new MojoFailureException(String.format("Cannot calculate Maven execution plan, caused by: %s", e.getMessage()), e);
        }
    }
}

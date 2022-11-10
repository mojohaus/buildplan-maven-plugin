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
package org.codehaus.mojo.buildplan;

import static org.codehaus.mojo.buildplan.util.Objects.firstNonNull;
import static org.codehaus.plexus.util.StringUtils.isEmpty;

import java.util.Collections;
import java.util.List;
import org.apache.maven.lifecycle.DefaultLifecycles;
import org.apache.maven.lifecycle.Lifecycle;
import org.apache.maven.plugin.MojoExecution;
import org.codehaus.mojo.buildplan.util.LinkedMultimap;
import org.codehaus.mojo.buildplan.util.Multimap;
import org.codehaus.plexus.logging.console.ConsoleLogger;

public class Groups {

    public static class ByPlugin {

        public static Multimap<String, MojoExecution> of(List<MojoExecution> executions) {
            return of(executions, null);
        }

        public static Multimap<String, MojoExecution> of(List<MojoExecution> executions, String artifactIdFilter) {
            Multimap<String, MojoExecution> result = new LinkedMultimap<>();
            boolean notFiltering = isEmpty(artifactIdFilter);
            for (MojoExecution execution : executions) {
                if (notFiltering || execution.getArtifactId().equalsIgnoreCase(artifactIdFilter)) {
                    result.put(execution.getArtifactId(), execution);
                }
            }
            return result;
        }
    }

    public static class ByPhase {

        public static Multimap<String, MojoExecution> of(List<MojoExecution> plan) {
            return of(plan, new Options(defaultLifecycles()));
        }

        public static Multimap<String, MojoExecution> of(List<MojoExecution> executions, String phaseFilter) {
            return of(executions, new Options(defaultLifecycles()).forPhase(phaseFilter));
        }

        public static Multimap<String, MojoExecution> of(List<MojoExecution> executions, Options options) {
            Multimap<String, MojoExecution> result = new LinkedMultimap<>();
            boolean notFiltering = isEmpty(options.phase);
            for (MojoExecution execution : executions) {
                String phase = firstNonNull(execution.getLifecyclePhase(), "<no phase>");
                if (options.showingAllPhases) {
                    Lifecycle lifecycle = options.defaultLifecycles.get(phase);
                    if (lifecycle != null) {
                        lifecycle
                                .getPhases()
                                .forEach(defaultPhase -> result.put(defaultPhase, NoMojoExecution.INSTANCE));
                    }
                }
                if (notFiltering || phase.equalsIgnoreCase(options.phase)) {
                    result.put(phase, execution);
                }
            }
            return result;
        }

        private static DefaultLifecycles defaultLifecycles() {
            return new DefaultLifecycles(Collections.emptyMap(), new ConsoleLogger());
        }
    }

    public static class Options {

        private final DefaultLifecycles defaultLifecycles;

        private String phase;
        private boolean showingAllPhases;
        private boolean showingLifecycles;

        public Options(DefaultLifecycles defaultLifecycles) {
            this.defaultLifecycles = defaultLifecycles;
        }

        public Options forPhase(String phase) {
            this.phase = phase;
            return this;
        }

        public Options showingAllPhases() {
            this.showingAllPhases = true;
            return this;
        }

        public Options showingLifecycles() {
            this.showingLifecycles = true;
            return this;
        }
    }
}

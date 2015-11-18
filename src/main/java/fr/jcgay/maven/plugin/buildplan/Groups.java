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

import com.google.common.base.Strings;
import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;
import org.apache.maven.plugin.MojoExecution;

import java.util.List;

import static com.google.common.base.Objects.firstNonNull;

public class Groups {

    public static class ByPlugin {

        public static Multimap<String, MojoExecution> of(List<MojoExecution> executions) {
            return of(executions, null);
        }

        public static Multimap<String,MojoExecution> of(List<MojoExecution> executions, String artifactIdFilter) {
            Multimap<String, MojoExecution> result = LinkedListMultimap.create();
            boolean notFiltering = Strings.isNullOrEmpty(artifactIdFilter);
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
            return of(plan, null);
        }

        public static Multimap<String, MojoExecution> of(List<MojoExecution> executions, String phaseFilter) {
            Multimap<String, MojoExecution> result = LinkedListMultimap.create();
            boolean notFiltering = Strings.isNullOrEmpty(phaseFilter);
            for (MojoExecution execution : executions) {
                String phase = firstNonNull(execution.getLifecyclePhase(), "default-phase");
                if (notFiltering || phase.equalsIgnoreCase(phaseFilter)) {
                    result.put(phase, execution);
                }
            }
            return result;
        }

    }
}

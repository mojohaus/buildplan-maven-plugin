package com.github.jcgay.maven.plugin.lifecycle;

import com.google.common.base.Strings;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import org.apache.maven.lifecycle.MavenExecutionPlan;
import org.apache.maven.plugin.MojoExecution;

import java.util.List;

public class Groups {

    public static class ByPlugin {

        public static Multimap<String, MojoExecution> of(List<MojoExecution> executions) {
            return of(executions, null);
        }

        public static Multimap<String,MojoExecution> of(List<MojoExecution> executions, String artifactIdFilter) {
            Multimap<String, MojoExecution> result = ArrayListMultimap.create();
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
            Multimap<String, MojoExecution> result = ArrayListMultimap.create();
            boolean notFiltering = Strings.isNullOrEmpty(phaseFilter);
            for (MojoExecution execution : executions) {
                String phase = getPhase(execution);
                if (notFiltering || phase.equalsIgnoreCase(phaseFilter)) {
                    result.put(phase, execution);
                }
            }
            return result;
        }

        private static String getPhase(MojoExecution execution) {
            if (execution.getMojoDescriptor() != null && execution.getMojoDescriptor().getPhase() != null) {
                return execution.getMojoDescriptor().getPhase();
            }
            return "default-phase";
        }
    }
}

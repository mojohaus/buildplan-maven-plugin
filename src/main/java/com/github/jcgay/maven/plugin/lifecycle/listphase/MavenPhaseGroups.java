package com.github.jcgay.maven.plugin.lifecycle.listphase;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import org.apache.maven.lifecycle.MavenExecutionPlan;
import org.apache.maven.plugin.MojoExecution;

import java.util.List;

public class MavenPhaseGroups {

    public static Multimap<String, MojoExecution> of(List<MojoExecution> plan) {

        Multimap<String, MojoExecution> result = ArrayListMultimap.create();
        for (MojoExecution execution : plan) {
            result.put(execution.getMojoDescriptor().getPhase(), execution);
        }
        return result;
    }
}

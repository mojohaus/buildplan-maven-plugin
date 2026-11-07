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
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.maven.lifecycle.DefaultLifecycles;
import org.apache.maven.lifecycle.Lifecycle;
import org.codehaus.plexus.logging.console.ConsoleLogger;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PhaseNamesTest {

    /** The shape of Maven 4's {@code org.apache.maven.api.Lifecycle}, as far as the lookup touches it. */
    public interface ApiLifecycle {
        Collection<Alias> aliases();
    }

    public interface Alias {
        String v3Phase();

        String v4Phase();
    }

    /** Maven 4's compat {@code Lifecycle} exposes its API model through {@code getDelegate()}. */
    public static class Maven4Lifecycle extends Lifecycle {
        public Maven4Lifecycle(String id) {
            super(id, Collections.emptyList(), Collections.emptyMap());
        }

        public ApiLifecycle getDelegate() {
            return () -> Arrays.asList(
                    alias("process-resources", "after:resources"), alias("prepare-package", "before:package"));
        }
    }

    private static Alias alias(String v3, String v4) {
        return new Alias() {
            public String v3Phase() {
                return v3;
            }

            public String v4Phase() {
                return v4;
            }
        };
    }

    private static DefaultLifecycles lifecycles(Lifecycle defaultLifecycle) {
        Map<String, Lifecycle> map = new HashMap<>();
        for (String id : DefaultLifecycles.STANDARD_LIFECYCLES) {
            map.put(id, new Lifecycle(id, Collections.emptyList(), Collections.emptyMap()));
        }
        map.put(defaultLifecycle.getId(), defaultLifecycle);
        return new DefaultLifecycles(map, new ConsoleLogger());
    }

    @Test
    void should_map_maven4_phase_names_to_their_maven3_alias() {
        DefaultLifecycles lifecycles = lifecycles(new Maven4Lifecycle("default"));

        assertThat(PhaseNames.toMaven3Name("after:resources", lifecycles)).isEqualTo("process-resources");
        assertThat(PhaseNames.toMaven3Name("before:package", lifecycles)).isEqualTo("prepare-package");
        assertThat(PhaseNames.toMaven3Name("compile", lifecycles)).isEqualTo("compile");
    }

    @Test
    void should_leave_phase_names_alone_on_maven3() {
        DefaultLifecycles lifecycles =
                lifecycles(new Lifecycle("default", Collections.emptyList(), Collections.emptyMap()));

        assertThat(PhaseNames.toMaven3Name("process-resources", lifecycles)).isEqualTo("process-resources");
        assertThat(PhaseNames.toMaven3Name("validate", null)).isEqualTo("validate");
        assertThat(PhaseNames.toMaven3Name(null, lifecycles)).isNull();
    }
}

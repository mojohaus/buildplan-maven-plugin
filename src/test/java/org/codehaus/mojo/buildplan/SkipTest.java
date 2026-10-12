/*
 * Copyright MojoHaus and Contributors
 * Based on work Copyright (C) 2012 Jean-Christophe Gay
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

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.apache.maven.plugin.logging.SystemStreamLog;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SkipTest {

    @Test
    void list_plugin_skips_execution() throws Exception {
        assertThat(executeSkipped(new ListPluginMojo())).containsExactly("Skipping build plan execution.");
    }

    @Test
    void list_phase_skips_execution() throws Exception {
        assertThat(executeSkipped(new ListPhaseMojo())).containsExactly("Skipping build plan execution.");
    }

    /** Runs the mojo with {@code buildplan.skip} set; the other parameters are left unset, so any real work would fail. */
    private static List<String> executeSkipped(AbstractLifecycleMojo mojo) throws Exception {
        List<String> infos = new ArrayList<>();
        mojo.setLog(new SystemStreamLog() {
            @Override
            public void info(CharSequence content) {
                infos.add(content.toString());
            }
        });
        Field skip = AbstractLifecycleMojo.class.getDeclaredField("skip");
        skip.setAccessible(true);
        skip.setBoolean(mojo, true);

        mojo.execute();

        return infos;
    }
}

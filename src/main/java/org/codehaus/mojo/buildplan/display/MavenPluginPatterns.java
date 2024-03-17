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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.maven.shared.utils.logging.MessageUtils;

enum MavenPluginPatterns {
    IN_FIRST("(.*)-maven-plugin"),
    IN_THE_MIDDLE("maven-(.*)-plugin");

    private final Pattern pattern;

    MavenPluginPatterns(String pattern) {
        this.pattern = Pattern.compile(pattern);
    }

    static String mayHighlightPrefix(String artifactId) {
        return Arrays.stream(values())
                .map(p -> p.pattern.matcher(artifactId))
                .filter(Matcher::matches)
                .findFirst()
                .map(matcher -> new StringBuilder(artifactId)
                        .replace(
                                matcher.start(1),
                                matcher.end(1),
                                MessageUtils.buffer().mojo(matcher.group(1)).toString())
                        .toString())
                .orElse(artifactId);
    }
}

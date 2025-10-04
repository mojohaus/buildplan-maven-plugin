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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static java.lang.System.lineSeparator;

class SynchronizedFileReporter {

    static synchronized void write(String output, File outputFile, boolean appendOutput) throws IOException {
        try (FileWriter writer = new FileWriter(outputFile, appendOutput)) {
            writer.write(lineSeparator());
            writer.write(output);
            writer.write(lineSeparator());
        }
    }
}

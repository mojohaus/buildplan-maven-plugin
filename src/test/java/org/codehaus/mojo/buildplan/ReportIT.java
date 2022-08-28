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

import static com.soebes.itf.extension.assertj.MavenITAssertions.assertThat;

import com.soebes.itf.jupiter.extension.MavenGoal;
import com.soebes.itf.jupiter.extension.MavenJupiterExtension;
import com.soebes.itf.jupiter.extension.MavenTest;
import com.soebes.itf.jupiter.maven.MavenExecutionResult;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

@MavenJupiterExtension
class ReportIT {

    @MavenTest
    @MavenGoal("site")
    void generate_report_for_multimodule_project(MavenExecutionResult result) {
        assertThat(result).isSuccessful()
            .project()
            .withFile("site/buildplan-report.html")
            .content()
            .satisfies(html -> {
                Document document = Jsoup.parse(html);
                assertThat(document.title()).isEqualTo("list-multimodule – Build Plan for list-multimodule 1.0-SNAPSHOT");
                assertThat(document.select("#titleContent").text()).isEqualTo("List plugin executions for org.codehaus.mojo.buildplan:list-multimodule");
                assertThat(document.select("table.bodyTable").outerHtml()).isEqualTo("<table border=\"0\" class=\"bodyTable\">\n"
                    + " <tbody>\n"
                    + "  <tr class=\"a\">\n"
                    + "   <th>LIFECYCLE</th>\n"
                    + "   <th>PHASE</th>\n"
                    + "   <th>PLUGIN</th>\n"
                    + "   <th>GOAL</th>\n"
                    + "   <th>EXECUTION_ID</th>\n"
                    + "  </tr>\n"
                    + "  <tr class=\"b\">\n"
                    + "   <td>default</td>\n"
                    + "   <td>install</td>\n"
                    + "   <td>maven-install-plugin</td>\n"
                    + "   <td>install</td>\n"
                    + "   <td>default-install</td>\n"
                    + "  </tr>\n"
                    + "  <tr class=\"a\">\n"
                    + "   <td>default</td>\n"
                    + "   <td>deploy</td>\n"
                    + "   <td>maven-deploy-plugin</td>\n"
                    + "   <td>deploy</td>\n"
                    + "   <td>default-deploy</td>\n"
                    + "  </tr>\n"
                    + " </tbody>\n"
                    + "</table>");
            });

        assertThat(result)
            .project()
            .withModule("module-a")
            .withFile("site/buildplan-report.html")
            .content()
            .satisfies(html -> {
                Document document = Jsoup.parse(html);
                assertThat(document.title()).isEqualTo("list-multimodule-module-a – Build Plan for list-multimodule-module-a 1.0-SNAPSHOT");
                assertThat(document.select("#titleContent").text()).isEqualTo("List plugin executions for org.codehaus.mojo.buildplan:list-multimodule-module-a");
                assertThat(document.select("table.bodyTable").outerHtml()).isNotEmpty();
            });

        assertThat(result)
            .project()
            .withModule("module-b")
            .withFile("site/buildplan-report.html")
            .content()
            .satisfies(html -> {
                Document document = Jsoup.parse(html);
                assertThat(document.title()).isEqualTo("list-multimodule-module-b – Build Plan for list-multimodule-module-b 1.0-SNAPSHOT");
                assertThat(document.select("#titleContent").text()).isEqualTo("List plugin executions for org.codehaus.mojo.buildplan:list-multimodule-module-b");
                assertThat(document.select("table.bodyTable").outerHtml()).isNotEmpty();
            });
    }
}

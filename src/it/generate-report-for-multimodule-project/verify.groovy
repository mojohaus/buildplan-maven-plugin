import org.jsoup.Jsoup

// same checks as the former ReportIT: jsoup comes from the test classpath (addTestClassPath)
def report = { String module ->
    def file = new File(basedir, (module ? module + '/' : '') + 'target/site/buildplan-report.html')
    assert file.isFile() : 'Missing ' + file
    Jsoup.parse(file, 'UTF-8')
}

def root = report(null)
assert root.title() == 'list-multimodule \u2013 Build Plan for list-multimodule 1.0-SNAPSHOT'
assert root.select('#titleContent').text() == 'List plugin executions for org.codehaus.mojo.buildplan:list-multimodule'
assert root.select('table.bodyTable').outerHtml() == '<table border="0" class="bodyTable">\n <tbody>\n  <tr class="a">\n   <th>LIFECYCLE</th>\n   <th>PHASE</th>\n   <th>PLUGIN</th>\n   <th>GOAL</th>\n   <th>EXECUTION ID</th>\n  </tr>\n  <tr class="b">\n   <td>default</td>\n   <td>install</td>\n   <td>maven-install-plugin</td>\n   <td>install</td>\n   <td>default-install</td>\n  </tr>\n  <tr class="a">\n   <td>default</td>\n   <td>deploy</td>\n   <td>maven-deploy-plugin</td>\n   <td>deploy</td>\n   <td>default-deploy</td>\n  </tr>\n </tbody>\n</table>'

for (module in ['module-a', 'module-b']) {
    def html = report(module)
    assert html.title() == "list-multimodule-${module} \u2013 Build Plan for list-multimodule-${module} 1.0-SNAPSHOT"
    assert html.select('#titleContent').text() == "List plugin executions for org.codehaus.mojo.buildplan:list-multimodule-${module}"
    assert !html.select('table.bodyTable').outerHtml().isEmpty()
}

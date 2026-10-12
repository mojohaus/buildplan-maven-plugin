// Doxia renders HTML that is not well-formed XML, so the report is checked with regular expressions.
def readReport = { String module ->
    def report = new File(basedir, (module ? module + '/' : '') + 'target/site/buildplan-report.html')
    assert report.isFile() : "Missing " + report
    return report.getText('UTF-8')
}

def titleOf = { String html ->
    def m = html =~ /(?s)<title>(.*?)<\/title>/
    assert m.find() : 'No <title> in the report'
    return m.group(1).replace('&#x2013;', '\u2013')
}

def titleContentOf = { String html ->
    def m = html =~ /(?s)<h1 id="titleContent">(.*?)<\/h1>/
    assert m.find() : 'No #titleContent in the report'
    return m.group(1)
}

// the opening tag of the report table and its rows, as [row class, cell texts]
def tableOf = { String html, String tableClass ->
    def m = html =~ /(?s)(<table[^>]*class="[^"]*\b/ + tableClass + /\b[^"]*"[^>]*>)(.*?)<\/table>/
    assert m.find() : 'No table.' + tableClass + ' in the report'
    def rows = []
    def r = m.group(2) =~ /(?s)<tr class="(\w+)">(.*?)<\/tr>/
    while (r.find()) {
        def cells = []
        def c = r.group(2) =~ /(?s)<t[hd]>(.*?)<\/t[hd]>/
        while (c.find()) {
            cells << c.group(1)
        }
        rows << [r.group(1), cells]
    }
    return [m.group(1), rows]
}

def expectedRows = [
    ['a', ['LIFECYCLE', 'PHASE', 'PLUGIN', 'GOAL', 'EXECUTION ID']],
    ['b', ['default', 'install', 'maven-install-plugin', 'install', 'default-install']],
    ['a', ['default', 'deploy', 'maven-deploy-plugin', 'deploy', 'default-deploy']]
]

def root = readReport(null)
assert titleOf(root) == 'list-multimodule \u2013 Build Plan for list-multimodule 1.0-SNAPSHOT'
assert titleContentOf(root) == 'List plugin executions for org.codehaus.mojo.buildplan:list-multimodule'
def rootTable = tableOf(root, 'table')
assert rootTable[0] == '<table class="table table-striped">'
assert rootTable[1] == expectedRows

['module-a', 'module-b'].each { module ->
    def html = readReport(module)
    assert titleOf(html) == 'list-multimodule-' + module + ' \u2013 Build Plan for list-multimodule-' + module + ' 1.0-SNAPSHOT'
    assert titleContentOf(html) == 'List plugin executions for org.codehaus.mojo.buildplan:list-multimodule-' + module
    assert !tableOf(html, 'table')[1].isEmpty()
}

return true

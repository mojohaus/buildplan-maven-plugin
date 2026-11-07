def lines = new File(basedir, 'build.log').readLines('UTF-8')

def phaseOf = { String executionId ->
    def row = lines.find { it.contains('| resources     | ' + executionId) }
    assert row != null : "build.log has no row for execution " + executionId
    return row.substring(0, row.indexOf('|')).trim()
}

assert phaseOf('early-resources') == 'validate'
assert phaseOf('late-resources') == 'process-test-resources'
assert phaseOf('default-resources') == 'process-resources'

return true

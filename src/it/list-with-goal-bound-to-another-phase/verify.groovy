def lines = new File(basedir, 'build.log').readLines('UTF-8')

def row = lines.find { it.contains('| resources     | early-resources') }
assert row != null : "build.log has no row for execution early-resources"
assert row.startsWith('validate ') : "early-resources is bound to validate but listed as: " + row

return true

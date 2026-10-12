def lines = new File(basedir, 'build.log').readLines('UTF-8')

// the expected lines must follow each other directly in the log, as AssertJ containsSequence does
def assertSequence = { List<String> expected ->
    assert Collections.indexOfSubList(lines, expected) >= 0 : "build.log does not contain the sequence:\n" + expected.join('\n')
}
def infoLines = lines.findAll { it.startsWith('[INFO] ') }.collect { it.substring('[INFO] '.length()) }

assert infoLines.contains('Skipping build plan execution.')

return true

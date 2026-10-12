def lines = new File(basedir, 'build.log').readLines('UTF-8')

// the expected lines must follow each other directly in the log, as AssertJ containsSequence does
def assertSequence = { List<String> expected ->
    assert Collections.indexOfSubList(lines, expected) >= 0 : "build.log does not contain the sequence:\n" + expected.join('\n')
}

assertSequence([
    '-------------------------------------------------------------------------------------------------',
    'PHASE                  | PLUGIN                 | VERSION | GOAL          | EXECUTION ID         ',
    '-------------------------------------------------------------------------------------------------',
    'process-resources      | maven-\u001b[32mresources\u001b[m-plugin | 2.6     | resources     | default-resources    ',
    'compile                | maven-\u001b[32mcompiler\u001b[m-plugin  | 3.1     | compile       | default-compile      ',
    'process-test-resources | maven-\u001b[32mresources\u001b[m-plugin | 2.6     | testResources | default-testResources',
    'test-compile           | maven-\u001b[32mcompiler\u001b[m-plugin  | 3.1     | testCompile   | default-testCompile  ',
    'test                   | maven-\u001b[32msurefire\u001b[m-plugin  | 2.12.4  | test          | default-test         ',
    'package                | maven-\u001b[32mjar\u001b[m-plugin       | 2.4     | jar           | default-jar          ',
    'install                | maven-\u001b[32minstall\u001b[m-plugin   | 2.4     | install       | default-install      ',
    'deploy                 | maven-\u001b[32mdeploy\u001b[m-plugin    | 2.7     | deploy        | default-deploy       '
])

return true

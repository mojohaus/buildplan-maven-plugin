def lines = new File(basedir, 'build.log').readLines('UTF-8')

// the expected lines must follow each other directly in the log, as AssertJ containsSequence does
def assertSequence = { List<String> expected ->
    assert Collections.indexOfSubList(lines, expected) >= 0 : "build.log does not contain the sequence:\n" + expected.join('\n')
}

assertSequence([
    'maven-resources-plugin ----------------------------------------------',
    '    + process-resources      | resources     | default-resources    ',
    '    + process-test-resources | testResources | default-testResources',
    'maven-compiler-plugin -----------------------------------------------',
    '    + compile                | compile       | default-compile      ',
    '    + test-compile           | testCompile   | default-testCompile  ',
    'maven-surefire-plugin -----------------------------------------------',
    '    + test                   | test          | default-test         ',
    'maven-jar-plugin ----------------------------------------------------',
    '    + package                | jar           | default-jar          ',
    'maven-install-plugin ------------------------------------------------',
    '    + install                | install       | default-install      ',
    'maven-deploy-plugin -------------------------------------------------',
    '    + deploy                 | deploy        | default-deploy       '
])

return true

def lines = new File(basedir, 'build.log').readLines('UTF-8')

// the expected lines must follow each other directly in the log, as AssertJ containsSequence does
def assertSequence = { List<String> expected ->
    assert Collections.indexOfSubList(lines, expected) >= 0 : "build.log does not contain the sequence:\n" + expected.join('\n')
}

def maven4 = mavenVersion.startsWith('4.')
assertSequence(maven4 ? [
    '[clean]',
    'clean ---------------------------------------------------------------',
    '    + maven-clean-plugin     | clean         | default-clean        ',
    '',
    '[default]',
    'after:resources -----------------------------------------------------',
    '    + maven-resources-plugin | resources     | default-resources    ',
    'compile -------------------------------------------------------------',
    '    + maven-compiler-plugin  | compile       | default-compile      ',
    'after:test-resources ------------------------------------------------',
    '    + maven-resources-plugin | testResources | default-testResources',
    'test-compile --------------------------------------------------------',
    '    + maven-compiler-plugin  | testCompile   | default-testCompile  ',
    'test ----------------------------------------------------------------',
    '    + maven-surefire-plugin  | test          | default-test         ',
    'package -------------------------------------------------------------',
    '    + maven-jar-plugin       | jar           | default-jar          ',
    'install -------------------------------------------------------------',
    '    + maven-install-plugin   | install       | default-install      ',
    'deploy --------------------------------------------------------------',
    '    + maven-deploy-plugin    | deploy        | default-deploy       '
] : [
    '[clean]',
    'clean ---------------------------------------------------------------',
    '    + maven-clean-plugin     | clean         | default-clean        ',
    '',
    '[default]',
    'process-resources ---------------------------------------------------',
    '    + maven-resources-plugin | resources     | default-resources    ',
    'compile -------------------------------------------------------------',
    '    + maven-compiler-plugin  | compile       | default-compile      ',
    'process-test-resources ----------------------------------------------',
    '    + maven-resources-plugin | testResources | default-testResources',
    'test-compile --------------------------------------------------------',
    '    + maven-compiler-plugin  | testCompile   | default-testCompile  ',
    'test ----------------------------------------------------------------',
    '    + maven-surefire-plugin  | test          | default-test         ',
    'package -------------------------------------------------------------',
    '    + maven-jar-plugin       | jar           | default-jar          ',
    'install -------------------------------------------------------------',
    '    + maven-install-plugin   | install       | default-install      ',
    'deploy --------------------------------------------------------------',
    '    + maven-deploy-plugin    | deploy        | default-deploy       '
])

return true

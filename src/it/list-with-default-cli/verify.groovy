def lines = new File(basedir, 'build.log').readLines('UTF-8')

// the expected lines must follow each other directly in the log, as AssertJ containsSequence does
def assertSequence = { List<String> expected ->
    assert Collections.indexOfSubList(lines, expected) >= 0 : "build.log does not contain the sequence:\n" + expected.join('\n')
}

assertSequence([
    '[INFO] Build Plan for list-multimodule: ',
    '--------------------------------------------------------------------',
    'PHASE   | PLUGIN               | VERSION | GOAL    | EXECUTION ID   ',
    '--------------------------------------------------------------------',
    'install | maven-install-plugin | 2.4     | install | default-install',
    'deploy  | maven-deploy-plugin  | 2.7     | deploy  | default-deploy '
])

assertSequence([
    '[INFO] Build Plan for list-multimodule-module-a: ',
    '-------------------------------------------------------------------------------------------------',
    'PHASE                  | PLUGIN                 | VERSION | GOAL          | EXECUTION ID         ',
    '-------------------------------------------------------------------------------------------------',
    'process-resources      | maven-resources-plugin | 2.6     | resources     | default-resources    ',
    'compile                | maven-compiler-plugin  | 3.1     | compile       | default-compile      ',
    'process-test-resources | maven-resources-plugin | 2.6     | testResources | default-testResources',
    'test-compile           | maven-compiler-plugin  | 3.1     | testCompile   | default-testCompile  ',
    'test                   | maven-surefire-plugin  | 2.12.4  | test          | default-test         ',
    'package                | maven-jar-plugin       | 2.4     | jar           | default-jar          ',
    'install                | maven-install-plugin   | 2.4     | install       | default-install      ',
    'deploy                 | maven-deploy-plugin    | 2.7     | deploy        | default-deploy       '
])

assertSequence([
    '[INFO] Build Plan for list-multimodule-module-b: ',
    '----------------------------------------------------------------------------------------------------',
    'PHASE                  | PLUGIN                 | VERSION | GOAL             | EXECUTION ID         ',
    '----------------------------------------------------------------------------------------------------',
    'process-resources      | maven-resources-plugin | 2.6     | resources        | default-resources    ',
    'compile                | maven-compiler-plugin  | 3.1     | compile          | default-compile      ',
    'process-test-resources | maven-resources-plugin | 2.6     | testResources    | default-testResources',
    'test-compile           | maven-compiler-plugin  | 3.1     | testCompile      | default-testCompile  ',
    'test                   | maven-surefire-plugin  | 2.12.4  | test             | default-test         ',
    'package                | maven-jar-plugin       | 2.4     | jar              | default-jar          ',
    'integration-test       | maven-failsafe-plugin  | 2.19.1  | integration-test | default              ',
    'verify                 | maven-failsafe-plugin  | 2.19.1  | verify           | default              ',
    'install                | maven-install-plugin   | 2.4     | install          | default-install      ',
    'deploy                 | maven-deploy-plugin    | 2.7     | deploy           | default-deploy       '
])

return true

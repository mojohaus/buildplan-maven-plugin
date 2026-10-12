def lines = new File(basedir, 'build.log').readLines('UTF-8')

// the expected lines must follow each other directly in the log, as AssertJ containsSequence does
def assertSequence = { List<String> expected ->
    assert Collections.indexOfSubList(lines, expected) >= 0 : "build.log does not contain the sequence:\n" + expected.join('\n')
}

assertSequence([
    '[INFO] Build Plan for list-multimodule: ',
    '--------------------------------------------------------------------------------',
    'LIFECYCLE | PHASE   | PLUGIN               | VERSION | GOAL    | EXECUTION ID   ',
    '--------------------------------------------------------------------------------',
    'clean     | clean   | maven-clean-plugin   | 2.5     | clean   | default-clean  ',
    'default   | install | maven-install-plugin | 2.4     | install | default-install',
    'default   | deploy  | maven-deploy-plugin  | 2.7     | deploy  | default-deploy '
])

assertSequence([
    '[INFO] Build Plan for list-multimodule-module-a: ',
    '-------------------------------------------------------------------------------------------------------------',
    'LIFECYCLE | PHASE                  | PLUGIN                 | VERSION | GOAL          | EXECUTION ID         ',
    '-------------------------------------------------------------------------------------------------------------',
    'clean     | clean                  | maven-clean-plugin     | 2.5     | clean         | default-clean        ',
    'default   | process-resources      | maven-resources-plugin | 2.6     | resources     | default-resources    ',
    'default   | compile                | maven-compiler-plugin  | 3.1     | compile       | default-compile      ',
    'default   | process-test-resources | maven-resources-plugin | 2.6     | testResources | default-testResources',
    'default   | test-compile           | maven-compiler-plugin  | 3.1     | testCompile   | default-testCompile  ',
    'default   | test                   | maven-surefire-plugin  | 2.12.4  | test          | default-test         ',
    'default   | package                | maven-jar-plugin       | 2.4     | jar           | default-jar          ',
    'default   | install                | maven-install-plugin   | 2.4     | install       | default-install      ',
    'default   | deploy                 | maven-deploy-plugin    | 2.7     | deploy        | default-deploy       '
])

assertSequence([
    '[INFO] Build Plan for list-multimodule-module-b: ',
    '----------------------------------------------------------------------------------------------------------------',
    'LIFECYCLE | PHASE                  | PLUGIN                 | VERSION | GOAL             | EXECUTION ID         ',
    '----------------------------------------------------------------------------------------------------------------',
    'clean     | clean                  | maven-clean-plugin     | 2.5     | clean            | default-clean        ',
    'default   | process-resources      | maven-resources-plugin | 2.6     | resources        | default-resources    ',
    'default   | compile                | maven-compiler-plugin  | 3.1     | compile          | default-compile      ',
    'default   | process-test-resources | maven-resources-plugin | 2.6     | testResources    | default-testResources',
    'default   | test-compile           | maven-compiler-plugin  | 3.1     | testCompile      | default-testCompile  ',
    'default   | test                   | maven-surefire-plugin  | 2.12.4  | test             | default-test         ',
    'default   | package                | maven-jar-plugin       | 2.4     | jar              | default-jar          ',
    'default   | integration-test       | maven-failsafe-plugin  | 2.19.1  | integration-test | default              ',
    'default   | verify                 | maven-failsafe-plugin  | 2.19.1  | verify           | default              ',
    'default   | install                | maven-install-plugin   | 2.4     | install          | default-install      ',
    'default   | deploy                 | maven-deploy-plugin    | 2.7     | deploy           | default-deploy       '
])

return true

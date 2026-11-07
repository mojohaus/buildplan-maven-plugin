def output = new File(basedir, 'list.txt').getText('UTF-8')

assert !output.contains('\u001b') : "list.txt contains ANSI escape codes:\n" + output

def expected = [
    'Build Plan for list-outputfile-and-color: ',
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
]
assert Collections.indexOfSubList(output.readLines(), expected) >= 0 : "list.txt does not contain the expected table:\n" + output

return true

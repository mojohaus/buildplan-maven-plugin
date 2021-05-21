#!/usr/bin/env groovy
String buildLog = new File(basedir, 'build.log').text

check(buildLog, new File(basedir, 'expected.txt').text)
check(buildLog, new File(basedir, '/module-a/expected.txt').text)
check(buildLog, new File(basedir, '/module-b/expected.txt').text)

def check(String actual, String expected) {
    try {
        assert actual.contains(expected)
    } catch (AssertionError error) {
        assert printable(actual).contains(printable(expected))
    }
}

String printable(String string) {
    string.replaceAll(" ", "·").replaceAll( "[\n\r]+", "¬\n" )
}




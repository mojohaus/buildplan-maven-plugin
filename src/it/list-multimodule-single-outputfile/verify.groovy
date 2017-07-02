#!/usr/bin/env groovy

check(new File(basedir, 'list.txt').text, new File(basedir, 'expected.txt').text)

def check(String actual, String expected) {
    try {
        assert actual == expected
    } catch (AssertionError error) {
        assert printable(actual) == printable(expected)
    }
}

String printable(String string) {
    string.replaceAll(" ", "·").replaceAll( "[\n\r]+", "¬\n" )
}




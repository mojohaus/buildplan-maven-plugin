package com.github.jcgay.maven.plugin.lifecycle.display;

public interface TableDescriptor {

    Character SEPARATOR = '|';
    String FORMAT_LEFT_ALIGN = "%-";
    Character FORMAT_STRING = 's';

    String rowFormat();

    int width();

    TableDescriptor plus(int size);
}

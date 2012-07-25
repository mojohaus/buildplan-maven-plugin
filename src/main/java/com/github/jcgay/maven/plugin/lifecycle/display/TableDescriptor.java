package com.github.jcgay.maven.plugin.lifecycle.display;

public interface TableDescriptor {

    String SEPARATOR = " | ";
    String FORMAT_LEFT_ALIGN = "%-";
    Character FORMAT_STRING = 's';
    String ROW_START = "    + ";

    String rowFormat();

    int width();
}

package com.github.jcgay.maven.plugin.buildplan.display;

public interface TableDescriptor {

    String SEPARATOR = " | ";
    String FORMAT_LEFT_ALIGN = "%-";
    Character FORMAT_STRING = 's';
    String ROW_START = "    + ";

    String rowFormat();

    int width();
}

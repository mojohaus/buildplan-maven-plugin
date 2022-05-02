# MojoHaus BuildPlan Maven Plugin

[![Apache License, Version 2.0, January 2004](https://img.shields.io/github/license/mojohaus/bp-maven-plugin.svg?label=License)](http://www.apache.org/licenses/)
[![Maven Central](https://img.shields.io/maven-central/v/org.codehaus.mojo/bp-maven-plugin.svg?label=Maven%20Central)](https://search.maven.org/artifact/org.codehaus.mojo/bp-maven-plugin)
[![Build Status](https://github.com/mojohaus/bp-maven-plugin/workflows/GitHub%20CI/badge.svg?branch=master)](https://github.com/mojohaus/bp-maven-plugin/actions/workflows/maven.yml?query=branch%3Amaster)

A Maven 3.x plugin to inspect the lifecycle of your project. [Documentation](http://www.mojohaus.org/bp-maven-plugin/)

### List plugin executions within a project

	mvn bp:list

### List plugin executions within phases

	mvn bp:list-phase

It is possible to limit the list to a specific phase:

	mvn bp:list-phase -Dbp.phase=test

### List plugin executions by plugins

	mvn bp:list-plugin

It is possible to limit the list to a specific plugin:

	mvn bp:list-plugin -Dbp.plugin=maven-compiler-plugin

### List to output file

	mvn bp:list -Dbp.outputFile=buildplan_output.txt

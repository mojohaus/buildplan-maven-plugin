# MojoHaus BuildPlan Maven Plugin

[![Apache License, Version 2.0, January 2004](https://img.shields.io/github/license/mojohaus/buildplan-maven-plugin.svg?label=License)](http://www.apache.org/licenses/)
[![Maven Central](https://img.shields.io/maven-central/v/org.codehaus.mojo/buildplan-maven-plugin.svg?label=Maven%20Central)](https://search.maven.org/artifact/org.codehaus.mojo/buildplan-maven-plugin)
[![Build Status](https://github.com/mojohaus/buildplan-maven-plugin/actions/workflows/maven.yml/badge.svg)](https://github.com/mojohaus/buildplan-maven-plugin/actions/workflows/maven.yml)

A Maven plugin to inspect the lifecycle of your project: which plugin executions run, in which phase, and for
which plugin. Documentation: <https://www.mojohaus.org/buildplan-maven-plugin/>

Requires Maven 3.9.0 or newer (3.10 recommended; Maven 4 is also tested) and Java 8 or newer.

## Basic usage

### List plugin executions within a project

        mvn buildplan:list

### List plugin executions within phases

        mvn buildplan:list-phase

It is possible to limit the list to a specific phase:

        mvn buildplan:list-phase -Dbuildplan.phase=test

### List plugin executions by plugins

        mvn buildplan:list-plugin

It is possible to limit the list to a specific plugin:

        mvn buildplan:list-plugin -Dbuildplan.plugin=maven-compiler-plugin

### Choose the tasks the plan is calculated for

The plan is calculated for `deploy` by default. Any goals or phases can be given instead, with the profiles and
properties on the same invocation:

        mvn buildplan:list -Dbuildplan.tasks=clean,verify -Pcoverage

### Write the list to a file

        mvn buildplan:list -Dbuildplan.outputFile=buildplan_output.txt

Add `-Dbuildplan.appendOutput=true` to append instead of overwrite, which collects every module of a reactor in
one file. `-Dbuildplan.skip=true` skips the goal.

### Report plugin executions within a project

        mvn buildplan:report

Contributor and agent notes are in [AGENTS.md](AGENTS.md).

## Release

Release notes are drafted on [GitHub Releases](https://github.com/mojohaus/buildplan-maven-plugin/releases) from
the merged pull requests. Summary of the full [process](https://www.mojohaus.org/development/performing-a-release.html):

        $> mvn release:prepare release:perform
        $> cd target/checkout
        $> mvn site site:stage scm-publish:publish-scm


# MojoHaus BuildPlan Maven Plugin

A Maven plugin to inspect the lifecycle of your project: which plugin executions run, in which phase, and for which plugin.

It requires Maven 3.9.0 or newer (3.10 recommended; Maven 4 is also tested) and Java 8 or newer.

## Goals Overview

The BuildPlan plugin has four goals:

* [buildplan:list](list-mojo.html) displays plugin executions within a Maven project.
* [buildplan:list-phase](list-phase-mojo.html) displays plugin executions within lifecycle phases.
* [buildplan:list-plugin](list-plugin-mojo.html) displays plugin executions by plugin.
* [buildplan:report](report-mojo.html) generates the same list as an HTML report, standalone or as part of the project site.

## Examples

To provide you with better understanding on some usages of the BuildPlan plugin, you can take a look at the [usage](usage.html) examples.

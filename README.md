#buildplan-maven-plugin

A Maven 3.x plugin to inspect the lifecycle of your project. [Documentation](http://buildplan.jcgay.fr/)

###Usage

Activate the plugin group in your Maven *settings.xml*:

	<pluginGroups>
	    <pluginGroup>fr.jcgay.maven.plugins</pluginGroup>
  	</pluginGroups>

###List plugin executions within a project

	mvn buildplan:list
	
###List plugin executions within phases

	mvn buildplan:list-phase

It is possible to limit the list to a specific phase:
	
	mvn buildplan:list-phase -Dbuildplan.phase=test
	
###List plugin executions by plugins

	mvn buildplan:list-plugin

It is possible to limit the list to a specific plugin:

	mvn buildplan:list-plugin -Dbuildplan.plugin=maven-compiler-plugin

# Build

## Status

[![Build Status](https://travis-ci.org/jcgay/buildplan-maven-plugin.svg?branch=master)](https://travis-ci.org/jcgay/buildplan-maven-plugin)
[![Coverage Status](https://coveralls.io/repos/jcgay/buildplan-maven-plugin/badge.svg?branch=master)](https://coveralls.io/r/jcgay/buildplan-maven-plugin?branch=master)

## Release

    mvn -B release:prepare release:perform

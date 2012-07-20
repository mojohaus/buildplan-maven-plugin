package com.github.jcgay.maven.plugin.lifecycle;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;

@Mojo(name = "list-plugins", threadSafe = true)
public class ListPluginsMojo extends AbstractLifecycleMojo {

    public void execute() throws MojoExecutionException, MojoFailureException {

        // plugins
        // phase executionid goal


    }
}

package zzq.mavenplugins.jmockitautoutmavenplugin;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;

@Mojo(name = "generator")
public class Generator extends AbstractMojo {

    @Parameter(defaultValue = "${project.build.sourceDirectory}")
    private File sourceDirectory;

    @Parameter(defaultValue = "${project.build.testSourceDirectory}")
    private File testSourceDirectory;

    public void execute()
            throws MojoExecutionException {
        GenerateUtil.generateMockAndTestFile(sourceDirectory,sourceDirectory,testSourceDirectory);
    }
}

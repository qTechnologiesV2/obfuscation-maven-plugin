package dev.mdma.qprotect;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;

@Mojo(name = "obfuscate",
        defaultPhase = LifecyclePhase.PACKAGE,
        requiresDependencyResolution = ResolutionScope.COMPILE_PLUS_RUNTIME)
public class ObfuscationMojo extends AbstractMojo {

    @Parameter(property = "obfuscation.skip", defaultValue = "false")
    private boolean isSkip;

    @Parameter(property = "obfuscation.path", required = true)
    private File obfuscatorPath;

    @Parameter(property = "obfuscation.configFile", required = true)
    private File configFile;

    @Parameter(property = "obfuscation.inputFile")
    private File inputFile;

    @Parameter(property = "obfuscation.outputFile")
    private File outputFile;

    @Parameter(property = "obfuscation.javaPath")
    private File javaPath;

    @Parameter(property = "project", readonly = true, required = true)
    protected MavenProject mavenProject;

    @Override
    public void execute() throws MojoExecutionException {
        if (isSkip) {
            getLog().info("Skipping qProtect obfuscation because isSkip is set to 'true'");
            return;
        }

        if (obfuscatorPath == null || !obfuscatorPath.exists()) {
            throw new MojoExecutionException("qProtect Obfuscator Path is null or does not exist.");
        }

        if (configFile == null || !configFile.exists()) {
            throw new MojoExecutionException("qProtect Obfuscator Config Path is null or does not exist.");
        }

        if(Objects.nonNull(javaPath))
            System.out.println("Using custom java path " + javaPath);

        try {
            Process process = createProecess();

            // Capture the log
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    getLog().info(line);
                }
            }

            int exitCode = process.waitFor();

            if (exitCode != 0) {
                throw new MojoExecutionException("qProtect failed with exit code: " + exitCode);
            }
        } catch (IOException | InterruptedException e) {
            throw new MojoExecutionException("An exception occurred while running the qProtect process.", e);
        }
    }

    private Process createProecess() throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder(
                Objects.isNull(javaPath) ? "java" : javaPath + File.separator + "bin" + File.separator + "java",
                "-jar",
                obfuscatorPath.getAbsolutePath(),
                "--config",
                configFile.getAbsolutePath()
        );

        if (inputFile != null) {
            processBuilder.command().add("--input");
            processBuilder.command().add(inputFile.getAbsolutePath());
        }

        if (outputFile != null) {
            processBuilder.command().add("--output");
            processBuilder.command().add(outputFile.getAbsolutePath());
        }

        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();
        return process;
    }
}

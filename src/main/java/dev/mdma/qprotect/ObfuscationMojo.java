package dev.mdma.qprotect;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;

import javax.inject.Inject;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;
import java.util.logging.Logger;

@Mojo(name = "obfuscate",
        defaultPhase = LifecyclePhase.PACKAGE,
        requiresDependencyResolution = ResolutionScope.COMPILE_PLUS_RUNTIME)
public class ObfuscationMojo extends AbstractMojo {
    @Inject
    private Logger logger;

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
            logger.info("Skipping qProtect obfuscation because isSkip is set to 'true'");
            return;
        }

        if (obfuscatorPath == null) {
            throw new MojoExecutionException("qProtect installation path is null");
        }

        if(!obfuscatorPath.exists()) {
            throw new MojoExecutionException("qProtect installation not found at: " + obfuscatorPath.getAbsoluteFile());
        }

        if (configFile == null || !configFile.exists()) {
            throw new MojoExecutionException("qProtect config path is null");
        }

        if( !configFile.exists()) {
            throw new MojoExecutionException("qProtect config not found at: " + configFile.getAbsoluteFile());
        }

        if(Objects.nonNull(javaPath))
            logger.info("Using custom java path: " + javaPath);
        try {
            Process process = createProcess();

            // Capture the log
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
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

    private Process createProcess() throws IOException {
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
        return processBuilder.start();
    }
}

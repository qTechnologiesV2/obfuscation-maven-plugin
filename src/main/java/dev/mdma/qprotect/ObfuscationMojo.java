package dev.mdma.qprotect;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

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

    @Parameter(property = "obfuscation.inputFile", required = true)
    private File inputFile;

    @Parameter(property = "obfuscation.outputFile", required = true)
    private File outputFile;

    @Parameter(property = "project", readonly = true, required = true)
    protected MavenProject mavenProject;

    @Override
    public void execute() throws MojoFailureException {
        if (isSkip) {
            getLog().info("Skipping qProtect obfuscation because isSkip is set to 'true'");
            return;
        }

        if (obfuscatorPath == null || obfuscatorPath.length() == 0 || !obfuscatorPath.exists()) {
            throw new MojoFailureException("qProtect Obfuscator Path is null.");
        }

        if (configFile == null || configFile.length() == 0 || !configFile.exists()) {
            throw new MojoFailureException("qProtect Obfuscator Config Path is null.");
        }

        try {
            ClassLoader loader = URLClassLoader.newInstance(
                    new URL[]{obfuscatorPath.toURI().toURL()},
                    getClass().getClassLoader()
            );

            Thread.currentThread().setContextClassLoader(loader);

            Class<?> clazz = Class.forName("de.xbrowniecodez.utils.Bootstrap", true, loader);
            Constructor<?> ctor = clazz.getConstructor();
            Object instance = ctor.newInstance();

            clazz.getMethod("main", String[].class).invoke(instance, (Object) new String[]{configFile.getAbsolutePath(), "--input", inputFile.getAbsolutePath(), "--output", outputFile.getAbsolutePath()});
        } catch (MalformedURLException | InstantiationException | InvocationTargetException | NoSuchMethodException | IllegalAccessException | ClassNotFoundException e) {
            throw new MojoFailureException("An exception occurred while trying to invoke main method.", e);
        }
    }
}

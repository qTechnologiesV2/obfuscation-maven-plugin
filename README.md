# 🔐 Obfuscation Maven Plugin

A Maven plugin for automating obfuscation using **qProtect** or **qProtect Lite**.

---

## 📥 Installation

### Add the Plugin Repository

Add the following repository to your `pom.xml` so Maven can resolve the plugin:

```xml
<pluginRepositories>
    <pluginRepository>
        <id>nexus-releases</id>
        <url>https://nexus.mdma.dev/repository/maven-releases/</url>
    </pluginRepository>
</pluginRepositories>
```

---

## 🛠️ Configuration

Add the plugin to your Maven build configuration:

```xml
<plugin>
    <groupId>dev.mdma.qprotect</groupId>
    <artifactId>obfuscation-maven-plugin</artifactId>
    <version>1.0.5</version>

    <configuration>
        <!-- Path to qProtect or qProtect Lite -->
        <obfuscatorPath>C:\qprotect-core-1.10.8.jar</obfuscatorPath>

        <!-- Path to qProtect configuration file -->
        <configFile>${project.basedir}/config.yml</configFile>

        <!-- Optional: can also be defined inside config.yml -->
        <inputFile>${project.basedir}/target/test-1.0.jar</inputFile>
        <outputFile>${project.basedir}/target/test-1.0-obf.jar</outputFile>

        <!-- Optional: custom Java runtime for launching qProtect -->
        <javaPath>C:\Program Files\Java\jdk-1.8</javaPath>
    </configuration>

    <executions>
        <execution>
            <goals>
                <goal>obfuscate</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```

---

## 🔗 qProtect Compatibility

This plugin is compatible with the following qProtect versions:

| Plugin Version | Compatible qProtect Versions |
|----------------|------------------------------|
| 1.0.5          | qProtect **1.x** and **2.x** |

---

## ☕ Java Version Compatibility

The `javaPath` option exists to ensure compatibility with different **qProtect** versions:

| qProtect Version | Required Java Version |
| ---------------- | --------------------- |
| qProtect 1.x     | Java 8                |
| qProtect 2.x     | Java 21 or newer      |

If your **project’s Java version differs** from the version required by qProtect, you **must** explicitly set `javaPath` so the plugin can launch qProtect with the correct Java runtime.

### Examples

**qProtect 1.x (Java 8):**

```xml
<javaPath>C:\Program Files\Java\jdk-1.8</javaPath>
```

**qProtect 2.x (Java 21+):**

```xml
<javaPath>C:\Program Files\Java\jdk-21</javaPath>
```

> 💡 If `javaPath` is not set, the plugin will use the Java version Maven is running with. This may cause qProtect to fail if the Java versions are incompatible.

---

## 📄 qProtect Configuration

Ensure the following paths are correctly defined either in the plugin configuration **or** in your `config.yml` file:

* ✅ qProtect JAR path
* 📥 Input JAR path
* 📤 Output JAR path
* ⚙️ qProtect config

> 💡 `inputFile` and `outputFile` are optional if already defined in `config.yml`.

---

## 🚀 Usage

Build and obfuscate your project by running:

```bash
mvn package
```

The obfuscated JAR will be generated automatically in the configured output location.

---

## 🧩 Requirements

* Maven 3.x
* Java 6+ (project Java version)
* Java 8 or Java 21+ available for qProtect (depending on version)

---

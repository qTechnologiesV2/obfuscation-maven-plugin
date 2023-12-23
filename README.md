
# obfuscation-maven-plugin
A maven plugin used to obfuscate a compiled file with qProtect and qProtect Lite

## Instructions
- add our repository as pluginRepository

```xml
<pluginRepositories>
    <pluginRepository>
        <id>nexus-releases</id>
        <url>https://nexus.mdma.dev/repository/maven-releases/</url>
    </pluginRepository>
</pluginRepositories>
```

 - add our plugin into your maven build

```xml
<plugin>
   <groupId>dev.mdma.qprotect</groupId>
   <artifactId>obfuscation-maven-plugin</artifactId>
   <version>1.0.3</version>
   <configuration>
       <obfuscatorPath>C:\qprotect-core-1.10.8.jar</obfuscatorPath>
       <configFile>${project.basedir}/config.yml</configFile>
       <!-- inputFile and outputFile is optional, it can be set here or in the config -->
       <inputFile>${project.basedir}/target/test-1.0.jar</inputFile>
       <outputFile>${project.basedir}/target/test-1.0-obf.jar</outputFile>
       <!-- javaPath is only required if your project doesn't use java 8-->
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
- set the qprotect and the input, output and config path
- then build your project using maven package



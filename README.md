
# obfuscation-maven-plugin
A maven plugin used to obfuscate a compiled file with qProtect and qProtect Lite

## Instructions
- clone the repository
-  install the dependency using maven install
- add the following dependency and plugin to your pom.xml

```xml
<dependency>
   <groupId>dev.mdma.qprotect</groupId>
   <artifactId>obfuscation-maven-plugin</artifactId>
   <version>1.0.0</version>
</dependency>
            
<plugin>
   <groupId>dev.mdma.qprotect</groupId>
   <artifactId>obfuscation-maven-plugin</artifactId>
   <version>1.0.0</version>
   <configuration>
      <obfuscatorPath>C:\qprotect-core-1.9.4-release.jar</obfuscatorPath>
      <configFile>C:\config.yml</configFile>
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
- set the qprotect and the config path
- then build your project using maven package



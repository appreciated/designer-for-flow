# Documentation

### Application

#### [Architecture](application/architecture)

### Packaging

To allow the end-user to use the application easily the Designer-for-flow application is being packaged using electron.

```
Electron
 |
 |--OpenJDK
      |
      |--Vaadin Application     
```

The whole packaging process is done via Maven in the pom.xml of the project. 
The electron related file can be found in `src/main/electron`.
            
Since multi-platform packaging caused issues the OpenJDK is being extracted on the first startup by the Electron part of the application.

To package the application run
``
mvn clean install -Pproduction
``
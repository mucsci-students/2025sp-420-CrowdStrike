# CrowdStrike UML
A cli uml edditor.

## Table of Contents
- [Running](#running)
  - [Tests](#tests)
  - [Project](#project)
- [Usage](#useage)

## Running
### Tests
First you must download [junit](https://repo1.maven.org/maven2/org/junit/platform/junit-platform-console-standalone/1.11.4/junit-platform-console-standalone-1.11.4.jar) and place it in the lib directory.
[junit download](https://repo1.maven.org/maven2/org/junit/platform/junit-platform-console-standalone/1.11.4/junit-platform-console-standalone-1.11.4.jar)
#### Linux/Macos
```sh
javac -d out/ -cp lib/*.jar src/*.java
javac -d out/ -cp lib/*.jar:out test/*.java
java -jar lib/junit-platform-console-standalone-1.11.4.jar execute -cp out:lib/*.jar --scan-class-path
```

#### Windows
```sh
javac -d out/ -cp lib/*.jar src/*.java
javac -d out/ -cp lib/*.jar;out test/*.java
java -jar lib/junit-platform-console-standalone-1.11.4.jar execute -cp out;lib/*.jar --scan-class-path
```

### Project
#### Linux/Macos
```sh
javac -d out/ -cp lib/*.jar src/*.java
jar cfe uml.jar CommandLineUMLClassEditorApp -C out/ .
java -jar uml.jar
```

#### Windows
```sh
javac -d out/ -cp lib/*.jar src/*.java
jar cfe uml.jar CommandLineUMLClassEditorApp -C out/ .
java -jar uml.jar
```
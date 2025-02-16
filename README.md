# CrowdStrike UML
A cli uml edditor.

## Table of Contents
- [Running](#running)
  - [Tests](#tests)
  - [Project](#project)
- [Usage](#useage)

## Running
First you must download [junit](https://repo1.maven.org/maven2/org/junit/platform/junit-platform-console-standalone/1.11.4/junit-platform-console-standalone-1.11.4.jar) and [gson](https://repo1.maven.org/maven2/com/google/code/gson/gson/2.12.1/gson-2.12.1.jar) place them in the lib directory.

[junit download](https://repo1.maven.org/maven2/org/junit/platform/junit-platform-console-standalone/1.11.4/junit-platform-console-standalone-1.11.4.jar)

[gson download](https://repo1.maven.org/maven2/com/google/code/gson/gson/2.12.1/gson-2.12.1.jar)
### Tests
#### Linux/Macos
```sh
javac -d out/ -cp lib/gson-2.12.1.jar:lib/junit-platform-console-standalone-1.11.4.jar src/*.java
javac -d out/ -cp lib/gson-2.12.1.jar:lib/junit-platform-console-standalone-1.11.4.jar:out test/*.java
java -jar lib/junit-platform-console-standalone-1.11.4.jar execute -cp out:lib/*.jar --scan-class-path
```

#### Windows
```sh
javac -d out/ -cp lib/gson-2.12.1.jar;lib/junit-platform-console-standalone-1.11.4.jar src/*.java
javac -d out/ -cp lib/gson-2.12.1.jar;lib/junit-platform-console-standalone-1.11.4.jar;out test/*.java
java -jar lib/junit-platform-console-standalone-1.11.4.jar execute -cp out;lib/*.jar --scan-class-path
```

### Project
#### Linux/Macos
```sh
javac -d out/ -cp lib/gson-2.12.1.jar:lib/junit-platform-console-standalone-1.11.4.jar src/*.java
jar cfe uml.jar CommandLineUMLClassEditorApp -C out/ .
java -jar uml.jar
```

#### Windows
```sh
javac -d out/ -cp lib/gson-2.12.1.jar:lib/junit-platform-console-standalone-1.11.4.jar src/*.java
jar cfe uml.jar CommandLineUMLClassEditorApp -C out/ .
java -jar uml.jar
```
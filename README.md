# CrowdStrike UML
A cli uml edditor.

## Table of Contents
- [Running](#running)
  - [Tests](#tests)
  - [Project](#project)
- [Usage](#useage)

## Running
### Tests
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

## Useage
TODO: add valid commands and info on eatch.
# CrowdStrike UML
Welcome to Team CrowdStrike's UML Class Editor: a Java project featuring a command line interface that allows users to create customized classes, describe them with attributes, and relate them in a user-friendly text format. 

Created for MU's CSCI420 in the Spring semester of 2025.

## Table of Contents
- [Installing](#installing)
- [Running](#running)
  - [Tests](#tests)
  - [Project](#project)
- [Usage](#usage)
- [A Guide To The Codebase](#codebase)
  - [Model](#model)
  - [View](#view)
  - [Controller](#controller)
- [Developers](#devs)


## Installing
Clone the project from GitHub: `git clone https://github.com/mucsci-students/2025sp-420-CrowdStrike.git`

Cd into the directory: `cd 2025sp-420-CrowdStrike`

## Running
First you must download [junit](https://repo1.maven.org/maven2/org/junit/platform/junit-platform-console-standalone/1.11.4/junit-platform-console-standalone-1.11.4.jar) and [gson](https://repo1.maven.org/maven2/com/google/code/gson/gson/2.12.1/gson-2.12.1.jar) place them in the lib directory.

[junit download](https://repo1.maven.org/maven2/org/junit/platform/junit-platform-console-standalone/1.11.4/junit-platform-console-standalone-1.11.4.jar)

[gson download](https://repo1.maven.org/maven2/com/google/code/gson/gson/2.12.1/gson-2.12.1.jar)
### Tests
#### Linux/Macos
```sh
javac -d out/ -cp lib/gson-2.12.1.jar:lib/junit-platform-console-standalone-1.11.4.jar src/*.java
javac -d out/ -cp lib/gson-2.12.1.jar:lib/junit-platform-console-standalone-1.11.4.jar:out test/*.java
java -jar lib/junit-platform-console-standalone-1.11.4.jar execute -cp lib/gson-2.12.1.jar:lib/junit-platform-console-standalone-1.11.4.jar:out --scan-class-path
```

#### Windows
```sh
javac -d out/ -cp lib/gson-2.12.1.jar;lib/junit-platform-console-standalone-1.11.4.jar src/*.java
javac -d out/ -cp lib/gson-2.12.1.jar;lib/junit-platform-console-standalone-1.11.4.jar;out test/*.java
java -jar lib/junit-platform-console-standalone-1.11.4.jar execute -cp lib/gson-2.12.1.jar;lib/junit-platform-console-standalone-1.11.4.jar;out --scan-class-path
```

### Project
#### Linux/Macos
```sh
javac -d out/ -cp lib/gson-2.12.1.jar:lib/junit-platform-console-standalone-1.11.4.jar src/*.java
jar cfe uml.jar UMLClassEditorApp -C out/ .
java -jar uml.jar
```

#### Windows
```sh
javac -d out/ -cp lib/gson-2.12.1.jar:lib/junit-platform-console-standalone-1.11.4.jar src/*.java
jar cfe uml.jar UMLClassEditorApp -C out/ .
java -jar uml.jar
```

## Codebase
We followed the Model/View/Controller (MVC) paradigm to design easy-to-follow code.

### Model
The model holds the data of a data driven application, which in this case is an instance of a UML Class Diagram.
The main file in this folder is `UMLModel.java` which contains user-created instantiations of the objects defined in `ClassObject.java`, `Attribute.java`, and `Relationship.java`.

### View
The view gets data from the model and displays it to the user. We have implemented a Command Line Interface through the class `CLView.java` that can show the user a textual description of their UML diagram.

### Controller
The controller gets the input from the user and changes the state in the model/view accordingly. In our setup, we have a controller that edits the data stored in the model called `UMLEditor.java` and a controller for our view called `CLController.java` that accepts text commands from the user and updates the command line interface.

## Devs
Brought to you by:  

dpbrooks - Dylan Brooks  

FinicalMedusa - Michael Bennett  

N-ckM - Nick Mills  

donut0711 - Mark Hilton  

DmurZel - Darlin Piruch

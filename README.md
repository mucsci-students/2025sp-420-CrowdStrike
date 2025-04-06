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
  - [Design Patterns](#Design_Patterns)
- [Developers](#devs)


## Installing
Clone the project from GitHub: `git clone https://github.com/mucsci-students/2025sp-420-CrowdStrike.git`

Cd into the directory: `cd 2025sp-420-CrowdStrike`

## Build
This will generate a distribution file in app/build/distributions/app.zip
#### Linux/Macos
```sh
./gradlew build
```
### Windows
```sh
.\gradlew.bat build
```


## Running
## distribution file
Unpack the distribution file located in app/build/distributions.
There will be both a tar an a zip file (unzip or tar -xf) depending on which version you want to unpack.
## Linux/Macos
```sh
./bin/app
```
## Window
```sh
.\bin\app.bat
```

### Tests
#### Linux/Macos
```sh
./gradlew test
```

#### Windows
```sh
.\gradlew.bat test
```

### Project (develop)
#### Linux/Macos
#### GUI
```sh
./gradlew run
```
#### CLI
```sh
./gradlew run-cli
```

#### Windows
#### GUI
```sh
.\gradlew.bat run
```
#### CLI
```sh
.\gradlew.bat run-cli
```
## Usage
 Below is our available commands in command line 
==============   CLASS   =============
ADD CLASS - Adds a new class with a unique name.
        type -> 'add class' or 'ac'

DELETE CLASS - Deletes an existing class by name.
        type -> 'delete class' or 'dc'

RENAME CLASS - Renames an existing class.
        type -> 'rename class' or 'rc'

LIST CLASS - Returns given class.
        type -> 'list class' or 'lc'

LIST CLASSES - Returns all classes.
        type -> 'list classes' or 'lcs'

==========   RELATIONSHIP   ===========
ADD RELATIONSHIP - Creates a relationship between two classes.
        type -> 'add relationship' or 'ar'

DELETE RELATIONSHIP - Removes an existing relationship between two classes.
        type -> 'delete relationship' or 'dr'

EDIT RELATIONSHIP - Allows user to edit a designated field of a relationship
        type -> 'edit relationship' or 'er'

LIST RELATIONSHIPS - Displays all relationships involving a specific class.
        type -> 'list relationships' or 'lr'

==========   Fields   ==========
ADD FIELD - Adds a new field to a class.
        type -> 'add field' or 'af'
    
DELETE FIELD - Removes a field from a class.
        type -> 'delete field' or 'df'
    
RENAME FIELD - Renames an existing field in a class.
        type -> 'rename field' or 'rf'
    
==========   Methods   ==========
ADD METHOD - Adds a new method to a class.
        type -> 'add method' or 'am'
    
DELETE METHOD - Removes a method from a class.
        type -> 'delete method' or 'dm'

RENAME METHOD - Renames an existing method in a class.
        type -> 'rename method' or 'rm'

==========   Parameters   ==========
ADD PARAMETER - Adds one or more parameters to a method
        type -> 'add parameter' or 'ap'

DELETE PARAMETER - Deletes one or all parameters from a method
        type -> 'remove parameter' or 'rp'

CHANGE PARAMETER - Changes one parameter or all parameters to a new set of parameters
        type -> 'change parameter' or 'cp'

==========   SAVE/LOAD   =============
SAVE - Saves the current state of the project.
        type -> save

LOAD - Loads a previously saved project.
        type -> load

==========   EXIT   =============
EXIT - Exits the program
        type -> 'exit' or 'q'
## Codebase
We followed the Model/View/Controller (MVC) paradigm to design easy-to-follow code.

### Model
The model holds the data of a data driven application, which in this case is an instance of a UML Class Diagram.
The main file in this folder is `UMLModel.java` which contains user-created instantiations of the objects defined in `ClassObject.java`, `Method.java`, `Parameter.java`,`Field.java`, and `Relationship.java`.

### View
The view gets data from the model and displays it to the user. 
We have implemented a Command Line Interface through the class `CLView.java` that can show the user a textual description of their UML diagram.
We also implemented a GUI through the class `GUIView.java` to show the use a graphical repersentation of their UML diagram.
We finally have a helper to the `GUIView.java` called `ClassBox.java` this file reperents the classes in the gui.

### Controller
The controller gets the input from the user and changes the state in the model/view accordingly. 
In our setup, we have a controller that edits the data stored in the model called `UMLEditor.java`.
We have a controller for our cli view called `CLController.java` that accepts text commands from the user and updates the command line interface.
We also have a controller for our gui view call `GUIController.java` that accepts commands from the user and updates the model and view.

## Design_Patterns
### 1. Memento
 Seen in the undo/redo the UMLMemento file to implement undo/redo by saving state on a stack.
### 2. Iterator
 Seen in the CLController as for:each loops to iterate through various lists. 
### 3. Decorator
 Seen in the CLI as we did not change the base functionality of calling commands, but rather added a command feature that allows users to tab complete.
### 4. Composite
 Seen in the classbox as we take in a class object and reference it later.
## Devs
Brought to you by:  

dpbrooks - Dylan Brooks  

FinicalMedusa - Michael Bennett  

N-ckM - Nick Mills  

donut0711 - Mark Hilton  

DmurZel - Darlin Piruch

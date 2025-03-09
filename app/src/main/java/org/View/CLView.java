package org.View;
/**
 * CLView.java
 * 
 * Displays the UML diagram to the user via a command line interface
 */

public class CLView {
    
    //Displays prompt
    public void show(String prompt) {
        System.out.println(prompt);
    }

    //this method displays showHelp after using show() to print getInstructions()
    public void showHelp() {
        show(getInstructions());
    }

    /* 
    // Provides a detailed help guide for class management commands.
    public static String getInstructions() {
                    return """
        ==============   HELP   ==============

        ==============   CLASS   =============
            ADD CLASS - Adds a new class with a unique name.

                type -> add class
                Enter the new class' name:
                [NAME]

            DELETE CLASS - Deletes an existing class by name.

                type -> delete class
                Which class would you like to delete?
                [NAME]

            RENAME CLASS - Renames an existing class.
                type -> rename class
                Which class would you like to rename?
                [OLD NAME]
                What would you like the new name to be?
                [NEW NAME]

            LIST CLASS - Returns given class.
                type -> list class
                What class would you like printed?
                [CLASS]

            LIST CLASSES - Returns all classes.
                type -> list classes

        ==========   RELATIONSHIP   ===========
            ADD RELATIONSHIP - Creates a relationship between two classes.
                type -> add relationship
                Would you like to assign a name to this relationship? (Y for yes)
                [NAME (optional)]
                Enter the source class name:
                [SOURCE CLASS]
                Enter the destination class name:
                [DESTINATION CLASS]

            DELETE RELATIONSHIP - Removes an existing relationship between two classes.
                type -> delete relationship
                Enter the source class name of the relationship:
                [SOURCE CLASS]
                Enter the destination class name of the relationship:
                [DESTINATION CLASS]

            LIST RELATIONSHIPS - Displays all relationships involving a specific class.
                type -> list relationships
                Enter the class name to list its relationships:
                [CLASS]

        ==========   ATTRIBUTE   =============
            ADD ATTRIBUTE - Adds a new attribute to a class.
                type -> add attribute
                Which class would you like to add an attribute to?
                [CLASS]
                What name would you like to give the attribute?
                [ATTRIBUTE]

            DELETE ATTRIBUTE - Removes an attribute from a class.
                type -> delete attribute
                Which class would you like to delete an attribute from?
                [CLASS]
                What is the name of the attribute you want to delete?
                [ATTRIBUTE]

            RENAME ATTRIBUTE - Renames an existing attribute in a class.
                type -> rename attribute
                Which class has the attribute you want to rename?
                [CLASS]
                What is the name of the attribute you want to rename?
                [OLD ATTRIBUTE]
                What would you like to rename the attribute to?
                [NEW ATTRIBUTE]

        ==========   SAVE/LOAD   =============
                SAVE - Saves the current state of the project.
                    type -> save

                LOAD - Loads a previously saved project.
                    type -> load
                        """;
    }
    */

    // Provides a detailed help guide for class management commands.
    public static String getInstructions() {
        return """
        ==============   HELP   ==============

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
            """;
}    
    
}

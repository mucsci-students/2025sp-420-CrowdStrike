package org.Model;

import java.util.ArrayList;

public interface UMLModelInterface {

    /**
     * Returns the classList
     *
     * @return list of created classes
     */
    public ArrayList<ClassObject> getClassList();

    /**
     * Returns the relationshipList
     *
     * @return list of created relationships
     */
    public ArrayList<Relationship> getRelationshipList();

    /**
     * Returns the length of the longest relationship name
     *
     * @return relationshipLength
     */
    public ClassObject fetchClass(String className) throws Exception;

    /**
     * Checks if a relationships exists
     *
     * @param source | The source of the relationship
     * @param dest | Destination of the relationship
     * @return Relationship object if it exists, null otherwise
     */
    public boolean relationshipExist(String source, String dest);

    /**
     * Creates a String listing classes and returns it
     *
     * @return String listing all classes and their info
     */
    public String listClasses();

    /**
     * List the info of a specified class
     *
     * @param cls | The class to be printed
     * @return A String containing the list of specified class' info or empty string if class does
     *     not exist
     */
    public String listClassInfo(ClassObject cls);

    /**
     * Lists all created relationships
     *
     * @return A string containing a list of all relationships
     */
    public String listRelationships() throws Exception;

    /**
     * Creates a list of all created class names that user can reference
     *
     * @return List of class names
     */
    public String listClassNames() throws Exception;

    /**
     * Creates a list of all created fields in the given class
     *
     * @param cls | Class whose fields are being listed
     * @return A string of all fields in the class
     */
    public String listFields(ClassObject cls) throws Exception;

    /**
     * Creates a list of all created methods in the given class
     *
     * @param cls | Class whose methods are being listed
     * @return A string containing all methods in the class
     */
    public String listMethods(ClassObject cls) throws Exception;
}

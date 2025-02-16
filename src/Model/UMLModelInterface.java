import java.util.ArrayList;

public interface UMLModelInterface {

/**
     * Returns the classList
     * @return list of created classes
     */
public ArrayList<ClassObject> getClassList();

/**
     * Returns the relationshipList
     * @return list of created relationships
     */
public ArrayList<Relationship> getRelationshipList();

/**
     * Returns the length of the longest relationship name
     * @return relationshipLength
     */
public int getRelationshipLength();

/**
     * Updates relationshipLength
     * @param newLen Length of new longest relationship name
     */
public void setRelationshipLength(int newLen);

/**
     * Gets a ClassObject by the given name
     * @param className The name of the class to return
     * @return ClassObject with specified name if it exists
     *    returns null if class does not exist
     */
public ClassObject fetchClass(String className);

/**
     * Checks if a relationships exists
     * @param source The source of the relationship
     * @param dest Destination of the relationship
     * @return True if relationship exists, false otherwise
     */
public Relationship relationshipExist(String source, String dest);

/**
     * Iterates through relationships to find the longest name
     * Used when deleting relationships
     */
public void updateLongest();

/**
     * Creates a String listing classes and returns it
     * @return String listing all classes and their info
     */
public String listClasses();

/**
 * List the info of a specified class
 * @param className The name of the class to be printed
 * @return A String containing the list of specified class' info or 
 *    empty string if class does not exist
 */
public String listClassInfo(String className);

/**
 * Lists all created relationships
 * @return A string containing a list of all relationships
 */
public String listRelationships();

/**
     * Creates a list of all created class names that user can reference
     * @return List of class names
     */
public String listClassNames();

/**
     * Creates a list of all created attributes in a given class
     * @param className Class whose attributes are being listed
     * @return A String of all attributes in the class
     */
public String listAttributes(String className);
}

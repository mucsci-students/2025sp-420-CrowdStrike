//relationship.java

//Class to represent a single relationship object
/* 
*/


public class Relationship{
    // The source and destination classes involved in this relationship
    private final ClassObject SOURCE;
    private final ClassObject DESTINATION;

    // Optionally, the user can choose to store the name of the relationship
    private String name;

    // Each relationship should have a unique ID that is assigned in our UMLModel class
    private final int ID;

    /**
     * Constructor for an unnamed relationship
     * Creates a relationship between a source class and destination class
     * @param ClassObject source    |   the source of the relationship
     * @param ClassObject dest      |   the destination of this relationship
     */
    public Relationship(ClassObject source, ClassObject dest){
        this.SOURCE = source;
        this.DESTINATION = dest;
        this.name = "";
    }

    /**
     * Constructor for a named relationship
     * Creates a relationship between a source class and destination class, and names it
     * @param ClassObject source    |   the source of the relationship
     * @param ClassObject dest      |   the destination of this relationship
     * @param ClassObject name      |   the name given to this relationship
     */
    public Relationship(ClassObject source, ClassObject dest, String relationshipName){
        this.SOURCE = source;
        this.DESTINATION = dest;
        this.name = relationshipName;
    }


    // Accssor methods

    /**
     * Relationship.getSource();
     * Gets the source class of this relationship
     * @return ClassObject SOURCE
     */
    public ClassObject getSource()
        {return this.SOURCE;}
    
    /**
     * Relationship.getDestination();
     * Gets the destination class of this relationship
     * @return ClassObject DESTINATION
     */
    public ClassObject getDestination()
        {return this.DESTINATION;}
    
    /**
     * Relationship.getName();
     * Returns the name of this relationship (could be empty)
     * @return String name
     */
    public ClassObject getName()
        {return this.name;}

    // Mutator methods
    /**
     * Relationship.setName(newName);
     * Sets this relationship's name to newName
     * @param String name
     */
    public void setName(String newName)
        {this.name = newName;}
}
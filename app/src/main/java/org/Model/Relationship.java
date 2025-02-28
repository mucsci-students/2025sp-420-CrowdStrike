package org.Model;
//relationship.java

//Class to represent a single relationship object
/* 
*/


public class Relationship{
    // The source and destination classes involved in this relationship
    private ClassObject source;
    private ClassObject destination;

    // Optionally, the user can choose to store the name of the relationship
    private String name;

    // Each relationship should have a unique ID that is assigned in our UMLModel class
    private final int ID;

    // Each relationship must be associated with one of four predetermined types
    public enum Type {AGGREGATION, COMPOSITION, INHERITANCE, REALIZATION};
    private Type type;

    /**
     * Constructor for an unnamed relationship
     * Creates a relationship between a source class and destination class
     * @param ClassObject source    |   the source of the relationship
     * @param ClassObject dest      |   the destination of this relationship
     * @param Type newType          |   the type of this new relationship
     */
    public Relationship(ClassObject source, ClassObject dest, Type newType){
        this.source = source;
        this.destination = dest;
        this.name = "";
        this.ID = source.hashCode() + dest.hashCode();
        this.type = newType;
    }

    /**
     * Constructor for a named relationship
     * Creates a relationship between a source class and destination class, and names it
     * @param String name           |   the name given to this relationship
     * @param ClassObject source    |   the source of the relationship
     * @param ClassObject dest      |   the destination of this relationship
     * @param Type type             |   the type of this relationship
     */
    public Relationship(String relationshipName, ClassObject source, ClassObject dest, Type newType){
        this.source = source;
        this.destination = dest;
        this.name = relationshipName;
        this.ID = source.hashCode() + dest.hashCode();
        this.type = newType;
    }


    // Accssor methods

    /**
     * Relationship.getSource();
     * Gets the current source class of this relationship
     * @return ClassObject source
     */
    public ClassObject getSource()
        {return this.source;}
    
    /**
     * Relationship.getDestination();
     * Gets the current destination class of this relationship
     * @return ClassObject destination
     */
    public ClassObject getDestination()
        {return this.destination;}
    
    /**
     * Relationship.getName();
     * Returns the name of this relationship (could be empty)
     * @return String name
     */
    public String getName()
        {return this.name;}

    /**
     * Relationship.getID();
     * Returns the ID of this relationship based on the hash of its source and destination
     * @return int
     */
    public int getID()
        {return this.ID;}

    /**
     * Relationship.getType();
     * Gets the type of this relationship
     * @return Type type
     */
    public Type getType()
        {return this.type;}
    
    /**
     * Relationship.getType();
     * Gets the type of this relationship as a string 
     * @return String representation of type
     */
    public String getTypeString(){
        switch (this.type) {
            case AGGREGATION: return "Aggregation"; break;
            case COMPOSITION: return "Composition"; break;
            case INHERITANCE: return "Inheritance"; break;
            case REALIZATION: return "Realization"; break;
        }
    }

    // Mutator methods
    /**
     * Relationship.setName(newName);
     * Sets this relationship's name to newName
     * @param String name
     */
    public void setName(String newName)
        {this.name = newName;}
    
    /**
     * Relationship.setSource(newSource);
     * Changes this relationship's source to a ClassObject newSource
     * @param ClassObject newSource
     */
    public void setSource(ClassObject newSource)
        {this.source = newSource;}
    
    /**
     * Relationship.setDestination(newDest);
     * Changes this relationship's destination to a ClassObject newDest
     * @param ClassObject newDest
     */
    public void setDestination(ClassObject newDest)
        {this.destination = newDest;}

    /**
     * Relationship.setType(newType);
     * Sets this relationship's type to newType
     * @param Type newType
     */
    public void setType(Type newType)
        {this.type = newType;}

}

package org.Model;

// RelationshipInterface.java
// 

public interface RelationshipInterface {
    
    // Enum representing the types of relationships
    enum Type {AGGREGATION, COMPOSITION, INHERITANCE, REALIZATION};
    
    // Accessor methods
    ClassObject getSource();
    ClassObject getDestination();
    
    /**
     * Relationship.getName();
     * Returns the name of this relationship (could be empty)
     * @return String name
     */
    String getName();
    
    /**
     * Relationship.getID();
     * Returns the ID of this relationship based on the hash of its source and destination
     * @return int ID of relationship
     */
    int getID();
    
    /**
     * Relationship.getType();
     * Gets the type of this relationship
     * @return Type type
     */
    Type getType();

    /**
     * Relationship.getTypeString();
     * Gets the type of this relationship as a String object
     * @return String representation of type
     */
    String getTypeString();
    
    // Mutator methods
    /**
     * Relationship.setName(newName);
     * Sets this relationship's name to newName
     * @param String name
     */
    void setName(String newName);

    /**
     * Relationship.setSource(newSource);
     * Changes this relationship's source to a ClassObject newSource
     * @param ClassObject newSource
     */
    void setSource(ClassObject newSource);
    
    /**
     * Relationship.setDestination(newDest);
     * Changes this relationship's destination to a ClassObject newDest
     * @param ClassObject newDest
     */
    void setDestination(ClassObject newDest);
    
    /**
     * Relationship.setType(newType);
     * Sets this relationship's type to newType
     * @param Type newType
     */
    void setType(Type newType);
    
    // Comparison method
    /**
     * Compares this relationship to any other java object 
     * @param Object other
     * @return boolean denoting equivalency
     */
    //boolean equals(Object other);
}
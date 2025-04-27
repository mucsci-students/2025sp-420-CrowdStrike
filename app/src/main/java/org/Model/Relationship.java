package org.Model;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

// Creates a relationship object
public class Relationship{
    // The source and destination classes involved in this relationship
    private ClassObject source;
    private ClassObject destination;

    // Each relationship should have a unique ID that is assigned in our UMLModel class
    private final int ID;

    // Each relationship must be associated with one of four predetermined types
    public enum Type {AGGREGATION, COMPOSITION, INHERITANCE, REALIZATION};
    private Type type;

    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);


    /**
     * Constructor for a relationship
     * Creates a relationship between a source class and destination class
     * @param ClassObject source    |   the source of the relationship
     * @param ClassObject dest      |   the destination of this relationship
     * @param Type type             |   the type of this relationship
     */
    public Relationship(ClassObject source, ClassObject dest, Type newType){
        this.source = source;
        this.destination = dest;
        this.ID = source.hashCode() + dest.hashCode();
        this.type = newType;
    }

        /**
     * Constructor for an unnamed, untyped relationship to not break tests
     * Creates a relationship between a source class and destination class
     * @param ClassObject source    |   the source of the relationship
     * @param ClassObject dest      |   the destination of this relationship
     * @param Type newType          |   the type of this new relationship
     */
    public Relationship(ClassObject source, ClassObject dest){
        this.source = source;
        this.destination = dest;
        this.ID = source.hashCode() + dest.hashCode();
        this.type = Type.REALIZATION;
    }

    /**
     * Construct a new relationship from an existing
     *
     * @param r the relationship to clone.
     */
    public Relationship(Relationship r){
	source = r.source;
	destination = r.destination;
	ID = r.ID;
	type = r.type;
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
            case AGGREGATION: return "Aggregation"; 
            case COMPOSITION: return "Composition"; 
            case INHERITANCE: return "Inheritance"; 
            default: return "Realization"; 
        }
    }

    // Mutator methods
    
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

    	/**
	 * Subscribe to changes to the model made by the edditor.
	 *
	 * @param listener the object trying to subscribe
	 */
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		pcs.addPropertyChangeListener(listener);
	}

    @Override
    public String toString(){
	String s = "";
	s += source.toString();
	s += "--";
	s += getTypeString();
	s += "->";
	s += destination.toString();
	return s;
    }

}

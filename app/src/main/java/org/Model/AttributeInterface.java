package org.Model;

public interface AttributeInterface {

    /**
     * Returns an attribute's name
     *
     * @return String name
     */
    public String getName();

    /**
     * Returns the attribute's type
     *
     * @return "Field" if the attribute is a field "Method" if it's a method
     */
    public String getType();

    /**
     * Changes an attribute's name
     *
     * @param newName | Name the attribute's name should be changed to
     */
    public void renameAttribute(String newName);
}

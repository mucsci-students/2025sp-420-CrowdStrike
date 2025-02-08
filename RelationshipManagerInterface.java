interface RelationshipManagerInterface {
    /**
     * Adds a relationship between two classes.
     *
     * @param sourceClass The class the new relationship will come from
     * @param destinationClass The class the new relationship will go to
     */
    String addRelationship(String sourceClass, String destinationClass);

    /**
     * Deletes a relationship by its unique ID.
     *
     * @param relationshipId The ID of the indended relationship
     */
    void deleteRelationshipById(int relationshipId);

    /**
     * Deletes a relationship between two classes.
     *
     * @param sourceClass The class that the indended relationship is from
     * @param destinationClass The class that the indended relationship is to
     */
    void deleteRelationship(String sourceClass, String destinationClass);

    /**
     * Lists all relationships between classes.
     *
     * @param filters A list of class names that relationships will be shown for.
     */
    void listRelationships(String... filters);

    /**
     * Returns a json segment that can be used to recreate this object.
     *
     * @return Json containing all nessisary state to recreate current state.
     */
    public String toJson();
}

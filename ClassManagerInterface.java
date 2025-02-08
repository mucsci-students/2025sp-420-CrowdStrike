interface ClassManagerInterface {
    /**
     * Adds a new class.
     *
     * @param className The name of a curently not existing class
     */
    public void addClass(String className);

    /**
     * Deletes an existing class by name.
     *
     * @param className The name of the class to delete.
     */
    public void deleteClass(String className);

    /**
     * Renames an existing class.
     *
     * @param oldName The name of an existing class.
     * @param newName The new name of the given oldName.
     */
    public void renameClass(String oldName, String newName);

    /**
     * Returns a json segment that can be used to recreate this object.
     *
     * @return Json containing all nessisary state to recreate current state.
     */
    public String toJson();
}

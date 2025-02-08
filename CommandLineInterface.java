public interface CommandLineInterface {
    /**
     * Displays all classes and their contents.
     */
    void listClasses();

    /**
     * Displays details of a specified class.
     *
     * @param className The name of the class to display details for.
     */
    void listClass(String className);

    /**
     * Displays all relationships between classes.
     *
     * @param filters The classes that will have there relationships displayed.
     */
    void listRelationships(String... filters);

    /**
     * Displays help information for the application.
     */
    void displayHelp();

    /**
     * Exits the application.
     */
    void exitApplication();
}

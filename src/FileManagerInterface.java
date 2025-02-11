import java.io.IOException;

interface FileManagerInterface {
	/**
	 * Take a current state and save it to a file.
	 *
	 * @param path          The path to save to.
	 * @param classes       The current classes.
	 * @param relationships The current relationships.
	 */
	public void save(String path, RelationshipManagerInterface relationships, ClassManagerInterface classes)
			throws IOException;

	/**
	 * Take a path to json and return the state from that file.
	 *
	 * @return An array of 2 objects [0] is the RelatonshipManager, [1] is the
	 *         ClassManager.
	 */
	public Object[] load(String path) throws IOException;
}

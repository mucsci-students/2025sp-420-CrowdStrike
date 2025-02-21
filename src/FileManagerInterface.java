import java.io.IOException;

interface FileManagerInterface {
	/**
	 * Take a current state and save it to a file.
	 *
	 * @param path  The path to save to.
	 * @param state The model to save to the path.
	 */
	public void save(String path, UMLModel model) throws Exception;

	/**
	 * Take a path to json and return the state from that file.
	 *
	 * @return A model object holding the loaded state.
	 */
	public UMLModel load(String path) throws Exception;
}

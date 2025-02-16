import java.io.File;
import java.io.IOException;

public class FileManager implements FileManagerInterface {
	public void save(String path, UMLModelInterface model) throws IOException {
		File jsonFile = new File(path);
		if (!jsonFile.canWrite())
			throw new IOException("Can not write to given path.");
		// TODO
	}

	public UMLModelInterface load(String path) throws IOException {
		File jsonFile = new File(path);
		if (!jsonFile.canRead())
			throw new IOException("Can not read given path.");
		// TODO
		return new UMLModel();
	}
}

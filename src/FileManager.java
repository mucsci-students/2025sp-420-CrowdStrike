import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class FileManager implements FileManagerInterface {
	public void save(String path, modelInterface state) throws IOException {
		File jsonFile = new File(path);
		if (!jsonFile.canWrite())
			throw new IOException("Can not write to given path.");
		// TODO
	}

	public modelInterface load(String path) throws IOException {
		File jsonFile = new File(path);
		if (!jsonFile.canRead())
			throw new IOException("Can not read given path.");
		// TODO
		return new model();
	}
}

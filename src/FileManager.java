import java.io.File;
import java.io.IOException;

class FileManager implements FileManagerInterface {
	public void save(String path, RelationshipManagerInterface relationships, ClassManagerInterface classes)
			throws IOException {
		File jsonFile = new File(path);
		if (!jsonFile.canWrite())
			throw new IOException("Can not write to given path.");

		// TODO
	}

	public Object[] load(String path) throws IOException {
		File jsonFile = new File(path);
		if (!jsonFile.canRead())
			throw new IOException("Can not read given path.");

		// TODO
		Object[] ret = {};
		return ret;
	}
}

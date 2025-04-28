package org;

import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;

import org.Model.UMLModel;

public class FileManager {

	public void save(UMLToJsonAdapter adapter, UMLModel model, String path) throws Exception{
		adapter.save(path, model);
	}

	public UMLModel load (UMLToJsonAdapter adapter, String path) throws Exception{
		if (!Files.isReadable(Paths.get(path)))
			throw new InvalidPathException(path, "given path is not valid");
		return adapter.load(path);
	}
}

	
import com.google.gson.Gson;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;

public class FileManager implements FileManagerInterface {

	public void save(String path, UMLModel model) throws Exception {
		String json;
		Gson gson = new Gson();
		FileWriter writer = new FileWriter(path);

		json = gson.toJson(model);
		writer.write(json);
		writer.close();

	}

	public UMLModel load(String path) throws Exception {
		Gson gson = new Gson();
		String content = Files.readString(Paths.get(path));
		UMLModel model = gson.fromJson(content, UMLModel.class);
		return model;
	}
}

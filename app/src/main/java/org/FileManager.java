package org;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.FileWriter;

import java.util.function.Function;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.Model.UMLModel;


public class FileManager {

	public void save(String path, UMLModel model) throws Exception {
		String json = "";
		FileWriter writer = new FileWriter(path);

		json += "{";
		json += jsonCommas("\"classes\": [", "],", model.getClassList(), this::classToJson);
		json += jsonCommas("\"relationships\": [", "]", model.getRelationshipList(), this::relationshipToJson);
		json += "}";

		/* use gson to format */
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		JsonElement jsonElement = JsonParser.parseString(json);
		json = gson.toJson(jsonElement);
		/**/

		writer.write(json);
		writer.close();

	}

	public UMLModel load(String path) throws Exception {
		Gson gson = new Gson();
		String content = Files.readString(Paths.get(path));
		UMLModel model = gson.fromJson(content, UMLModel.class);
		return model;
	}

	private <T> String jsonCommas(String begin, String end, ArrayList<T> data, Function<T, String> convert) {
		String ret = begin;
		boolean first = true;
		for (T obj : data) {
			if (!first)
				ret += ",";
			first = false;
			ret += convert.apply(obj);
		}
		ret += end;
		return ret;
	}

	private String classToJson(ClassObject c) {
		String ret = "{\"name\": \"" + c.getName() + "\",";
		ret += jsonCommas("\"fields\": [", "],", c.getAttrList(), this::attributeToJson);// TODO replace with
													// fields
		ret += jsonCommas("\"methods\": [", "]", c.getAttrList(), this::attributeToJson);// TODO replace with
													// methods
		ret += "}";
		return ret;
	}

	private String attributeToJson(Attribute a) { // TODO replace with fieds and methods
		return String.format("{\"name\": \"%s\"}", a.getName());
	}

	private String relationshipToJson(Relationship r) {
		return String.format("{\"source\": \"%s\",\"destination\": \"%s\",\"type\":\"%s\"}",
				r.getSource().getName(),
				r.getDestination().getName(),
				r.getName());
	}
}

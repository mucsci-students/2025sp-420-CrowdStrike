package org;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.FileWriter;

import java.util.function.Function;
import java.util.ArrayList;
import java.lang.StringBuilder;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.Model.UMLModel;
import org.Model.ClassObject;
import org.Model.Method;
import org.Model.Parameter;
import org.Model.AttributeInterface;
import org.Model.Relationship;

public class FileManager {

	/**
	 * Saves a model as json to a given path.
	 *
	 * @param path  The path to save the model to. Needs to end in a <filename>.json
	 * @param model The model to be saved.
	 */
	public void save(String path, UMLModel model) throws Exception {
		StringBuilder json = new StringBuilder();
		FileWriter writer = new FileWriter(path);

		json.append("{");
		json.append(jsonCommas("\"classes\": [", "],", model.getClassList(), this::classToJson));
		json.append(jsonCommas("\"relationships\": [", "]", model.getRelationshipList(),
				this::relationshipToJson));
		json.append("}");

		/* use gson to format and verify */
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		JsonElement jsonElement = JsonParser.parseString(json.toString());
		String jsonstr = gson.toJson(jsonElement);
		/**/

		writer.write(jsonstr);
		writer.close();

	}

	public UMLModel load(String path) throws Exception {
		Gson gson = new Gson();
		String content = Files.readString(Paths.get(path));
		UMLModel model = gson.fromJson(content, UMLModel.class);
		return model;
	}

	private <T> String jsonCommas(String begin, String end, ArrayList<T> data, Function<T, String> convert) {
		StringBuilder ret = new StringBuilder(begin);
		boolean first = true;
		for (T obj : data) {
			if (!first)
				ret.append(",");
			first = false;
			ret.append(convert.apply(obj));
		}
		ret.append(end);
		return ret.toString();
	}

	private String classToJson(ClassObject classobj) {
		StringBuilder ret = new StringBuilder("{\"name\": \"" + classobj.getName() + "\",");
		ret.append(jsonCommas("\"fields\": [", "],", classobj.getFieldList(), this::FieldsToJson));
		ret.append(jsonCommas("\"methods\": [", "]", classobj.getMethodList(), this::MethodsToJson));
		ret.append("}");
		return ret.toString();
	}

	private String FieldsToJson(AttributeInterface field) {
		return String.format("{ \"name\": \"%s\"}", field.getName());
	}

	private String MethodsToJson(AttributeInterface methodInterface) {
		StringBuilder ret = new StringBuilder("{\"name\": \"" + methodInterface.getName() + "\",");
		Method method = (Method) methodInterface;
		ret.append(jsonCommas("\"params\": [", "]", method.getParamList(), this::ParamToJson));
		ret.append("}");
		return ret.toString();
	}

	private String ParamToJson(Parameter param) {
		return param.getName();
	}

	private String relationshipToJson(Relationship relationship) {
		return String.format("{\"source\": \"%s\",\"destination\": \"%s\",\"type\":\"%s\"}",
				relationship.getSource().getName(),
				relationship.getDestination().getName(),
				relationship.getTypeString());
	}
}

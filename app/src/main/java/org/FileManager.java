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
import org.Model.Field;
import org.Model.Method;
import org.Model.Parameter;
import org.Model.AttributeInterface;
import org.Model.Relationship;

public class FileManager {

	public void save(String path, UMLModel model) throws Exception {
		StringBuilder json = new StringBuilder();
		FileWriter writer = new FileWriter(path);

		json.append("{");
		json.append(jsonCommas("\"classes\": [", "],", model.getClassList(), this::classToJson));
		json.append(jsonCommas("\"relationships\": [", "]", model.getRelationshipList(),
				this::relationshipToJson));
		json.append("}");

		/* use gson to format */
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

	private String classToJson(ClassObject c) {
		StringBuilder ret = new StringBuilder("{\"name\": \"" + c.getName() + "\",");
		ret.append(jsonCommas("\"fields\": [", "],", c.getFieldList(), this::FieldsToJson));
		ret.append(jsonCommas("\"methods\": [", "]", c.getMethodList(), this::MethodsToJson));
		ret.append("}");
		return ret.toString();
	}

	private String FieldsToJson(AttributeInterface f) {
		return String.format("{ \"name\": \"%s\"}", f.getName());
	}

	private String MethodsToJson(AttributeInterface i) {
		StringBuilder ret = new StringBuilder("{\"name\": \"" + i.getName() + "\",");
		Method m = (Method) i;
		ret.append(jsonCommas("\"params\": [", "]", m.getParamList(), this::ParamToJson));
		ret.append("}");
		return ret.toString();
	}

	private String ParamToJson(Parameter p) {
		return p.getName();
	}

	private String relationshipToJson(Relationship r) {
		return String.format("{\"source\": \"%s\",\"destination\": \"%s\",\"type\":\"%s\"}",
				r.getSource().getName(),
				r.getDestination().getName(),
				r.getTypeString());
	}
}

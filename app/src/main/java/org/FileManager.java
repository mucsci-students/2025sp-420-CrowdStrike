package org;

import com.google.gson.Gson;

import com.google.gson.JsonObject;
import com.google.gson.JsonElement;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

import java.io.FileWriter;
import java.io.InvalidObjectException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;

import java.util.ArrayList;

import org.Model.UMLModel;
import org.Model.AttributeInterface;
import org.Model.ClassObject;
import org.Model.Field;
import org.Model.Method;
import org.Model.Parameter;
import org.Model.Relationship.Type;

import org.Controller.UMLEditor;

public class FileManager {

	public void save(String path, UMLModel model) throws Exception {
		String json;
		Gson gson = new Gson();
		FileWriter writer = new FileWriter(path);

		json = gson.toJson(model);
		writer.write(json);
		writer.close();
	}

	/**
	 * Loads a UMLModel from Json
	 *
	 * @param path The path to the json.
	 * @return A new UMLModel containing the state from the json.
	 */
	public UMLModel load(String path) throws Exception {
		UMLModel umlModel = new UMLModel();
		ArrayList<ClassObject> classList = umlModel.getClassList();

		if (!Files.isReadable(Paths.get(path)))
			throw new InvalidPathException(path, "given path is not valid");

		String content = Files.readString(Paths.get(path));
		JsonElement modelJson = JsonParser.parseString(content);
		JsonObject modelObject;

		if (!modelJson.isJsonObject())
			throw new InvalidObjectException("given json is invalid");

		modelObject = modelJson.getAsJsonObject();
		if (!modelObject.has("classes"))
			throw new InvalidObjectException("Missing classes in json");
		if (!modelObject.has("relationships"))
			throw new InvalidObjectException("Missing relationships in json");

		for (JsonElement e : modelObject.get("classes").getAsJsonArray()) {
			classList.add(buildClass(e.getAsJsonObject()));
		}

		for (JsonElement e : modelObject.get("relationships").getAsJsonArray()) {
			addRelationships(e, umlModel);
		}

		return umlModel;
	}

	private void addRelationships(JsonElement json, UMLModel model) throws Exception {
		UMLEditor e = new UMLEditor(model);
		JsonObject relationshipObject;
		if (!json.isJsonObject())
			throw new InvalidObjectException("relationship is not an object");

		relationshipObject = json.getAsJsonObject();
		if (!(relationshipObject.has("source") && relationshipObject.has("destination")
				&& relationshipObject.has("type")))
			throw new InvalidObjectException("relationship is missing a field");

		Type relationshipType = null;/* if not changed will crash the cli */
		switch (relationshipObject.get("type").getAsString()) {
			case "Aggregation":
				relationshipType = Type.AGGREGATION;
				break;
			case "Composition":
				relationshipType = Type.COMPOSITION;
				break;
			case "Inheritance":
				relationshipType = Type.INHERITANCE;
				break;
			case "Realization":
				relationshipType = Type.REALIZATION;
				break;
		}
		e.addRelationship("", relationshipObject.get("source").getAsString(),
				relationshipObject.get("destination").getAsString(), relationshipType);
	}

	private ClassObject buildClass(JsonObject classJson) throws Exception {
		JsonArray tmp;
		ClassObject classObject;

		if (!classJson.has("name"))
			throw new InvalidObjectException("Missing name in class object");
		if (!classJson.has("fields") && classJson.get("fields").isJsonArray())
			throw new InvalidObjectException("Missing fields in class object");
		if (!classJson.has("methods") && classJson.get("methods").isJsonArray())
			throw new InvalidObjectException("Missing methods in class object");

		classObject = new ClassObject(classJson.get("name").getAsString());
		tmp = classJson.get("fields").getAsJsonArray();
		addFields(tmp, classObject.getFieldList());

		tmp = classJson.get("methods").getAsJsonArray();
		addMethods(tmp, classObject.getMethodList());

		return classObject;
	}

	private void addFields(JsonArray json, ArrayList<AttributeInterface> fields) throws Exception {
		for (JsonElement field : json) {
			if (!field.isJsonObject())
				throw new InvalidObjectException("Field is not an object");
			if (!field.getAsJsonObject().has("name"))
				throw new InvalidObjectException("Field is missing name");

			fields.add(new Field(field.getAsJsonObject().get("name").getAsString()));
		}
	}

	private void addMethods(JsonArray json, ArrayList<AttributeInterface> methods) throws Exception {
		for (JsonElement methodJson : json) {
			JsonObject methodObject;
			Method method;
			ArrayList<Parameter> parameterList = new ArrayList<Parameter>();

			if (!methodJson.isJsonObject())
				throw new InvalidObjectException("method is not an object");

			methodObject = methodJson.getAsJsonObject();

			if (!(methodObject.has("name") && methodObject.has("params")))
				throw new InvalidObjectException("method is missing nanme");

			if (!methodObject.has("params") && methodObject.get("params").isJsonArray())
				throw new InvalidObjectException("method params are malformed");

			addParameters(methodObject.get("params").getAsJsonArray(), parameterList);
			method = new Method(methodObject.get("name").getAsString(), parameterList);
			methods.add(method);
		}
	}

	private void addParameters(JsonArray json, ArrayList<Parameter> parameterList) throws Exception {
		for (JsonElement parameter : json) {
			if (!parameter.isJsonObject() && parameter.getAsJsonObject().has("name"))
				throw new InvalidObjectException("Parameter is malformed");
			parameterList.add(new Parameter(parameter.getAsJsonObject().get("name").getAsString()));
		}
	}
}

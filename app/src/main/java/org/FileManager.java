package org;

import com.google.gson.Gson;

import com.google.gson.JsonObject;
import com.google.gson.JsonElement;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.GsonBuilder;

import java.io.FileWriter;
import java.io.InvalidObjectException;

import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;

import java.util.function.Function;
import java.util.ArrayList;
import java.lang.StringBuilder;
import java.awt.Point;

import org.Model.UMLModel;
import org.Model.ClassObject;
import org.Model.Method;
import org.Model.Parameter;
import org.Model.AttributeInterface;
import org.Model.Relationship;
import org.Model.Field;
import org.Model.Relationship.Type;

import org.Controller.UMLEditor;

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
		e.addRelationship(relationshipObject.get("source").getAsString(),
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
		if (!classJson.has("position"))
			throw new InvalidObjectException("Missing position in class object");

		classObject = new ClassObject(classJson.get("name").getAsString());
		tmp = classJson.get("fields").getAsJsonArray();
		addFields(tmp, classObject.getFieldList());

		tmp = classJson.get("methods").getAsJsonArray();
		addMethods(tmp, classObject.getMethodList());
		
		JsonObject tmp2 = classJson.get("position").getAsJsonObject();
		addPosition(tmp2, classObject);

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
			if (!parameter.isJsonObject() && parameter.getAsJsonObject().has("name") && parameter.getAsJsonObject().has("type"))
				throw new InvalidObjectException("Parameter is malformed");
			parameterList.add(new Parameter(parameter.getAsJsonObject().get("name").getAsString(), parameter.getAsJsonObject().get("type").getAsString() ));
		}
	}

	private void addPosition(JsonObject json, ClassObject cls) throws Exception{
	    if(!json.has("x") && !json.has("y"))
		throw new InvalidObjectException("Position is malformed");
	    int x = json.get("x").getAsInt();
	    int y = json.get("y").getAsInt();
	    cls.setPosition(x,y);
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
		ret.append(jsonCommas("\"methods\": [", "],", classobj.getMethodList(), this::MethodsToJson));
		ret.append(PositionToJson(classobj.getPosition()));
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
		return String.format("{ \"name\": \"%s\", \"type\": \"%s\"}", param.getName(), param.getType());
	}

	private String relationshipToJson(Relationship relationship) {
		return String.format("{\"source\": \"%s\",\"destination\": \"%s\",\"type\":\"%s\"}",
				relationship.getSource().getName(),
				relationship.getDestination().getName(),
				relationship.getTypeString());
	}

	private String PositionToJson(Point position) {
		StringBuilder ret = new StringBuilder(String.format("\"position\": { \"x\": %d , \"y\": %d }", position.x, position.y));
		return ret.toString();
		
		//return String.format("\"position\": { \"x\": %d , \"y\": %d }", position.x, position.y);
	}
}

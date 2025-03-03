package org;

import com.google.gson.Gson;

import com.google.gson.JsonObject;
import com.google.gson.JsonElement;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

import java.io.FileWriter;
import java.io.InvalidObjectException;
import java.nio.file.Files;
import java.nio.file.Paths;

import java.util.ArrayList;

import org.Model.UMLModel;
import org.Model.ClassObject;
import org.Model.Relationship;

public class FileManager {

	public void save(String path, UMLModel model) throws Exception {
		String json;
		Gson gson = new Gson();
		FileWriter writer = new FileWriter(path);

		json = gson.toJson(model);
		writer.write(json);
		writer.close();
	}

	public UMLModel load(String path) throws Exception {
		UMLModel model = new UMLModel();
		ArrayList<ClassObject> clist = model.getClassList();
		ArrayList<Relationship> rlist = model.getRelationshipList();

		String content = Files.readString(Paths.get(path));
		JsonElement mdl = JsonParser.parseString(content);
		JsonElement tmp;
		JsonObject mdlObj;

		if (!mdl.isJsonObject())
			throw new InvalidObjectException("given json is invalid");

		mdlObj = mdl.getAsJsonObject();
		if (!mdlObj.has("classes"))
			throw new InvalidObjectException("Missing classes in json");
		if (!mdlObj.has("relationships"))
			throw new InvalidObjectException("Missing relationships in json");

		for (JsonElement e : mdlObj.get("classes").getAsJsonArray()) {
			clist.add(buildClass(e.getAsJsonObject()));
		}

		return model;
	}

	private ClassObject buildClass(JsonObject c) throws Exception {
		JsonArray tmp;
		ClassObject obj;

		if (!c.has("name"))
			throw new InvalidObjectException("Missing name in class object");
		if (!c.has("fields") && c.get("fields").isJsonArray())
			throw new InvalidObjectException("Missing fields in class object");
		if (!c.has("methods") && c.get("methods").isJsonArray())
			throw new InvalidObjectException("Missing methods in class object");

		obj = new ClassObject(c.get("name").getAsString());
		tmp = c.get("fields").getAsJsonArray();
		// TODO call addFields with tmp and field arraylist

		tmp = c.get("methods").getAsJsonArray();
		// TODO call addMethods with tmp and methods arraylist

		return obj;
	}

	private void addFields(JsonArray json, ArrayList<Object/* replace with feild obj */> flds) throws Exception {
		for (JsonElement field : json) {
			if (!field.isJsonObject())
				throw new InvalidObjectException("Field is not an object");
			if (!field.getAsJsonObject().has("name"))
				throw new InvalidObjectException("Field is missing name");

			flds.add(1); // TODO init field instend of #1
		}
	}

	private void addMethods(JsonArray json, ArrayList<Object/* replace with method obj */> mths) throws Exception {
		for (JsonElement method : json) {
			JsonObject m;
			if (!method.isJsonObject())
				throw new InvalidObjectException("method is not an object");

			m = method.getAsJsonObject();

			if (!(m.has("name") && m.has("params")))
				throw new InvalidObjectException("method is missing nanme");

			if (!m.has("params") && m.get("params").isJsonArray())
				throw new InvalidObjectException("method params are malformed");

			// TODO build Method
		}
	}

}

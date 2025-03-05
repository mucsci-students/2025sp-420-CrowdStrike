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
import org.Model.AttributeInterface;
import org.Model.ClassObject;
import org.Model.Relationship;
import org.Model.Field;
import org.Model.Method;
import org.Model.Parameter;

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

	public UMLModel load(String path) throws Exception {
		UMLModel model = new UMLModel();
		ArrayList<ClassObject> clist = model.getClassList();
		ArrayList<Relationship> rlist = model.getRelationshipList();

		String content = Files.readString(Paths.get(path));
		JsonElement mdl = JsonParser.parseString(content);
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

		for (JsonElement e : mdlObj.get("relationships").getAsJsonArray()) {

		}

		return model;
	}

	private void addRelationships(JsonArray json, UMLModel m) throws Exception {
		UMLEditor e = new UMLEditor(m);
		for (JsonElement r : json) {
			JsonObject obj;
			if (!r.isJsonObject())
				throw new InvalidObjectException("Relationship is not an object");
			obj = r.getAsJsonObject();
			if (!(obj.has("source") && obj.has("destination") && obj.has("type")))
				throw new InvalidObjectException("Relationship is missing a paramiter");
			e.addRelationship(obj.get("name").getAsString(), obj.get("source").getAsString(),
					obj.get("destination").getAsString());
		}
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
		addFields(tmp, obj.getFieldList());

		tmp = c.get("methods").getAsJsonArray();
		addMethods(tmp, obj.getMethodList());

		return obj;
	}

	private void addFields(JsonArray json, ArrayList<AttributeInterface> flds) throws Exception {
		for (JsonElement field : json) {
			if (!field.isJsonObject())
				throw new InvalidObjectException("Field is not an object");
			if (!field.getAsJsonObject().has("name"))
				throw new InvalidObjectException("Field is missing name");

			flds.add(new Field(field.getAsJsonObject().get("name").getAsString()));
		}
	}

	private void addMethods(JsonArray json, ArrayList<AttributeInterface> mths) throws Exception {
		for (JsonElement method : json) {
			JsonObject m;
			Method mobj;
			ArrayList<Parameter> plst = new ArrayList<Parameter>();

			if (!method.isJsonObject())
				throw new InvalidObjectException("method is not an object");

			m = method.getAsJsonObject();

			if (!(m.has("name") && m.has("params")))
				throw new InvalidObjectException("method is missing nanme");

			if (!m.has("params") && m.get("params").isJsonArray())
				throw new InvalidObjectException("method params are malformed");

			addParams(m.get("params").getAsJsonArray(), plst);
			mobj = new Method(m.get("name").getAsString(), plst);
			mths.add(mobj);
		}
	}

	private void addParams(JsonArray json, ArrayList<Parameter> plst) throws Exception {
		for (JsonElement p : json) {
			if (!p.isJsonObject() && p.getAsJsonObject().has("name"))
				throw new InvalidObjectException("Parameter is malformed");
			plst.add(new Parameter(p.getAsJsonObject().get("name").getAsString()));
		}
	}

}

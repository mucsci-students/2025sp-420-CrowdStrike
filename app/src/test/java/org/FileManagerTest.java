package org;

import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.File;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;

import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.Model.UMLModel;
import org.Model.ClassObject;
import org.Model.Relationship;
import org.Model.Relationship.Type;
import org.Controller.UMLEditor;

class FileManagerSaveTest {
	FileManager file;
	UMLModel mdl;
	static String PATH = "./test.json";

	@BeforeEach
	void init() {
		file = new FileManager();
		mdl = new UMLModel();
		ClassObject objs[] = { new ClassObject("a"), new ClassObject("b"), new ClassObject("c") };
		mdl.getClassList().add(objs[0]);
		mdl.getClassList().add(objs[1]);
		mdl.getClassList().add(objs[2]);
		mdl.getRelationshipList().add(new Relationship(objs[0], objs[1]));

		try (FileWriter writer = new FileWriter(PATH)) {
			writer.write("");
		} catch (Exception e) {
		}
		;
	}

	@Test
	public void saveOK() {
		try {
			file.save(PATH, mdl);
		} catch (Exception e) {
			fail("Failed to save");
		}

		try {
			String content = Files.readString(Paths.get(PATH));
			assertNotEquals("", content, "File content does not match expected string.");
		} catch (Exception e) {
		}

	}

	@Test
	public void saveBadPath() {
		try {
			file.save("", mdl);
			fail("Bad path no error");
		} catch (Exception e) {
		}

		try {
			String content = Files.readString(Paths.get(PATH));
			assertEquals("", content, "File content does not match expected string.");
		} catch (Exception e) {
		}
	}

	@Test
	public void saveBadModel() {
		try {
			file.save("", null);
			fail("Saved Null");
		} catch (Exception e) {
		}

		try {
			String content = Files.readString(Paths.get(PATH));
			assertEquals("", content, "File content does not match expected string.");
		} catch (Exception e) {
		}
	}
}

class FileManagerLoadTest {
	FileManager file;
	UMLEditor edit;
	UMLModel mdl;
	static String PATH = "./Load.json";
	static String CONTENT = """
			{
			  "classes": [
			    {
			      "name": "a",
			      "fields": [],
			      "methods": []
			    },
			    {
			      "name": "b",
			      "fields": [],
			      "methods": []
			    },
			    {
			      "name": "c",
			      "fields": [],
			      "methods": []
			    }
			  ],
			  "relationships": [
			    {
			      "source": "a",
			      "destination": "b",
			      "type": "Composition"
			    }
			  ]
			}
			""";

	@BeforeEach
	void init() {
		// TODO create more full state.
		file = new FileManager();
		mdl = new UMLModel();
		edit = new UMLEditor(mdl);
		edit.addClass("a");
		edit.addClass("b");
		edit.addClass("c");
		edit.addRelationship("", "a", "b", Type.COMPOSITION);

		try (FileWriter writer = new FileWriter(PATH)) {
			writer.write(CONTENT);
		} catch (Exception e) {
		}

	}

	@AfterEach
	void cleanUp() {
		File file = new File(PATH);
		file.delete();
	}

	@Test
	public void loadOK() {
		try {
			UMLModel a = file.load(PATH);
			assertEquals(a.listClasses(), mdl.listClasses(), "Miss match classes.");
			assertEquals(a.listRelationships(), mdl.listRelationships(), "Miss match relationships.");
		} catch (Exception e) {
			fail("did not load (" + e.toString() + ")");

		}
	}

	@Test
	public void loadNoFile() {
		try {
			UMLModel a = file.load("");
			fail("loaded empty path");
		} catch (Exception e) {
		}
	}

	@Test
	public void loadBadFile() {
		File file = new File(PATH);
		file.delete();
		try (FileWriter writer = new FileWriter(PATH)) {
			writer.write("{ \"this is bad json\": 69420}");
		} catch (Exception e) {
		}
		try {
			UMLModel a = this.file.load(PATH);
			fail("loaded bad file");
		} catch (Exception e) {
		}
	}
}

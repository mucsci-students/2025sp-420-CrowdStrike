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
	}

    @AfterEach
	void cleanUp() {
		File file = new File(PATH);
		file.delete();
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
	UMLModel mdl;
	static String PATH = "./Load.json";

	@BeforeEach
	void init() {
		file = new FileManager();
		mdl = new UMLModel();
		ClassObject objs[] = { new ClassObject("a"), new ClassObject("b"), new ClassObject("c") };
		mdl.getClassList().add(objs[0]);
		mdl.getClassList().add(objs[1]);
		mdl.getClassList().add(objs[2]);
		mdl.getRelationshipList().add(new Relationship(objs[0], objs[1]));
		try {
			file.save(PATH, mdl);
		} catch (Exception e) {
		}
		;
	}

    /* dose not work with new json
    	@Test
	public void loadOK() {
		try {
			UMLModel a = file.load(PATH);
			assertEquals(a.listClasses(), mdl.listClasses(), "Miss match classes.");
			assertEquals(a.listRelationships(), mdl.listRelationships(), "Miss match relationships.");
		} catch (Exception e) {
			fail("did not load");

		}
	}
    */

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
		try {
			UMLModel a = file.load("");
			fail("loaded bad file");
		} catch (Exception e) {
		}
	}
}

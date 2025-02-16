import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.fail;

// TODO write tests
class FileManagerSaveTest {
	FileManager file;
	UMLModel mdl;

	@BeforeEach
	void init() {
		file = new FileManager();
		mdl = new UMLModel();
		// TODO build some state
	}

	@Test
	public void saveOK() {
		try {
			file.save("./test.json", mdl);
		} catch (Exception e) {
			fail("Failed to save");
		}
	}

	@Test
	public void saveBadPath() {
		try {
			file.save("", mdl);
			fail("Bad path no error");
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
		// TODO build expeded state and save
	}

	@Test
	public void loadOK() {
		try {
			UMLModelInterface a = file.load(PATH);
			if (!mdl.equals(a)) {
				fail("did not load same as saved");
			}
		} catch (Exception e) {
			fail("did not load");
		}
	}

	@Test
	public void loadNoFile() {
		try {
			UMLModelInterface a = file.load("");
			fail("loaded empty path");
		} catch (Exception e) {
		}
	}

	@Test
	public void loadBadFile() {
		// TODO write bad file to path
		try {
			UMLModelInterface a = file.load("");
			fail("loaded bad file");
		} catch (Exception e) {
		}
	}
}

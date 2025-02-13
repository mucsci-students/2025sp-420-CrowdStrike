import org.junit.jupiter.api.Test;
import java.util.ArrayList;

// TODO write tests
class FileManagerSaveTest {
	FileManager file = new FileManager();
	ArrayList<RelationshipInterface> r = new ArrayList<RelationshipInterface>();
	ArrayList<ClassObjectInterface> c = new ArrayList<ClassObjectInterface>();

	@Test
	public void saveOK() {
		try {
			file.save("test.json", r, c);
		} catch (Exception e) {
		}
	}

	@Test
	public void saveBadPath() {
		try {
			file.save("", r, c);
		} catch (Exception e) {
		}
	}

	@Test
	public void saveBadRelationship() {
		try {
			file.save("test.json", null, c);
		} catch (Exception e) {
		}
	}

	@Test
	public void saveBadClass() {
		try {
			file.save("test.json", r, null);
		} catch (Exception e) {
		}
	}
}

class FileManagerLoadTest {
	FileManager file = new FileManager();

	@Test
	public void loadOK() {
	}

	@Test
	public void loadNoFile() {
	}

	@Test
	public void loadBadFile() {
	}
}

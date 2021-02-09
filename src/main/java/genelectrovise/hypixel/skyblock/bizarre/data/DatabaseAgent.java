package genelectrovise.hypixel.skyblock.bizarre.data;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.CharBuffer;

import genelectrovise.hypixel.skyblock.bizarre.data.IFileDefinition.FileType;

public class DatabaseAgent {

	public static final IFileDefinition TRACKING = IFileDefinition.of("database/tracking.json", FileType.FILE);
	public static final IFileDefinition CONFIG = IFileDefinition.of("config.json", FileType.FILE);
	public static final IFileDefinition REPORTS = IFileDefinition.of("reports/", FileType.DIRECTORY);
	public static final IFileDefinition DATABASE = IFileDefinition.of("database/", FileType.DIRECTORY);

	public DatabaseAgent() {
		ensureExistance(TRACKING, "{\"tracking\":[]}");
		ensureExistance(CONFIG, "{\"apikey\":\"NONE\"}");
		ensureExistance(REPORTS, "");
		ensureExistance(DATABASE, "");
	}

	public void ensureExistance(IFileDefinition definition, String defaultContents) {

		try {

			File file = fromDefinition(definition);

			if (definition.type() == FileType.FILE) {
				if (!file.getParentFile().exists()) {
					file.getParentFile().mkdirs();
				}

				if (!file.exists()) {
					write(definition, defaultContents);
				}
			}

			if (definition.type() == FileType.DIRECTORY) {
				file.mkdirs();
			}
		} catch (IOException io) {
			io.printStackTrace();
		}
	}

	public synchronized String read(IFileDefinition definition) throws IOException {
		File file = fromDefinition(definition);
		ensureExistance(definition, "");

		BufferedReader reader = new BufferedReader(new FileReader(file));
		CharBuffer buffer = CharBuffer.allocate(Math.toIntExact(file.length()));
		reader.read(buffer);

		char[] chars = buffer.array();
		String contents = "";
		for (char c : chars) {
			contents = contents + c;
		}

		reader.close();
		return contents;
	}

	public synchronized String write(IFileDefinition definition, String contents) throws IOException {
		File file = fromDefinition(definition);
		FileWriter writer = new FileWriter(file);

		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}

		writer.write(contents);
		writer.flush();
		writer.close();
		return contents;
	}

	public synchronized void openInSystemEditor(IFileDefinition definition) throws IOException {
		File file = fromDefinition(definition);
		Desktop.getDesktop().open(file);
	}

	public synchronized void openRoamingDirectory() {
		try {
			Desktop.getDesktop().open(roamingDirectory());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * An {@link IFileDefinition} "database/tracking.json" would return the file
	 * "USER_HOME\AppData\Roaming\bizarre\database\tracking.json"
	 * 
	 * @param definition
	 */
	private synchronized File fromDefinition(IFileDefinition definition) {
		return new File(roamingDirectory().getAbsolutePath() + "\\" + definition.path());
	}

	/**
	 * 
	 * @return
	 */
	public File roamingDirectory() {
		File dir = new File(System.getProperty("user.home") + "\\AppData\\Roaming\\bizarre\\");
		dir.mkdirs();
		return dir;
	}
}

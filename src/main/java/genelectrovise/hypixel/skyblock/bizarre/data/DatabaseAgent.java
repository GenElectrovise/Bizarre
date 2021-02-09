package genelectrovise.hypixel.skyblock.bizarre.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.CharBuffer;

public class DatabaseAgent {

	public static void main(String[] args) {
		DatabaseAgent agent = new DatabaseAgent();
		
		try {
			System.out.println(agent.read(() -> "database/tracking.json"));
			agent.write(IFileDefinition.of("database/tester.txt"), "hello there");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	public synchronized String read(IFileDefinition definition) throws IOException {
		File file = fromDefinition(definition);
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
		writer.write(contents);
		writer.flush();
		writer.close();
		return contents;
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

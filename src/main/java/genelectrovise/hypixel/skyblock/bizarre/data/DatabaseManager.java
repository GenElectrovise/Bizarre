package genelectrovise.hypixel.skyblock.bizarre.data;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.google.common.collect.HashBiMap;

import genelectrovise.hypixel.skyblock.bizarre.data.access.FileAccessController;
import genelectrovise.hypixel.skyblock.bizarre.data.access.IFileAccessController;

public class DatabaseManager {

	private volatile FileAccessController ROAMING_DIR = new FileAccessController(roamingDirectory().getAbsolutePath());
	private volatile FileAccessController DATABASE_DIR = new FileAccessController(databaseDirectory().getAbsolutePath());
	private volatile FileAccessController TRACKING_JSON = new FileAccessController(trackingJson().getAbsolutePath());
	private volatile FileAccessController CONFIG_JSON = new FileAccessController(configJson().getAbsolutePath());
	private volatile FileAccessController NETWORKS_JSON = new FileAccessController(networksJson().getAbsolutePath());

	private HashBiMap<String, IFileAccessController> controllers = HashBiMap.create();

	public DatabaseManager() {
		controllers.putIfAbsent("roaming", ROAMING_DIR);
		controllers.putIfAbsent("database", DATABASE_DIR);
		controllers.putIfAbsent("tracking", TRACKING_JSON);
		controllers.putIfAbsent("config", CONFIG_JSON);
		controllers.putIfAbsent("networks", NETWORKS_JSON);
	}

	public IFileAccessController get(String key) {
		return controllers.get(key);
	}

	public String get(IFileAccessController key) {
		return controllers.inverse().get(key);
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

	/**
	 * 
	 * @return
	 */
	public File databaseDirectory() {
		return validateDirectory(roamingDirectory().getAbsolutePath() + "\\database");
	}

	/**
	 * 
	 * @return
	 */
	public File reportsDirectory() {
		return validateDirectory(roamingDirectory().getAbsolutePath() + "\\reports");
	}

	/**
	 * 
	 * @return
	 */
	public File trackingJson() {
		return validateFile(databaseDirectory().getAbsolutePath() + "\\tracking.json", "{\"tracking\":[]}");
	}

	/**
	 * 
	 * @return
	 */
	private File configJson() {
		return validateFile(roamingDirectory().getAbsolutePath() + "\\config.json", "{\"apikey\":\"NONE\"}");
	}

	/**
	 * 
	 * @return
	 */
	private File networksJson() {
		return validateFile(databaseDirectory().getAbsolutePath() + "\\networks.json", "{\"networks\":{}}");
	}

	/**
	 * 
	 * @param path
	 * @return
	 */
	public File validateDirectory(String path) {
		File file = new File(path);
		file.mkdirs();
		System.out.println("Validated managed directory " + path);
		return file;
	}

	/**
	 * 
	 * @param path
	 * @param defaultContents
	 * @return
	 */
	public File validateFile(String path, String defaultContents) {
		File file = new File(path);
		if (!file.exists()) {

			System.out.println("File " + path + " does not exist! Will attempt to create with default contents " + defaultContents);

			try {

				FileWriter writer = new FileWriter(file);
				writer.write(defaultContents);
				writer.flush();
				writer.close();

			} catch (IOException io) {
				io.printStackTrace();
			}
		}

		System.out.println("Validated managed file " + path);

		return file;
	}
}

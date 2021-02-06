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

	private HashBiMap<String, IFileAccessController> controllers = HashBiMap.create();

	public DatabaseManager() {
		controllers.putIfAbsent("roaming", ROAMING_DIR);
		controllers.putIfAbsent("database", DATABASE_DIR);
		controllers.putIfAbsent("tracking", TRACKING_JSON);
		controllers.putIfAbsent("config", CONFIG_JSON);
	}

	public IFileAccessController get(String key) {
		return controllers.get(key);
	}

	public String get(IFileAccessController key) {
		return controllers.inverse().get(key);
	}

	public File roamingDirectory() {
		File dir = new File(System.getProperty("user.home") + "\\AppData\\Roaming\\bizarre\\");
		dir.mkdirs();
		return dir;
	}

	public File databaseDirectory() {
		File roaming = roamingDirectory();
		File database = new File(roaming.getAbsolutePath() + "\\database");
		database.mkdirs();
		return database;
	}

	public File reportsDirectory() {
		File roaming = roamingDirectory();
		File reports = new File(roaming.getAbsolutePath() + "\\reports");
		reports.mkdirs();
		return reports;
	}

	public File trackingJson() {
		File json = new File(databaseDirectory().getAbsolutePath() + "\\tracking.json");
		if (!json.exists()) {

			try {

				FileWriter writer = new FileWriter(json);
				writer.write("{\"tracking\":[]}");
				writer.flush();
				writer.close();

			} catch (IOException io) {
				io.printStackTrace();
			}
		}
		return json;
	}

	private File configJson() {
		File json = new File(roamingDirectory().getAbsolutePath() + "\\config.json");
		if (!json.exists()) {

			try {

				FileWriter writer = new FileWriter(json);
				writer.write("{\"apikey\":\"NONE\"}");
				writer.flush();
				writer.close();

			} catch (IOException io) {
				io.printStackTrace();
			}
		}
		return json;
	}
}

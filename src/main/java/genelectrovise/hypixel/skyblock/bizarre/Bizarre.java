package genelectrovise.hypixel.skyblock.bizarre;

import java.util.UUID;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import genelectrovise.hypixel.skyblock.bizarre.data.DatabaseManager;
import genelectrovise.hypixel.skyblock.bizarre.data.access.IFileAccessController;
import genelectrovise.hypixel.skyblock.bizarre.neuroph.NetworkManager;
import net.hypixel.api.HypixelAPI;

public class Bizarre {
	public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	public static final DatabaseManager DATABASE_MANAGER = new DatabaseManager();
	public static UUID apiKey = null;
	public static final HypixelAPI HYPIXEL_API = getApi();
	public static final NetworkManager NETWORK_MANAGER = getNetworkManager();

	private static HypixelAPI getApi() {

		IFileAccessController.withinOpenAndClose(DATABASE_MANAGER.get("config"), (controller, file) -> {

			try {
				JsonObject object = GSON.fromJson(controller.read(), JsonObject.class);
				
				if(!object.has("apikey")) {
					throw new NullPointerException("The apikey of config.json must be populated! Use: \"apikey\":\"KEY_GOES_HERE\"");
				}
				
				String key = object.get("apikey").getAsString();
				
				if(key == "NONE") {
					throw new IllegalStateException("The apikey of config.json cannot be NONE. Get a key by going onto the Hypixel server and running '/api'.");
				}
				
				Bizarre.apiKey = UUID.fromString(key);
			} catch (Exception e) {
				System.err.println("Error getting apikey! (Exists, and not NONE, so must be invalid format?)");
				e.printStackTrace();
			}
		});

		return new HypixelAPI(apiKey);
	}

	private static NetworkManager getNetworkManager() {
		return null;
	}
}

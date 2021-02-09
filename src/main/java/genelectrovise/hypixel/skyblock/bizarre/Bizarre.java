package genelectrovise.hypixel.skyblock.bizarre;

import java.io.IOException;
import java.util.UUID;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import genelectrovise.hypixel.skyblock.bizarre.data.DatabaseAgent;
import genelectrovise.hypixel.skyblock.bizarre.neuroph.NetworkManager;
import net.hypixel.api.HypixelAPI;

public class Bizarre {
	public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	public static final DatabaseAgent DATABASE_AGENT = new DatabaseAgent();
	public static UUID apiKey = null;
	public static final HypixelAPI HYPIXEL_API = getApi();
	public static final NetworkManager NETWORK_MANAGER = getNetworkManager();

	private static HypixelAPI getApi() {

		try {
			JsonObject object = GSON.fromJson(DATABASE_AGENT.read(DatabaseAgent.CONFIG), JsonObject.class);

			if (!object.has("apikey")) {
				throw new NullPointerException("The apikey of config.json must be populated! Use: \"apikey\":\"KEY_GOES_HERE\"");
			}

			String key = object.get("apikey").getAsString();

			if (key.contentEquals("NONE") || key.equals("") || key == null) {
				System.err.println("The apikey of config.json cannot be NONE, '', or null. Get a key by going onto the Hypixel server and running '/api'.");
				System.exit(-2);
			}

			Bizarre.apiKey = UUID.fromString(key);
		} catch (IllegalStateException il) {
			System.err.println("Error getting apikey! (Exists, and not NONE, so must be invalid format?)");
			il.printStackTrace();

		} catch (JsonSyntaxException js) {
			js.printStackTrace();

		} catch (IOException io) {
			io.printStackTrace();
		}

		return new HypixelAPI(apiKey);
	}

	private static NetworkManager getNetworkManager() {
		return null;
	}
}

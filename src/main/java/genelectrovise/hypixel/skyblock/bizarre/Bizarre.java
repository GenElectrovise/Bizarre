package genelectrovise.hypixel.skyblock.bizarre;

import java.io.IOException;
import java.util.UUID;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import genelectrovise.hypixel.skyblock.bizarre.data.FileSystemAgent;
import genelectrovise.hypixel.skyblock.bizarre.neuroph.NeurophManager;
import net.hypixel.api.HypixelAPI;

public class Bizarre {

	static {
		Runtime.getRuntime().addShutdownHook(new ShutdownHooks());
	}

	public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	public static final FileSystemAgent FILE_SYSTEM_AGENT = new FileSystemAgent();
	public static UUID apiKey = null;
	public static final HypixelAPI HYPIXEL_API = getApi();
	public static final NeurophManager NEUROPH_MANAGER = getNeurophManager();
	public static final H2DatabaseAgent H2_DATABASE_AGENT = getH2DatabaseAgent();

	private static HypixelAPI getApi() {

		try {
			JsonObject object = GSON.fromJson(FILE_SYSTEM_AGENT.read(FileSystemAgent.CONFIG), JsonObject.class);

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

	public static void successfulExecutionCompletedSoWillNowExit() {
		System.exit(0);
	}

	private static NeurophManager getNeurophManager() {
		return null;
	}

	private static H2DatabaseAgent getH2DatabaseAgent() {
		return new H2DatabaseAgent(H2DatabaseAgent.getDefaultPath());
	}
}

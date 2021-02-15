package genelectrovise.hypixel.skyblock.bizarre.data;

public class Namespacer {
	public static String internalise(String external) {
		external = external.replace(":", "_COLON_");
		return external;
	}

	public static String externalise(String internal) {
		internal = internal.replaceAll("_COLON_", ":");

		return internal;
	}
}

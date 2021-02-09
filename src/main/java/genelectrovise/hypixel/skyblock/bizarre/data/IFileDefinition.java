package genelectrovise.hypixel.skyblock.bizarre.data;

/**
 * Example input: database/tracking.json
 *
 */
@FunctionalInterface
public interface IFileDefinition {

	String path();
	
	public static IFileDefinition of(String path) {
		return () -> path;
	}
}

package genelectrovise.hypixel.skyblock.bizarre.data;

/**
 * Example input: database/tracking.json
 *
 */
public interface IFileDefinition {

	String path();

	FileType type();

	public static IFileDefinition of(String path, FileType type) {
		return new IFileDefinition() {

			@Override
			public String path() {
				return path;
			}

			@Override
			public FileType type() {
				return type;
			}
		};
	}

	public enum FileType {
		FILE, DIRECTORY;
	}
}

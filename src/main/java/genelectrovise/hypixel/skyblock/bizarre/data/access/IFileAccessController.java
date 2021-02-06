package genelectrovise.hypixel.skyblock.bizarre.data.access;

import java.io.File;
import java.util.function.BiConsumer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Given that there are thread executors involved here, I'd rather not have any
 * race conditions... Enter the {@link IFileAccessController}! For all your
 * don't-get-accessed-by-two-things-at-the-same-time needs!
 *
 */
public interface IFileAccessController {

	/**
	 * Opens the controlled file.
	 * 
	 * @return The opened file, or null if it is already open.
	 */
	@Nullable
	File open();

	/**
	 * Closes the controlled file.
	 */
	@Nonnull
	void close();

	/**
	 * @return Whether the file is currently accessible (closed), or not (open)
	 */
	@Nonnull
	boolean accessible();

	/**
	 * @return The path to the controlled file.
	 */
	@Nonnull
	String path();

	/**
	 * Reads all bytes out of the controlled file. (Must be accessible)
	 */
	@Nonnull
	String read();

	/**
	 * Executes the given {@link BiConsumer} on the given
	 * {@link IFileAccessController}. The file is automatically opened and closed
	 * around the consumer's execution.
	 * 
	 * @param controller
	 * @param consumer
	 */
	public static void withinOpenAndClose(IFileAccessController controller, BiConsumer<IFileAccessController, File> consumer) {
		File file = controller.open();
		consumer.accept(controller, file);
		controller.close();
	}
}

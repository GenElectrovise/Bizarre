package genelectrovise.hypixel.skyblock.bizarre.command;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.JsonObject;

import genelectrovise.hypixel.skyblock.bizarre.Bizarre;
import genelectrovise.hypixel.skyblock.bizarre.data.access.IFileAccessController;
import genelectrovise.hypixel.skyblock.bizarre.data.file.TrackingFile;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name = "tracking", description = "Manages tracked items")
public class CmdTrack implements Runnable {

	@Option(names = { "-item" }, required = true)
	private String item;

	/**
	 * The command for adding a new tracking entry
	 */
	@Command(name = "add", description = "Adds an item to the tracking list")
	public void cmdAdd() {

		IFileAccessController.withinOpenAndClose(Bizarre.DATABASE_MANAGER.get("tracking"), (controller, file) -> {
			TrackingFile trackingFile = TrackingFile.fromJson(Bizarre.GSON, controller.read());

			System.out.println("Adding: " + item);

			boolean added = false;
			if (trackingFile.getTracking().contains(item)) {
				
			} else {
				added = trackingFile.getTracking().add(item);
				writeTrackingFileToFileSystem(trackingFile, file);
			}

			System.out.println("Added: " + added);
			displayTracking(trackingFile);
		});
	}

	/**
	 * Removes a tracking entry
	 */
	@Command(name = "remove", description = "Removes an item from the tracking list")
	public void cmdRemove() {

		IFileAccessController.withinOpenAndClose(Bizarre.DATABASE_MANAGER.get("tracking"), (controller, file) -> {
			TrackingFile trackingFile = TrackingFile.fromJson(Bizarre.GSON, controller.read());

			System.out.println("Removing: " + item);

			boolean contained = trackingFile.getTracking().contains(item);
			boolean removed = trackingFile.getTracking().remove(item);
			writeTrackingFileToFileSystem(trackingFile, file);

			System.out.println("Previously contained: " + contained + "; Removed: " + removed);
			displayTracking(trackingFile);

		});
	}

	/**
	 * Runs the inherent behaviour of the command - dumps the tracking file.
	 */
	@Override
	public void run() {

		IFileAccessController.withinOpenAndClose(Bizarre.DATABASE_MANAGER.get("tracking"), (controller, file) -> {
			TrackingFile trackingFile = TrackingFile.fromJson(Bizarre.GSON, controller.read());

			displayTracking(trackingFile);
		});
	}

	/**
	 * Writes the given {@link TrackingFile} to the file system.
	 * 
	 * @param trackingFileIn The {@link TrackingFile} to write from.
	 * @param destination    The destination file. Obtained by an
	 *                       {@link IFileAccessController}.
	 */
	public void writeTrackingFileToFileSystem(TrackingFile trackingFileIn, File destination) {

		try {

			String json = Bizarre.GSON.toJson(trackingFileIn, TrackingFile.class);
			FileWriter writer = new FileWriter(destination);
			writer.write(json);
			writer.flush();
			writer.close();

		} catch (IOException io) {
			io.printStackTrace();
		}

	}

	public void closeTrackingFile() {
		Bizarre.DATABASE_MANAGER.get("tracking").close();
	}

	public void displayTracking(TrackingFile file) {
		StringBuilder builder = new StringBuilder("Now tracking: [");
		file.getTracking().forEach((str) -> {
			builder.append(str);
			builder.append(" ");
		});
		builder.append("]");

		System.out.println(builder.toString());
	}

}

package genelectrovise.hypixel.skyblock.bizarre.command;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import genelectrovise.hypixel.skyblock.bizarre.Bizarre;
import genelectrovise.hypixel.skyblock.bizarre.data.DatabaseAgent;
import genelectrovise.hypixel.skyblock.bizarre.data.file.TrackingFile;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name = "tracking", description = "Manages tracked items")
public class CmdTracking implements Runnable {

	@Option(names = { "-item" }, required = true)
	private String item;

	/**
	 * The command for adding a new tracking entry
	 */
	@Command(name = "add", description = "Adds an item to the tracking list")
	public void cmdAdd() {
		try {
			System.out.println("Adding: " + item);

			TrackingFile trackingFile = TrackingFile.fromJson(Bizarre.GSON, Bizarre.DATABASE_AGENT.read(DatabaseAgent.TRACKING));
			boolean added = false;
			if (trackingFile.getTracking().contains(item)) {

			} else {
				added = trackingFile.getTracking().add(item);
				Bizarre.DATABASE_AGENT.write(DatabaseAgent.TRACKING, Bizarre.GSON.toJson(trackingFile, TrackingFile.class));
			}
			System.out.println("Added: " + added);
			displayTracking(trackingFile);

		} catch (IOException io) {
			io.printStackTrace();
		}
	}

	/**
	 * Removes a tracking entry
	 */
	@Command(name = "remove", description = "Removes an item from the tracking list")
	public void cmdRemove() {

		try {
			System.out.println("Removing: " + item);

			TrackingFile trackingFile = TrackingFile.fromJson(Bizarre.GSON, Bizarre.DATABASE_AGENT.read(DatabaseAgent.TRACKING));
			boolean contained = trackingFile.getTracking().contains(item);
			boolean removed = trackingFile.getTracking().remove(item);
			Bizarre.DATABASE_AGENT.write(DatabaseAgent.TRACKING, Bizarre.GSON.toJson(trackingFile, TrackingFile.class));

			System.out.println("Previously contained: " + contained + "; Removed: " + removed);
			displayTracking(trackingFile);
		} catch (IOException io) {
			io.printStackTrace();
		}

	}

	/**
	 * Runs the inherent behaviour of the command - dumps the tracking file.
	 */
	@Override
	public void run() {

		TrackingFile trackingFile;
		try {
			trackingFile = TrackingFile.fromJson(Bizarre.GSON, Bizarre.DATABASE_AGENT.read(DatabaseAgent.TRACKING));

			displayTracking(trackingFile);
		} catch (IOException io) {
			io.printStackTrace();
		}
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

	public void displayTracking(TrackingFile file) {
		StringBuilder builder = new StringBuilder("Now tracking: [ ");
		file.getTracking().forEach((str) -> {
			builder.append(str);
			builder.append(" ");
		});
		builder.append("]");

		System.out.println(builder.toString());
	}

}

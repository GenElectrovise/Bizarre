package genelectrovise.hypixel.skyblock.bizarre.command.tracking;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import genelectrovise.hypixel.skyblock.bizarre.Bizarre;
import genelectrovise.hypixel.skyblock.bizarre.H2DatabaseAgent;
import genelectrovise.hypixel.skyblock.bizarre.data.Namespacer;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name = "tracking", description = "Manages tracked items")
public class CmdTracking implements Runnable {

	@Option(names = { "-item" }, required = true)
	private String item;

	/**
	 * The command for adding a new tracking entry. <br>
	 * <code>MERGE INTO Bizarre.TrackedItems KEY (external_name) values ('internal_name', 'external_name')</code>
	 */
	@Command(name = "add", description = "Adds an item to the tracking list")
	public void cmdAdd() {
		try {
			System.out.println("Adding: " + item);

			createRequired();

			// IF the external_name doesn't exist, insert the given external_name into the
			// table
			H2DatabaseAgent.instance().createStatement().execute("MERGE INTO Bizarre.TrackedItems KEY (external_name) values ('" + Namespacer.internalise(item) + "', '" + item + "')");
			displayTracking();

		} catch (SQLException sql) {
			sql.printStackTrace();
		}

		System.out.println(" >> DONE << ");
		Bizarre.successfulExecutionCompletedSoWillNowExit();
	}

	/**
	 * Removes a tracking entry. <br>
	 * <code>DELETE FROM Bizarre.TrackedItems WHERE external_name='item'</code>
	 */
	@Command(name = "remove", description = "Removes an item from the tracking list")
	public void cmdRemove() {
		System.out.println("Removing: " + item);

		try {

			H2DatabaseAgent.instance().createStatement().execute("DELETE FROM Bizarre.TrackedItems WHERE external_name='" + item + "'");
			displayTracking();

		} catch (SQLException sql) {
			sql.printStackTrace();
		}

		System.out.println(" >> DONE << ");
		Bizarre.successfulExecutionCompletedSoWillNowExit();
	}

	/**
	 * Runs the inherent behaviour of the command - dumps the tracking file.
	 */
	@Override
	public void run() {

		try {
			displayTracking();
		} catch (SQLException sql) {
			sql.printStackTrace();
		}
		
		Bizarre.successfulExecutionCompletedSoWillNowExit();
	}

	public void displayTracking() throws SQLException {

		// Get a set of TrackedItems
		ResultSet results = H2DatabaseAgent.instance().createStatement().executeQuery("SELECT * FROM Bizarre.TrackedItems");
		ArrayList<String> resultsList = new ArrayList<String>();

		// For each item in the results, print
		while (results.next()) {
			resultsList.add(results.getString(1));
		}

		StringBuilder builder = new StringBuilder("Now tracking: [ ");
		resultsList.forEach((str) -> {
			builder.append(str);
			builder.append(" ");
		});
		builder.append("]");

		System.out.println(builder.toString());
	}

	public static void createRequired() {
		try {

			H2DatabaseAgent.instance().createStatement().execute("CREATE SCHEMA IF NOT EXISTS Bizarre");
			H2DatabaseAgent.instance().createStatement().execute("CREATE TABLE IF NOT EXISTS Bizarre.TrackedItems(external_name varchar(255), internal_name varchar(255))");

		} catch (SQLException sql) {
			sql.printStackTrace();
		}
	}

}

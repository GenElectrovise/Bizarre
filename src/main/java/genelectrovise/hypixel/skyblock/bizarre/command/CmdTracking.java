package genelectrovise.hypixel.skyblock.bizarre.command;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import genelectrovise.hypixel.skyblock.bizarre.Bizarre;
import genelectrovise.hypixel.skyblock.bizarre.sql.H2DatabaseAgent;
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
			boolean added = false;
			
			createRequired();
			
			// SELECT * FROM TrackedItems WHERE Name='DIAMOND'
			ResultSet doesAlreadyExist = Bizarre.H2_DATABASE_AGENT.getConnection().createStatement().executeQuery("SELECT * FROM Bizarre.TrackedItems WHERE external_name='" + item + "'");

			// If value in first column is null (no identically named item exists)
			if (doesAlreadyExist.getString(1) == null) {

				// INSERT INTO TrackedItems VALUES (DIAMOND);
				@SuppressWarnings("unused")
				ResultSet resultOfInput = Bizarre.H2_DATABASE_AGENT.getConnection().createStatement().executeQuery("INSERT INTO Bizarre.TrackedItems VALUES (" + item + ");");
				added = true;
			}

			System.out.println("Added: " + added);
			displayTracking();

		} catch (SQLException sql) {
			sql.printStackTrace();
		}
	}

	/**
	 * Removes a tracking entry
	 */
	@Command(name = "remove", description = "Removes an item from the tracking list")
	public void cmdRemove() {
		System.out.println("Removing: " + item);

		boolean previouslyContained = false;
		boolean removed = false;

		try {

			// SELECT * FROM TrackedItems WHERE Name='DIAMOND'
			ResultSet doesAlreadyExist = Bizarre.H2_DATABASE_AGENT.getConnection().createStatement().executeQuery("SELECT * FROM Bizarre.TrackedItems WHERE external_name='" + item + "'");

			if (doesAlreadyExist.getString(1) == null) {
				previouslyContained = false;
			}

			// If previously contained
			// DELETE FROM TrackedItems WHERE Name='DIAMOND';
			if (previouslyContained) {
				ResultSet removing = Bizarre.H2_DATABASE_AGENT.getConnection().createStatement().executeQuery("DELETE FROM Bizarre.TrackedItems WHERE external_name='" + item + "';");
				removed = true;
			}

			System.out.println("Previously contained: " + previouslyContained + "; Removed: " + removed);
			displayTracking();

		} catch (SQLException sql) {
			sql.printStackTrace();
		}

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
	}

	public void displayTracking() throws SQLException {

		// Get a set of TrackedItems
		ResultSet results = Bizarre.H2_DATABASE_AGENT.getConnection().createStatement().executeQuery("SELECT * FROM Bizarre.TrackedItems");
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
	
	private void createRequired() {
		try {

			H2DatabaseAgent.instance().createStatement().execute("CREATE SCHEMA IF NOT EXISTS Bizarre");
			H2DatabaseAgent.instance().createStatement().execute("CREATE TABLE IF NOT EXISTS Bizarre.TrackedItems(external_name varchar(255), internal_name varchar(255))");
			
		} catch (SQLException sql) {
			sql.printStackTrace();
		}
	}

}

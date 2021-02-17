package genelectrovise.hypixel.skyblock.bizarre.neuroph;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.google.common.collect.HashBiMap;

import genelectrovise.hypixel.skyblock.bizarre.H2DatabaseAgent;

public class NeurophManager {

	private static final int[] NETWORK_TRAINING_INTERVALS = { 5, 30, 60, 1440, 10080 };

	/**
	 * Manages the training of a all NNETs
	 * 
	 * @param threads
	 */
	public void trainNeuralNetworks_forTrackedItems_withAllAvailableData(int threads) {

		ExecutorService service = Executors.newFixedThreadPool(threads);

		// Prepare a programmatic representation of Bizarre.TrackedItems
		// <internal_name, external_name>
		ResultSet trackedItemsRAW;
		HashBiMap<String, String> trackedItems = HashBiMap.create();
		try {
			// Raw contents
			trackedItemsRAW = H2DatabaseAgent.instance().createStatement().executeQuery("SELECT * FROM Bizarre.TrackedItems");

			// Enter into Map
			while (trackedItemsRAW.next()) {
				String internal_name = trackedItemsRAW.getString(1);
				String external_name = trackedItemsRAW.getString(2);
				trackedItems.put(internal_name, external_name);
			}
		} catch (SQLException sql) {
			sql.printStackTrace();
			System.err.println("Unable to train neural networks. Failed to load Bizarre.TrackedItems");
			throw new IllegalStateException(sql);
		}

		// Process each tracked item in a new thread
		trackedItems.forEach((internal_name, external_name) -> {
			service.execute(() -> processItem_threadSafe(internal_name, external_name));
		});
	}

	/**
	 * A delegate of other methods. Processes the training of one item's set of
	 * NNETs in a THREAD SAFE manner.
	 * 
	 * @param internal_name
	 * @param external_name
	 */
	private void processItem_threadSafe(String internal_name, String external_name) {

		// Create a list of timestamps for the data entries for the item
		List<Timestamp> timestamps = new ArrayList<Timestamp>();
		try {
			ResultSet allTimestampsRAW = H2DatabaseAgent.instance().createStatement().executeQuery("SELECT timestamp FROM Bizarre_Responses." + internal_name);
			while (allTimestampsRAW.next()) {
				timestamps.add(allTimestampsRAW.getTimestamp(1));
			}
		} catch (SQLException sql) {
			sql.printStackTrace();
		}

		// For each interval, find which stamps are within tolerance, and pass on the
		// data.
		for (int i = 0; i < NETWORK_TRAINING_INTERVALS.length; i++) {

			Timestamp last = timestamps.get(0);
			Timestamp idealFuture;
			List<Timestamp> useStamps = new ArrayList<Timestamp>();
			long idealTimeGap = TimeUnit.MINUTES.toMillis(NETWORK_TRAINING_INTERVALS[i]);

			// For each recorded stamp, find the next applicable one
			for (Timestamp timestamp : timestamps) {

				// Get the ideal future time
				idealFuture = modifyTime(last, idealTimeGap, false);

				// Generate the smallest and largest stamps that can be used
				Timestamp smallest = modifyTime(idealFuture, new Double(idealTimeGap * 0.05).longValue(), true);
				Timestamp largest = modifyTime(idealFuture, new Double(idealTimeGap * 0.05).longValue(), false);

				// IF after smallest && before largest, use the stamp
				// smallest < X < largest
				if (timestamp.after(smallest) && timestamp.before(largest)) {
					useStamps.add(timestamp);
				}

				// Shift the current time forward
				last = idealFuture;
			}

			trainWithTimestamps(useStamps, NETWORK_TRAINING_INTERVALS[i], internal_name);
		}
	}

	/**
	 * Trains a network using data with the given time stamps.
	 * 
	 * @param useStamps
	 * @param i
	 * @param internal_name
	 */
	private void trainWithTimestamps(List<Timestamp> useStamps, int i, String internal_name) {
		// Create the name of the file holding the network
		// >> If the file doesn't exist, create one with a standard network
		// Load the network from the file.

		// FOR EACH COMBINATION OF 10 CONSECUTIVE STAMPS
		// >> SELECT * FROM Bizarre_Responses.internal_name
		// >> load columns 2-10 (I think...? The ones with numerical data in!!)
		// >> Clamp the values between 0 and 1M (or other functional values)
		// >> Train the network. (new DataSet)
	}

	private Timestamp modifyTime(Timestamp oldStamp, long millis, boolean trueSubtractElseAdd) {
		return new Timestamp(oldStamp.getTime() + (trueSubtractElseAdd ? -1 * millis : millis));
	}
}

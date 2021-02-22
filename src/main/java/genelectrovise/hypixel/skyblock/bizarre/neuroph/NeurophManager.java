package genelectrovise.hypixel.skyblock.bizarre.neuroph;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.nnet.Perceptron;

import com.google.common.collect.HashBiMap;
import com.google.common.net.PercentEscaper;

import genelectrovise.hypixel.skyblock.bizarre.Bizarre;
import genelectrovise.hypixel.skyblock.bizarre.H2DatabaseAgent;
import genelectrovise.hypixel.skyblock.bizarre.data.FileSystemAgent;
import genelectrovise.hypixel.skyblock.bizarre.data.ItemResponse;

public class NeurophManager {

	private static final int SAMPLE_SIZE = 20;
	private static final int DATA_POINTS_PER_RESPONSE = 9;

	// i.e. 9 * 20 + 1 (big 8 & time * sample size + plus the time to predict at)
	private static final int INPUT_NEURONS = DATA_POINTS_PER_RESPONSE * SAMPLE_SIZE + 1;
	private static final int OUTPUT_NEURONS = 8;

	/**
	 * Manages the training of a all NNETs
	 * 
	 * @param threads
	 */
	public void trainNeuralNetworks_forTrackedItems_withAllAvailableData(int threads) {
		
		System.out.println("RETRAINING ALL NNETs!");

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
		
		System.out.println("Found TrackedItems " + trackedItems);

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

		try {
			// X Y Z A
			ResultSet allTimestamps = H2DatabaseAgent.instance().createStatement().executeQuery("SELECT timestamp FROM Bizarre_Responses." + internal_name);
			int count = H2DatabaseAgent.instance().createStatement().executeQuery("SELECT COUNT(1) FROM Bizarre_Responses." + internal_name).getInt("rowcount");
			System.out.println("Bizarre_Responses." + internal_name + " contains " + count + " entries");

			// 4 - 2 = 2
			int startIndex = count - SAMPLE_SIZE;
			int currentIndex;

			// currentIndex < startIndex (pointer position)
			// 0 < 2 (pointer 1 -> 2 of 4)
			// 1 < 2 (pointer 2 -> 3 of 4)
			// 2 = 2 (pointer @ 3 of 4, BREAK)
			for (currentIndex = 0; currentIndex < startIndex; currentIndex++) {
				allTimestamps.next();
			}

			List<ItemResponse> responses = new ArrayList<ItemResponse>();
			while (allTimestamps.next()) {
				double buyPrice = allTimestamps.getDouble("buy_price");
				double buyVolume = allTimestamps.getDouble("buy_volume");
				double buyOrders = allTimestamps.getDouble("buy_orders");
				double buyMovingWeek = allTimestamps.getDouble("buy_moving_week");
				double sellPrice = allTimestamps.getDouble("sell_price");
				double sellVolume = allTimestamps.getDouble("sell_volume");
				double sellOrders = allTimestamps.getDouble("sell_orders");
				double sellMovingWeek = allTimestamps.getDouble("sell_moving_week");
				double timestamp = Double.longBitsToDouble(allTimestamps.getTimestamp("timestamp").getTime());

				ItemResponse response = new ItemResponse(internal_name, external_name, buyPrice, buyVolume, buyOrders, buyMovingWeek, sellPrice, sellVolume, sellOrders, sellMovingWeek, timestamp);
				responses.add(response);
				System.out.println("Found response for " + internal_name + " " + response);
			}

			List<Double> networkTrainingInputsList = new ArrayList<Double>();
			List<Double> networkTrainingOutputsList = new ArrayList<Double>();
			for (int i = 0; i < responses.size(); i++) {
				ItemResponse response = responses.get(i);

				// Not the final one!
				if (i != responses.size() - 1) {
					// Add all as inputs
					networkTrainingInputsList.add(Double.valueOf(response.getBuyPrice()));
					networkTrainingInputsList.add(Double.valueOf(response.getBuyVolume()));
					networkTrainingInputsList.add(Double.valueOf(response.getBuyOrders()));
					networkTrainingInputsList.add(Double.valueOf(response.getBuyMovingWeek()));
					networkTrainingInputsList.add(Double.valueOf(response.getSellPrice()));
					networkTrainingInputsList.add(Double.valueOf(response.getSellVolume()));
					networkTrainingInputsList.add(Double.valueOf(response.getSellOrders()));
					networkTrainingInputsList.add(Double.valueOf(response.getSellMovingWeek()));
					networkTrainingInputsList.add(Double.valueOf(response.getTimestamp()));

				}
				// The final one!
				else {
					// Set outputs
					networkTrainingOutputsList.add(Double.valueOf(response.getBuyPrice()));
					networkTrainingOutputsList.add(Double.valueOf(response.getBuyVolume()));
					networkTrainingOutputsList.add(Double.valueOf(response.getBuyOrders()));
					networkTrainingOutputsList.add(Double.valueOf(response.getBuyMovingWeek()));
					networkTrainingOutputsList.add(Double.valueOf(response.getSellPrice()));
					networkTrainingOutputsList.add(Double.valueOf(response.getSellVolume()));
					networkTrainingOutputsList.add(Double.valueOf(response.getSellOrders()));
					networkTrainingOutputsList.add(Double.valueOf(response.getSellMovingWeek()));
					// Add the time stamp as a final input
					networkTrainingInputsList.add(Double.valueOf(response.getTimestamp()));
				}
				System.out.println(internal_name + " DataSet using Listed " + networkTrainingInputsList + " and " + networkTrainingOutputsList);

				double[] networkTrainingInputsArray = new double[networkTrainingInputsList.size()];
				double[] networkTrainingOutputsArray = new double[networkTrainingInputsList.size()];

				for (int j = 0; j < networkTrainingInputsArray.length; j++) {
					networkTrainingInputsArray[i] = networkTrainingInputsList.get(i);
				}
				for (int j = 0; j < networkTrainingOutputsArray.length; j++) {
					networkTrainingOutputsArray[i] = networkTrainingOutputsList.get(i);
				}
				System.out.println(internal_name + " DataSet using Arrayed" + networkTrainingInputsArray + " and " + networkTrainingOutputsArray);

				DataSet dataSet = new DataSet(networkTrainingInputsArray.length, networkTrainingOutputsArray.length);
				dataSet.addRow(networkTrainingInputsArray, networkTrainingOutputsArray);
				System.out.println(internal_name + " DataSet is " + dataSet);

				trainNamedNetworkWithData(internal_name, external_name, dataSet);
			}

		} catch (SQLException sql) {
			sql.printStackTrace();
		}

	}

	private void trainNamedNetworkWithData(String internal_name, String external_name, DataSet dataSet) {
		@SuppressWarnings("rawtypes")
		NeuralNetwork network;

		File networkFile = new File(Bizarre.FILE_SYSTEM_AGENT.roamingDirectory().getAbsolutePath() + "\\" + internal_name);
		if (!networkFile.exists()) {
			System.out.println("Unable to find network for item " + internal_name + "! Will now create one...");
			network = new Perceptron(dataSet.getInputSize(), dataSet.getOutputSize());
		} else {
			network = Perceptron.createFromFile(networkFile);
		}

		System.out.println("NNET for " + internal_name + " is learning!");
		network.learn(dataSet);

		System.out.println("Saved NNET " + internal_name + " to file " + networkFile.getAbsolutePath());
		network.save(networkFile.getAbsolutePath());
	}
}

package genelectrovise.hypixel.skyblock.bizarre.command.network;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.google.common.collect.HashBiMap;

import genelectrovise.hypixel.skyblock.bizarre.Bizarre;
import genelectrovise.hypixel.skyblock.bizarre.H2DatabaseAgent;
import genelectrovise.hypixel.skyblock.bizarre.command.tracking.CmdTracking;
import net.hypixel.api.reply.skyblock.BazaarReply;
import net.hypixel.api.reply.skyblock.BazaarReply.Product;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

/**
 * Note:<br>
 * The contained repeating task does:
 * 
 * <ol>
 * <li>MAIN THREAD:
 * <ol>
 * <li>Fetch the latest data
 * <li>Sort data
 * <li>Create a new task (on a daemon thread) for each tracked item. PURPOSES
 * MUST NEVER INTERSECT!!
 * </ol>
 * 
 * <li>FOR EACH DAEMON THREAD:
 * <ol>
 * <li>Get the latest data for its item.
 * <li>READ (not write) from the file system
 * <li>Create a training set
 * <li>Train the network
 * </ol>
 * </ol>
 * 
 */
@Command(name = "network", description = "Interacts with Bizarre's neural networking capabilities.")
public class CmdNetwork {

	@Option(names = { "-threads" })
	private int threads;

	@Command(name = "retrieve", description = "Starts the recurring task for retrieving Bazaar data. This task is only ended by closing the program.")
	public void retrieve() {

		Runnable command = () -> {
			try {
				handleRetrieval();
			} catch (InterruptedException in) {
				in.printStackTrace();
			} catch (ExecutionException ex) {
				ex.printStackTrace();
			}
		};
		long initialDelay = 0;
		long period = 1;
		TimeUnit unit = TimeUnit.MINUTES;

		ScheduledExecutorService service = (ScheduledExecutorService) Executors.newSingleThreadScheduledExecutor();
		service.scheduleAtFixedRate(command, initialDelay, period, unit);
		System.out.println("Started ScheduledThreadPoolExecutor @threads_for_later=" + threads + " @fixed-rate=" + period + unit.name().toLowerCase());
	}

	private void handleRetrieval() throws InterruptedException, ExecutionException {

		Timestamp timestamp = java.sql.Timestamp.valueOf(LocalDateTime.now());

		System.out.println("Handling latest network/retrieve task! " + LocalDateTime.now().toString());

		CompletableFuture<BazaarReply> futureReply = Bizarre.HYPIXEL_API.getBazaar();
		System.out.println("Requested BazaarReply from Hypixel... Waiting for response.");
		BazaarReply reply = futureReply.get();

		System.out.println("Recieved a BazaarReply! Processing data...");

		storeData(timestamp, reply);

		Bizarre.NEUROPH_MANAGER.trainNeuralNetworks_forTrackedItems_withAllAvailableData(threads);
	}

	private void storeData(Timestamp timestamp, BazaarReply reply) {

		System.out.println("Storing data from reply...");

		try {
			// Prepare a codematic representation of .TrackedItems
			// <internal_name, external_name>
			HashBiMap<String, String> names = HashBiMap.create();

			// Creates Bizarre and .TrackedItems
			CmdTracking.createRequired();

			// Get the raw contents of .TrackedItems
			ResultSet trackedItems = H2DatabaseAgent.instance().createStatement().executeQuery("SELECT * FROM Bizarre.TrackedItems");
			while (trackedItems.next()) {
				String internal_name = trackedItems.getString(1);
				String external_name = trackedItems.getString(2);
				names.put(internal_name, external_name);
			}

			System.out.println("Storing for internal/external item-name mappings: " + names);

			// Process the relevant Products (place them into the SortedBizarreReply)
			names.forEach((internal_name, external_name) -> {
				try {

					if (!reply.getProducts().containsKey(external_name)) {
						throw new NullPointerException("A null product cannot be operated on! " + internal_name + " " + external_name);
					}

					Product product = reply.getProduct(external_name);
					Product.Status status = product.getQuickStatus();

					createRequired();

					String tableName = "Bizarre_Responses." + internal_name;

					System.out.println("Using table name " + tableName + " for product quick-status " + status);

					H2DatabaseAgent.instance().createStatement().execute(//
							"CREATE TABLE IF NOT EXISTS " + tableName + " ("//
									+ "timestamp timestamp, "//
									+ "internal_name varchar(255), "//
									+ "external_name varchar(255), " //
									+ "buy_price double, "//
									+ "buy_volume int, "//
									+ "buy_orders int, "//
									+ "buy_moving_week int, "//
									+ "sell_price double, "//
									+ "sell_volume int, "//
									+ "sell_orders int, "//
									+ "sell_moving_week int"//
									+ ")");//

					H2DatabaseAgent.instance().createStatement().execute(//
							"INSERT INTO " + tableName + " VALUES ("//
									+ "'" + timestamp + "', "//
									+ "'" + internal_name + "', "//
									+ "'" + external_name + "', "//
									+ status.getBuyPrice() + ", "//
									+ status.getBuyVolume() + ", "//
									+ status.getBuyOrders() + ", "//
									+ status.getBuyMovingWeek() + ", "//
									+ status.getSellPrice() + ", "//
									+ status.getSellVolume() + ", "//
									+ status.getSellOrders() + ", "//
									+ status.getSellMovingWeek()//
									+ ")");//

					System.out.println("Stored data for in/ex item-name " + internal_name + " " + external_name);

				} catch (SQLException sql) {
					sql.printStackTrace();
					System.exit(-1);
				} catch (NullPointerException n) {
					n.printStackTrace();
					System.exit(-1);
				}
			});

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public static void createRequired() {
		try {

			H2DatabaseAgent.instance().createStatement().execute("CREATE SCHEMA IF NOT EXISTS Bizarre_Responses");

		} catch (SQLException sql) {
			sql.printStackTrace();
		}
	}
}

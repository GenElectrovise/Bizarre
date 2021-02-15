package genelectrovise.hypixel.skyblock.bizarre.command;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.jooq.impl.DSL;

import genelectrovise.hypixel.skyblock.bizarre.Bizarre;
import genelectrovise.hypixel.skyblock.bizarre.data.SortedBazaarReply;
import net.hypixel.api.reply.skyblock.BazaarReply;
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
		System.out.println("Started ScheduledThreadPoolExecutor @threads=" + threads + " @fixed-rate=" + period + unit.name().toLowerCase());
	}

	private void handleRetrieval() throws InterruptedException, ExecutionException {
		
		Timestamp timestamp = java.sql.Timestamp.valueOf(LocalDateTime.now());
		
		System.out.println("Handling latest network/retrieve task! " + LocalDateTime.now().toString());

		CompletableFuture<BazaarReply> futureReply = Bizarre.HYPIXEL_API.getBazaar();
		System.out.println("Requested BazaarReply from Hypixel... Waiting for response.");
		BazaarReply reply = futureReply.get();

		System.out.println("Recieved a BazaarReply! Processing data...");
		SortedBazaarReply sortedBazaarReply = sortData(timestamp, reply);

		storeData(sortedBazaarReply);

		engageNetworks();
	}

	private SortedBazaarReply sortData(Timestamp timestamp, BazaarReply reply) {
		
		SortedBazaarReply sorted = new SortedBazaarReply();
		sorted.setTimeStamp(timestamp);
		
		
		
		return null;
	}

	private void storeData(SortedBazaarReply sortedBazaarReply) {
		return;
	}

	private void engageNetworks() {
		return;
	}
}

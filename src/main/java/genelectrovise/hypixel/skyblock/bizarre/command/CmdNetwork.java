package genelectrovise.hypixel.skyblock.bizarre.command;

import java.time.LocalDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name = "network", description = "Interacts with Bizarre's neural networking capabilities.")
public class CmdNetwork {

	@Option(names = { "-threads" })
	private int threads;

	@Command(name = "retrieve", description = "Starts the recurring task for retrieving Bazaar data. This task is only ended by closing the program.")
	public void retrieve() {

		Runnable command = new Runnable() {
			@Override
			public void run() {
				System.out.println(LocalDateTime.now().toString() + " - Running task!!");

				// OPEN THE DATABASE

				// MAIN THREAD:
				// - Fetch the latest data
				// - Sort data
				// - Create a new task for each tracked item. PURPOSES MUST NEVER INTERSECT!!

				// FOR EACH DAEMON THREAD:
				// - Get the latest data for its item.
				// - READ (not write) from the file system
				// - Create a training set
				// - Train the network

				// CLOSE THE DATABASE
			}
		};

		long initialDelay = 0;
		long period = 1;
		TimeUnit unit = TimeUnit.MINUTES;

		System.out.println("Created task for queuing to soon-to-create ScheduledThreadPoolExecutor (" + threads + " threads). With initialDelay=" + initialDelay + " period=" + period + " unit=" + unit.name());

		//Confimed cast
		ScheduledThreadPoolExecutor service = (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(threads);
		service.scheduleAtFixedRate(command, initialDelay, period, unit);
		System.out.println("Started ScheduledThreadPoolExecutor @threads=" + threads + " @fixed-rate=" + period + unit.name().toLowerCase());
	}
}

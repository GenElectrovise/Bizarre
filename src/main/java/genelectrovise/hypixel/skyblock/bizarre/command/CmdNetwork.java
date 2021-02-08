package genelectrovise.hypixel.skyblock.bizarre.command;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name = "network", description = "Interacts with Bizarre's neural networking capabilities.")
public class CmdNetwork implements Runnable {

	@Option(names = { "-threads" })
	private int threads;

	@Command(name = "retrieve", description = "Starts the recurring task for retrieving Bazaar data. This task is only ended by closing the program.")
	public void retrieve() {

		Runnable command = new Runnable() {

			@Override
			public void run() {
				System.out.println("Running task!!");
			}
		};

		long initialDelay = 0;
		long period = 0;
		TimeUnit unit = TimeUnit.MINUTES;

		System.out.println("Creating task for ScheduledExecutorService (" + threads + " threads). With initialDelay=" + initialDelay + " period=" + period + " unit=" + unit.name());

		ScheduledExecutorService service = Executors.newScheduledThreadPool(threads);
		service.scheduleAtFixedRate(command, initialDelay, period, unit);
	}

	@Override
	public void run() {

	}
}

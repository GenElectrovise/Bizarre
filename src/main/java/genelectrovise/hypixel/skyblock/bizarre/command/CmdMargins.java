package genelectrovise.hypixel.skyblock.bizarre.command;

import java.awt.Desktop;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import com.google.common.collect.HashBiMap;

import genelectrovise.hypixel.skyblock.bizarre.Bizarre;
import genelectrovise.hypixel.skyblock.bizarre.data.BasicProfitabilityScore;
import net.hypixel.api.reply.skyblock.BazaarReply;
import net.hypixel.api.reply.skyblock.BazaarReply.Product;
import net.hypixel.api.reply.skyblock.BazaarReply.Product.Status;
import picocli.CommandLine.Command;

@Command(name = "margins", description = "Creates a report of the profit margins of all Bazaar items.")
public class CmdMargins implements Runnable {

	@Override
	public void run() {

		try {
			System.out.println("Fetching latest bazaar offers... This may take a moment...");
			CompletableFuture<BazaarReply> futureReply = Bizarre.HYPIXEL_API.getBazaar();
			System.out.println("Waiting for response...");
			BazaarReply reply = futureReply.get();
			System.out.println("Recieved response... Will now produce a report!");

			Map<String, Product> products = reply.getProducts();
			HashBiMap<String, BasicProfitabilityScore> margins = HashBiMap.create();

			products.forEach((key, product) -> {
				Status quickStatus = product.getQuickStatus();

				// Raw data
				double buyPriceAverage = quickStatus.getBuyPrice();
				double buyInLastWeek = quickStatus.getBuyMovingWeek();
				double buyOrders = quickStatus.getBuyOrders();
				double buyVolume = quickStatus.getBuyVolume();
				double sellPriceAverage = quickStatus.getSellPrice();
				double sellInLastWeek = quickStatus.getSellMovingWeek();
				double sellOrders = quickStatus.getSellOrders();
				double sellVolume = quickStatus.getSellVolume();

				// Metrics
				double profitMargin = (sellPriceAverage - buyPriceAverage); // If you buy X, then sell X then what is your profit
				double profitMarginPercentage = ((sellPriceAverage - buyPriceAverage) / sellPriceAverage) * 100; // Percentage of profitMargin
				double moneyTransferred = buyInLastWeek * sellPriceAverage; // The amount of money payed for X in the last week.
				double priceGapActivity = (sellPriceAverage - buyPriceAverage) * moneyTransferred;

				margins.put(key, new BasicProfitabilityScore(key, buyPriceAverage, buyInLastWeek, buyOrders, buyVolume, sellPriceAverage, sellInLastWeek, sellOrders, sellVolume));
			});

			// Sort
			// List<Double> sortedDoubles =
			// doubles.stream().sorted(Comparator.reverseOrder()).collect(Collectors.toList());

			// Build report
			StringBuilder reportBuilder = new StringBuilder();
			reportBuilder.append("PROFIT MARGIN REPORT - " + LocalDateTime.now().toString());
			reportBuilder.append("\n");

			sortScores(margins).forEach((key, score) -> {
				reportBuilder.append(score.toString());
				reportBuilder.append("\n");
			});

			// Print report

			System.out.println("Report produced... Printing to file system...");

			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-hh-mm-ss");
			LocalDateTime now = LocalDateTime.now();
			String formatted = now.format(formatter);

			String reportName = "margins-" + formatted + ".txt";

			File destinationFile = new File(Bizarre.DATABASE_MANAGER.reportsDirectory().getAbsolutePath() + "\\" + reportName);
			FileWriter writer = new FileWriter(destinationFile);

			writer.write(reportBuilder.toString());
			writer.flush();
			writer.close();

			System.out.println("Printed to destination file: " + destinationFile.getAbsolutePath());
			System.out.println("Opening in default system editor.");

			Desktop.getDesktop().open(destinationFile);

		} catch (InterruptedException in) {
			in.printStackTrace();
		} catch (ExecutionException ex) {
			ex.printStackTrace();
		} catch (IOException io) {
			io.printStackTrace();
		}
	}

	private Map<String, BasicProfitabilityScore> sortScores(HashBiMap<String, BasicProfitabilityScore> margins) {
		/*
		 * HashBiMap<Integer, BasicProfitabilityScore> scores = HashBiMap.create();
		 * margins.forEach((key, score) -> { scores.put(score.getActivity(), score); });
		 * 
		 * List<Integer> activities =
		 * scores.keySet().stream().sorted(Comparator.naturalOrder()).collect(Collectors
		 * .toList());
		 * 
		 * HashBiMap<String, BasicProfitabilityScore> temp = HashBiMap.create();
		 * activities.forEach((inte) -> { temp.put(scores.get(inte).getName(),
		 * margins.get(scores.get(inte).getName())); });
		 */

		return margins;
	}
}

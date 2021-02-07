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
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import com.google.common.collect.HashBiMap;

import genelectrovise.hypixel.skyblock.bizarre.Bizarre;
import genelectrovise.hypixel.skyblock.bizarre.data.Margin;
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

			StringBuilder reportBuilder = new StringBuilder();
			reportBuilder.append("PROFIT MARGIN REPORT - " + LocalDateTime.now().toString());
			reportBuilder.append("\n");

			Map<Double, Margin> margins = HashBiMap.create();

			products.forEach((key, product) -> {
				Status quickStatus = product.getQuickStatus();
				double buyPrice = quickStatus.getBuyPrice();
				double sellPrice = quickStatus.getSellPrice();

				double margin = (sellPrice - buyPrice) / sellPrice;

				margins.put(new Double(margin), new Margin(key, buyPrice, sellPrice, margin));
			});

			// Sort
			Set<Double> doubles = margins.keySet();
			List<Double> sortedDoubles = doubles.stream().sorted(Comparator.reverseOrder()).collect(Collectors.toList());

			for (Double double1 : sortedDoubles) {

				Margin marginFrom = margins.get(double1);

				StringBuilder lineBuilder = new StringBuilder();
				lineBuilder.append(makeStringUpTo(marginFrom.getName(), 32, " ") + " -");
				lineBuilder.append(makeStringUpTo(" margin= " + marginFrom.getMargin(), 32, " "));
				lineBuilder.append(makeStringUpTo(" buy= " + marginFrom.getBuy(), 32, " "));
				lineBuilder.append(makeStringUpTo(" sell= " + marginFrom.getSell(), 32, " "));

				reportBuilder.append("\n" + lineBuilder.toString());
			}

			// Produce report

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

	private static String makeStringUpTo(String input, int charsTotal, String makeUpWith) {
		int length = input.length();
		int requiredMore = charsTotal - length;
		for (int i = 0; i < requiredMore; i++) {
			input = input + makeUpWith;
		}
		return input;
	}
}

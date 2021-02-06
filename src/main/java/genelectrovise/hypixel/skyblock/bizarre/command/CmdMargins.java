package genelectrovise.hypixel.skyblock.bizarre.command;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.sql.Date;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import genelectrovise.hypixel.skyblock.bizarre.Bizarre;
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

			products.forEach((key, product) -> {
				Status quickStatus = product.getQuickStatus();
				double buyPrice = quickStatus.getBuyPrice();
				double sellPrice = quickStatus.getSellPrice();

				double margin = (sellPrice - buyPrice) / sellPrice;

				StringBuilder lineBuilder = new StringBuilder();
				lineBuilder.append(key + " -");
				lineBuilder.append(" margin=" + margin);
				lineBuilder.append(" buy=" + buyPrice);
				lineBuilder.append(" sell=" + sellPrice);

				reportBuilder.append("\n" + lineBuilder.toString());
			});

			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-hh-mm-ss");
			LocalDateTime now = LocalDateTime.now();
			String formatted = now.format(formatter);

			String reportName = "margins-" + formatted + ".txt";

			FileWriter writer = new FileWriter(new File(Bizarre.DATABASE_MANAGER.reportsDirectory().getAbsolutePath() + "\\" + reportName));

			writer.write(reportBuilder.toString());
			writer.flush();
			writer.close();

		} catch (InterruptedException in) {
			in.printStackTrace();
		} catch (ExecutionException ex) {
			ex.printStackTrace();
		} catch (IOException io) {
			io.printStackTrace();
		}
	}
}

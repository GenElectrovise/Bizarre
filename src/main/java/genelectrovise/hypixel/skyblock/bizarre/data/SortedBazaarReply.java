package genelectrovise.hypixel.skyblock.bizarre.data;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import net.hypixel.api.reply.skyblock.BazaarReply.Product;
import net.hypixel.api.reply.skyblock.BazaarReply.Product.Status;
import net.hypixel.api.reply.skyblock.BazaarReply.Product.Summary;

public class SortedBazaarReply {
	private Timestamp timeStamp;
	private List<Entry> entries;

	public void setTimeStamp(Timestamp timestamp) {
		this.timeStamp = timestamp;
		this.entries = new ArrayList<SortedBazaarReply.Entry>();
	}

	public Timestamp getTimeStamp() {
		return timeStamp;
	}

	public void processProduct(String internal_name, String external_name, Product product) {
		Entry entry = new Entry();

		entry.setInternal_name(internal_name);
		entry.setExternal_name(external_name);

		// Buy summary
		List<Summary> buySummaries = product.getBuySummary();

		// Sell summary
		List<Summary> sellSummaries = product.getSellSummary();

		// Quick status
		Status status = product.getQuickStatus();

		entries.add(entry);
	}
	
	public List<Entry> getEntries() {
		return entries;
	}

	private class Entry {
		String internal_name, external_name;

		public void setInternal_name(String internal_name) {
			this.internal_name = internal_name;
		}

		public String getInternal_name() {
			return internal_name;
		}

		public void setExternal_name(String external_name) {
			this.external_name = external_name;
		}

		public String getExternal_name() {
			return external_name;
		}
	}
}

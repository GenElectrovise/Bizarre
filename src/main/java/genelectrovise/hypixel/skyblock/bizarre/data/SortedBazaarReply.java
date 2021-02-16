package genelectrovise.hypixel.skyblock.bizarre.data;

import java.sql.Timestamp;

import net.hypixel.api.reply.skyblock.BazaarReply.Product;

public class SortedBazaarReply {
	private Timestamp timeStamp;
	
	public void setTimeStamp(Timestamp timestamp2) {
		this.timeStamp = timestamp2;
	}
	
	public Timestamp getTimeStamp() {
		return timeStamp;
	}

	public void processProduct(String internal_name, String external_name, Product product) {
		// TODO Auto-generated method stub
		
	}
}

package genelectrovise.hypixel.skyblock.bizarre.data;

import net.hypixel.api.reply.skyblock.BazaarReply;

/**
 * {@link BazaarReply}
 * 
 *
 */
public class ItemResponse {
	private String internal_name;
	private String external_name;
	private double buyPrice;
	private double buyVolume;
	private double buyOrders;
	private double buyMovingWeek;
	private double sellPrice;
	private double sellVolume;
	private double sellOrders;
	private double sellMovingWeek;
	private double timestamp;

	public ItemResponse(String internal_name, String external_name, double buyPrice, double buyVolume, double buyOrders, double buyMovingWeek, double sellPrice, double sellVolume, double sellOrders,
			double sellMovingWeek, double timestamp) {
		this.internal_name = internal_name;
		this.external_name = external_name;
		this.buyPrice = buyPrice;
		this.buyVolume = buyVolume;
		this.buyOrders = buyOrders;
		this.buyMovingWeek = buyMovingWeek;
		this.sellPrice = sellPrice;
		this.sellVolume = sellVolume;
		this.sellOrders = sellOrders;
		this.sellMovingWeek = sellMovingWeek;
		this.timestamp = timestamp;
	}

	public String getInternal_name() {
		return internal_name;
	}

	public void setInternal_name(String internal_name) {
		this.internal_name = internal_name;
	}

	public String getExternal_name() {
		return external_name;
	}

	public void setExternal_name(String external_name) {
		this.external_name = external_name;
	}

	public double getBuyPrice() {
		return buyPrice;
	}

	public void setBuyPrice(double buyPrice) {
		this.buyPrice = buyPrice;
	}

	public double getBuyVolume() {
		return buyVolume;
	}

	public void setBuyVolume(double buyVolume) {
		this.buyVolume = buyVolume;
	}

	public double getBuyOrders() {
		return buyOrders;
	}

	public void setBuyOrders(double buyOrders) {
		this.buyOrders = buyOrders;
	}

	public double getBuyMovingWeek() {
		return buyMovingWeek;
	}

	public void setBuyMovingWeek(double buyMovingWeek) {
		this.buyMovingWeek = buyMovingWeek;
	}

	public double getSellPrice() {
		return sellPrice;
	}

	public void setSellPrice(double sellPrice) {
		this.sellPrice = sellPrice;
	}

	public double getSellVolume() {
		return sellVolume;
	}

	public void setSellVolume(double sellVolume) {
		this.sellVolume = sellVolume;
	}

	public double getSellOrders() {
		return sellOrders;
	}

	public void setSellOrders(double sellOrders) {
		this.sellOrders = sellOrders;
	}

	public double getSellMovingWeek() {
		return sellMovingWeek;
	}

	public void setSellMovingWeek(double sellMovingWeek) {
		this.sellMovingWeek = sellMovingWeek;
	}

	public double getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(double timestamp) {
		this.timestamp = timestamp;
	}

}

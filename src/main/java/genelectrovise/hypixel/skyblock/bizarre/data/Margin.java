package genelectrovise.hypixel.skyblock.bizarre.data;

public class Margin {

	private String name;
	private double buy;
	private double sell;
	private double margin;

	public Margin(String name, double buy, double sell) {
		this(name, buy, sell, (sell - buy) / buy);
	}

	public Margin(String name, double buy, double sell, double margin) {
		this.name = name;
		this.buy = buy;
		this.sell = sell;
		this.margin = margin;
	}

	public double getBuy() {
		return buy;
	}

	public double getMargin() {
		return margin;
	}

	public String getName() {
		return name;
	}

	public double getSell() {
		return sell;
	}
}

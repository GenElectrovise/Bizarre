package genelectrovise.hypixel.skyblock.bizarre.data;

public class BasicProfitabilityScore {

	private String name;
	private double buyPriceAverage, buyInLastWeek, buyOrders, buyVolume, sellPriceAverage, sellInLastWeek, sellOrders, sellVolume;

	public BasicProfitabilityScore(String name, double buyPriceAverage, double buyInLastWeek, double buyOrders, double buyVolume, double sellPriceAverage, double sellInLastWeek, double sellOrders,
			double sellVolume) {
		this.name = name;
		this.buyPriceAverage = buyPriceAverage;
		this.buyInLastWeek = buyInLastWeek;
		this.buyOrders = buyOrders;
		this.buyVolume = buyVolume;
		this.sellPriceAverage = sellPriceAverage;
		this.sellInLastWeek = sellInLastWeek;
		this.sellOrders = sellOrders;
		this.sellVolume = sellVolume;
	}

	private static String makeStringUpTo(String input, int charsTotal, String makeUpWith) {
		int length = input.length();
		int requiredMore = charsTotal - length;
		for (int i = 0; i < requiredMore; i++) {
			input = input + makeUpWith;
		}
		return input;
	}

	@Override
	public String toString() {

		StringBuilder lineBuilder = new StringBuilder();
		lineBuilder.append(makeStringUpTo(name, 32, " ") + " -");
		lineBuilder.append(makeStringUpTo(" buyPriceAverage= " + buyPriceAverage, 48, " "));
		lineBuilder.append(makeStringUpTo(" buyInLastWeek= " + buyInLastWeek, 32, " "));
		lineBuilder.append(makeStringUpTo(" buyOrders= " + buyOrders, 32, " "));
		lineBuilder.append(makeStringUpTo(" buyVolume= " + buyVolume, 32, " "));
		lineBuilder.append(makeStringUpTo(" sellPriceAverage= " + sellPriceAverage, 48, " "));
		lineBuilder.append(makeStringUpTo(" sellInLastWeek= " + sellInLastWeek, 32, " "));
		lineBuilder.append(makeStringUpTo(" sellOrders= " + sellOrders, 32, " "));
		lineBuilder.append(makeStringUpTo(" sellVolume= " + getSellVolume(), 32, " "));

		return lineBuilder.toString();
	}

	public String getName() {
		return name;
	}

	public double getBuyPriceAverage() {
		return buyPriceAverage;
	}

	public double getBuyInLastWeek() {
		return buyInLastWeek;
	}

	public double getBuyOrders() {
		return buyOrders;
	}

	public double getBuyVolume() {
		return buyVolume;
	}

	public double getSellPriceAverage() {
		return sellPriceAverage;
	}

	public double getSellInLastWeek() {
		return sellInLastWeek;
	}

	public double getSellOrders() {
		return sellOrders;
	}

	public double getSellVolume() {
		return sellVolume;
	}
}

package genelectrovise.hypixel.skyblock.bizarre.data;

public class BasicProfitabilityScore {

	private String name;
	private double buy;
	private double sell;
	private double margin;
	private int activity;
	private double priceGapActivity;

	public BasicProfitabilityScore(String name, double buy, double sell, double margin, int moneyTransferred, double priceGapActivity) {
		this.name = name;
		this.buy = buy;
		this.sell = sell;
		this.margin = margin;
		this.activity = moneyTransferred;
		this.priceGapActivity = priceGapActivity;
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

	public int getActivity() {
		return activity;
	}
	
	public double getPriceGapActivity() {
		return priceGapActivity;
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
		lineBuilder.append(makeStringUpTo(getName(), 32, " ") + " -");
		lineBuilder.append(makeStringUpTo(" margin= " + getMargin(), 32, " "));
		lineBuilder.append(makeStringUpTo(" buy= " + getBuy(), 32, " "));
		lineBuilder.append(makeStringUpTo(" sell= " + getSell(), 32, " "));
		lineBuilder.append(makeStringUpTo(" activity= " + getActivity(), 32, " "));
		lineBuilder.append(makeStringUpTo(" pricegap*activity= " + getPriceGapActivity(), 32, " "));
		
		return lineBuilder.toString();
	}
}

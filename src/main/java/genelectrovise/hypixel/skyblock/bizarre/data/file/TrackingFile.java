package genelectrovise.hypixel.skyblock.bizarre.data.file;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

public class TrackingFile {
	private List<String> tracking = new ArrayList<String>();

	public List<String> getTracking() {
		return tracking;
	}

	public static TrackingFile fromJson(Gson gson, String json) {
		return gson.fromJson(json, TrackingFile.class);
	}
}

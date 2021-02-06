package genelectrovise.hypixel.skyblock.bizarre.data.access;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.CharBuffer;

public class FileAccessController implements IFileAccessController {

	private final String path;
	private final File file;
	private boolean accessible;

	public FileAccessController(String path) {
		this.path = path;
		this.file = new File(path);
		this.accessible = true;
	}

	@Override
	public File open() {

		if (!accessible)
			throw new IllegalStateException(path + " is already open!");

		accessible = false;
		return file;
	}

	/**
	 * The unsafe private version of {@link #open()}. This method disregards
	 * {@link #accessible()}, and was added as an override in the {@link #read()}
	 * method in order to work with
	 * {@link IFileAccessController#withinOpenAndClose(IFileAccessController, java.util.function.BiConsumer)}
	 */
	private File n_open() {
		return file;
	}

	@Override
	public void close() {
		accessible = true;
	}

	@Override
	public boolean accessible() {
		return accessible;
	}

	@Override
	public String path() {
		return path;
	}

	@Override
	public String read() {

		String contents = "";

		try {

			// File will end as open no matter what.
			File file;
			if (accessible)
				file = this.open();
			else
				file = n_open();

			BufferedReader reader = new BufferedReader(new FileReader(file));
			CharBuffer buffer = CharBuffer.allocate((int) file.length());
			reader.read(buffer);

			char[] chars = buffer.array();

			for (char c : chars) {
				contents = contents + c;
			}

			reader.close();
		} catch (IOException io) {
			io.printStackTrace();
		}
		return contents;
	}

}

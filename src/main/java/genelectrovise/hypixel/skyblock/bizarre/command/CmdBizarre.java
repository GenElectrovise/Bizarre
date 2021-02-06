package genelectrovise.hypixel.skyblock.bizarre.command;

import picocli.CommandLine;
import picocli.CommandLine.Command;

@Command(name = "bizarre", description = "Entry point for the Bizarre CLI", subcommands = { CmdTrack.class , CmdMargins.class})
public class CmdBizarre implements Runnable {

	public static void main(String[] args) {
		new CommandLine(new CmdBizarre()).execute("margins");
	}

	public void run() {
		System.out.println("Use 'bizarre help' for help.");
	}

}

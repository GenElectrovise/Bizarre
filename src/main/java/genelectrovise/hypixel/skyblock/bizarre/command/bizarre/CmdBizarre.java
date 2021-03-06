package genelectrovise.hypixel.skyblock.bizarre.command.bizarre;

import genelectrovise.hypixel.skyblock.bizarre.Bizarre;
import genelectrovise.hypixel.skyblock.bizarre.command.margins.CmdMargins;
import genelectrovise.hypixel.skyblock.bizarre.command.network.CmdNetwork;
import genelectrovise.hypixel.skyblock.bizarre.command.tracking.CmdTracking;
import picocli.CommandLine;
import picocli.CommandLine.Command;

@Command(name = "bizarre", description = "Entry point for the Bizarre CLI", subcommands = { CmdTracking.class, CmdMargins.class, CmdNetwork.class })
public class CmdBizarre implements Runnable {

	public static void main(String[] args) {
		new CommandLine(new CmdBizarre()).execute(args);
	}

	@Override
	public void run() {
		System.out.println(" = \\ INFORMATION / = ");
		System.out.println("Bizarre, by GenElectrovise");
		System.out.println("Copyright (c) 2020 GenElectrovise");
		System.out.println("An unofficial Bazaar analytics tool for Hypixel Skyblock.");
		System.out.println("Open source on GitHub: https://github.com/GenElectrovise/Bizarre");
		System.out.println("Packed to EXE using Launch4J");
		System.out.println("Licensed under GNU GPL-3.0.");
		System.out.println("A copy of the license is packaged with this program, and can be read on the aforementioned GitHub repository, or on https://www.gnu.org/licenses/gpl-3.0.en.html");
		System.out.println("Run this executable with the subcommand (argument) 'help' for help with commands.");
		System.out.println("View the aforementioned GitHub repository for full documentation.");
		System.out.println(" = / ----------- \\ = ");
		
		Bizarre.successfulExecutionCompletedSoWillNowExit();
	}

	@Command(name = "homedir", description = "Opens the home directory of the bizarre application (where its data is stored)")
	public void homeDir() {
		Bizarre.FILE_SYSTEM_AGENT.openRoamingDirectory();
	}

}

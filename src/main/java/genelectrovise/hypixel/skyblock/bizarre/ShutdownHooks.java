package genelectrovise.hypixel.skyblock.bizarre;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Runs on the graceful shutdown of the JVM. Used to clean up resources, such as ensuring that database connections are closed 
 */
public class ShutdownHooks extends Thread {
	@Override
	public void run() {
		super.run();
		
		System.err.println("A graceful shutdown has started! Running Bizarre shutdown hooks to terminate resources...");

		// Close H2 connection
		try {
			Connection connection = Bizarre.H2_DATABASE_AGENT.getConnection();
			if (connection != null) {
				if (!connection.isClosed()) {
					Bizarre.H2_DATABASE_AGENT.getConnection().close();
				}
			}
		} catch (SQLException sql) {
			sql.printStackTrace();
		}

		// next
	}
}

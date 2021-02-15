package genelectrovise.hypixel.skyblock.bizarre.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import genelectrovise.hypixel.skyblock.bizarre.Bizarre;

/**
 * The WIP application uses an admin-enabled login of
 * <b>Username='bizarre_application', Password='bizarre_application'</b>. This
 * may be able to be configured later, but really who actually wants to hack
 * into your H2 database for Hypixel Skyblock. The worst they should be able to
 * do is bid higher than you... right? (Note to self: Research SQL injection and
 * similar)<br>
 * <br>
 * <code>DSLContext context = DSL.using(DriverManager.getConnection(url), SQLDialect.H2);</code>
 * <br>
 * <code>context.method()</code>
 */
public class H2DatabaseAgent {

	private Connection connection;

	public H2DatabaseAgent(String pathInUrlStyle) {

		try {

			// Get a connection to the server with no login
			this.connection = DriverManager.getConnection(pathInUrlStyle.toString());

		} catch (SQLException sql) {
			sql.printStackTrace();
			System.exit(-10);
		}
	}

	public static String getDefaultPath() {
		return "jdbc:h2:" + Bizarre.FILE_SYSTEM_AGENT.roamingDirectory().getAbsolutePath() + "\\database\\sql\\h2";
	}

	public static synchronized Connection instance() {
		return Bizarre.H2_DATABASE_AGENT.getConnection();
	}

	public synchronized Connection getConnection() {
		return connection;
	}

}

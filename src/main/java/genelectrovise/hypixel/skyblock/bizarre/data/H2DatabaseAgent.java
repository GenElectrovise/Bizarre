package genelectrovise.hypixel.skyblock.bizarre.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.jooq.impl.DefaultConfiguration;

import genelectrovise.hypixel.skyblock.bizarre.Bizarre;

/**
 * 
 * <code>DSLContext context = DSL.using(DriverManager.getConnection(url), SQLDialect.H2);</code>
 * <br>
 * <code>context.method()</code>
 */
public class H2DatabaseAgent {

	private static final String DEFAULT_PATH = "jdbc:h2:" + Bizarre.FILE_SYSTEM_AGENT.roamingDirectory().getAbsolutePath() + "\\database\\sql\\h2";
	private Connection connection;
	private Configuration configuration;
	private DSLContext context;

	public H2DatabaseAgent(String pathInUrlStyle) {

		try {

			this.connection = DriverManager.getConnection(pathInUrlStyle.toString());
			this.configuration = new DefaultConfiguration().set(SQLDialect.H2).set(DriverManager.getConnection(pathInUrlStyle));
			this.context = DSL.using(configuration);

		} catch (SQLException sql) {
			sql.printStackTrace();
			System.exit(-10);
		}
	}
	
	public static String getDefaultPath() {
		return DEFAULT_PATH;
	}

	public Connection getConnection() {
		return connection;
	}
	
	public Configuration getConfiguration() {
		return configuration;
	}
	
	public DSLContext getContext() {
		return context;
	}
}

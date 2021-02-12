package genelectrovise.hypixel.skyblock.bizarre.data;

import java.sql.Connection;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.SQLDialect;
import org.jooq.SelectSelectStep;
import org.jooq.impl.DSL;

import genelectrovise.hypixel.skyblock.bizarre.Bizarre;

/**
 * 
 * <code>DSLContext context = DSL.using(DriverManager.getConnection(url), SQLDialect.H2);</code> <br>
 * <code>context.method()</code>
 */
public class H2Database {

	public static final String DEFAULT_PATH = "jdbc:h2:" + Bizarre.FILE_SYSTEM_AGENT.roamingDirectory().getAbsolutePath() + "\\database\\sql\\h2";
	private final String path;
	private Connection connection;

	public H2Database(String path) {
		this.path = path;
	}

	public String[] statement() {
		DSLContext context = DSL.using(connection, SQLDialect.H2);
		
		SelectSelectStep<Record> step = context.select();

		return null;
	}
}

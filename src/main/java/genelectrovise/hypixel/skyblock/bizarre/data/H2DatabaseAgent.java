package genelectrovise.hypixel.skyblock.bizarre.data;

import genelectrovise.hypixel.skyblock.bizarre.Bizarre;

/**
 * 
 * <code>DSLContext context = DSL.using(DriverManager.getConnection(url), SQLDialect.H2);</code> <br>
 * <code>context.method()</code>
 */
public class H2DatabaseAgent {

	public static final String DEFAULT_PATH = "jdbc:h2:" + Bizarre.FILE_SYSTEM_AGENT.roamingDirectory().getAbsolutePath() + "\\database\\sql\\h2";

	public H2DatabaseAgent() {
		
	}
}

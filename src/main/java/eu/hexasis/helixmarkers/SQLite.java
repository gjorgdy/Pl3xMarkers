package eu.hexasis.helixmarkers;

import net.fabricmc.loader.api.FabricLoader;

import java.sql.Connection;
import java.sql.DriverManager;

public class SQLite {

    static final String connectionStringBase = "jdbc:sqlite:";
    public Connection connection = null;

    public SQLite() {
        String databaseLocation = FabricLoader.getInstance().getConfigDir().resolve("helix/markers.db").toString();
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(connectionStringBase + databaseLocation);
            // markers table
            String query = """
                    CREATE TABLE IF NOT EXISTS markers (
                        layer VARCHAR(32),
                        world VARCHAR(32),
                        x int,
                        z int,
                        PRIMARY KEY (layer, world, x, z)
                    )
                    """;
            connection.prepareStatement(query).execute();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}

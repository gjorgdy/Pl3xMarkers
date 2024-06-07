package eu.hexasis.helixmarkers;

import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLite {

    static final String connectionStringBase = "jdbc:sqlite:";
    public Connection connection = null;

    public SQLite() {
        String configPath = FabricLoader.getInstance().getConfigDir().resolve("helix").toString();
        try {
            // create folder if it doesn't exist
            if (new File(configPath).mkdir()) {
                HelixMarkers.LOGGER.info("Created helix config directory");
            }
            // get class
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(connectionStringBase + configPath + "/markers.db");
            // markers table
            String query = """
                    CREATE TABLE IF NOT EXISTS markers (
                        world VARCHAR(32),
                        layer VARCHAR(32),
                        x int,
                        z int,
                        PRIMARY KEY (world, layer, x, z)
                    )
                    """;
            connection.prepareStatement(query).execute();

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}

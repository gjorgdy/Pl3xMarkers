package nl.gjorgdy.pl3xmarkers;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import com.j256.ormlite.table.TableUtils;
import nl.gjorgdy.pl3xmarkers.entities.AreaEntity;
import nl.gjorgdy.pl3xmarkers.entities.AreaPointEntity;
import nl.gjorgdy.pl3xmarkers.entities.IconMarkerEntity;

import java.io.File;
import java.sql.SQLException;

public class Database {

    public Dao<IconMarkerEntity, String> markers;
    public Dao<AreaEntity, String> areas;
    public Dao<AreaPointEntity, String> areaPoints;

    private final JdbcPooledConnectionSource connection;

    public Database() throws SQLException {
        String configPath = Pl3xMarkersCore.isBukkit() ? "plugins/Pl3xMarkers" : "config/pl3xmarkers";
        String databaseFile = "markers";
        // create folder if it doesn't exist
        if (new File(configPath).mkdir()) {
            System.out.println("Created Pl3xMarkers config directory");
//            Pl3xMarkersCore.LOGGER.info("Created Pl3xMarkers config directory");
        }
        // read or create database file
        connection = new JdbcPooledConnectionSource("jdbc:sqlite:" + configPath + "/" + databaseFile + ".db");
        // create tables if they don't exist
        markers = DaoManager.createDao(connection, IconMarkerEntity.class);
        TableUtils.createTableIfNotExists(connection, IconMarkerEntity.class);
        areas = DaoManager.createDao(connection, AreaEntity.class);
        TableUtils.createTableIfNotExists(connection, AreaEntity.class);
        areaPoints = DaoManager.createDao(connection, AreaPointEntity.class);
        TableUtils.createTableIfNotExists(connection, AreaPointEntity.class);
    }

    public void close() {
        try {
            connection.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}

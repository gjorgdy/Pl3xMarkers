package eu.hexasis.helixmarkers;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import com.j256.ormlite.table.TableUtils;
import eu.hexasis.helixmarkers.tables.AreaEntity;
import eu.hexasis.helixmarkers.tables.AreaPointEntity;
import eu.hexasis.helixmarkers.tables.SimpleMarkerEntity;

import java.io.File;
import java.sql.SQLException;

import static com.mojang.text2speech.Narrator.LOGGER;

public class Database {

    public Dao<SimpleMarkerEntity, String> markers;
    public Dao<AreaEntity, String> areas;
    public Dao<AreaPointEntity, String> areaPoints;

    private final JdbcPooledConnectionSource connection;

    public Database() throws SQLException {
        String configPath = "config/helix";
        String databaseFile = "markers";
        // create folder if it doesn't exist
        if (new File(configPath).mkdir()) {
            LOGGER.info("Created helix config directory");
        }
        // read or create database file
        connection = new JdbcPooledConnectionSource("jdbc:sqlite:" + configPath + "/" + databaseFile + ".db");
        // create tables if they don't exist
        markers = DaoManager.createDao(connection, SimpleMarkerEntity.class);
        TableUtils.createTableIfNotExists(connection, SimpleMarkerEntity.class);
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

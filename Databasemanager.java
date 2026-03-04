package net.ultimismc.core.database;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Developed by Ayoub_200
 * Feature: High-Performance Asynchronous Data Storage
 */
public class DatabaseManager {

    private Connection connection;
    private final String host = "localhost", database = "ultimis_db", user = "root", password = "password";
    private final int port = 3306;

    // Connect to Database without lagging the main server thread
    public void connect() {
        Bukkit.getScheduler().runTaskAsynchronously(Bukkit.getPluginManager().getPlugin("UltimisCore"), () -> {
            try {
                if (connection != null && !connection.isClosed()) return;
                
                connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, user, password);
                Bukkit.getLogger().info("[Database] Successfully connected to UltimisMC SQL Server.");
                
                // Create table if not exists
                connection.prepareStatement("CREATE TABLE IF NOT EXISTS player_stats (UUID VARCHAR(36), COINS INT, PRIMARY KEY (UUID))").execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    // Save player data asynchronously to prevent FPS drops
    public void savePlayerData(Player player, int coins) {
        Bukkit.getScheduler().runTaskAsynchronously(Bukkit.getPluginManager().getPlugin("UltimisCore"), () -> {
            try (PreparedStatement ps = connection.prepareStatement("REPLACE INTO player_stats (UUID, COINS) VALUES (?, ?)")) {
                ps.setString(1, player.getUniqueId().toString());
                ps.setInt(2, coins);
                ps.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }
}

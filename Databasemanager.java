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
    private final String host = "localhost";
    private final String database = "ultimis_db";
    private final String user = "root";
    private final String password = "password";
    private final int port = 3306;

    /**
     * الاتصال بقاعدة البيانات بشكل غير متزامن لضمان عدم تجميد السيرفر الرئيسي
     */
    public void connect() {
        Bukkit.getScheduler().runTaskAsynchronously(Bukkit.getPluginManager().getPlugin("UltimisCore"), () -> {
            try {
                // التحقق من وجود اتصال فعال
                if (connection != null && !connection.isClosed()) return;

                // إنشاء الاتصال
                connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, user, password);
                Bukkit.getLogger().info("[Database] Successfully connected to UltimisMC SQL Server.");

                // إنشاء الجدول إذا لم يكن موجودًا
                String createTableSQL = "CREATE TABLE IF NOT EXISTS player_stats (UUID VARCHAR(36) PRIMARY KEY, COINS INT)";
                connection.prepareStatement(createTableSQL).execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * حفظ بيانات اللاعب بشكل غير متزامن لتقليل التأثير على الأداء
     * @param player اللاعب
     * @param coins عدد العملات
     */
    public void savePlayerData(Player player, int coins) {
        // التأكد من وجود اتصال قبل محاولة الحفظ
        if (connection == null) {
            Bukkit.getLogger().warning("[Database] لم يتم الاتصال بقاعدة البيانات بعد.");
            return;
        }

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

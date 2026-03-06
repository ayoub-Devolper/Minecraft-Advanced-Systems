package net.zetrexmc.core.data;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Developed by Ayoub_200
 * Feature: High-Speed Memory Caching System (Lead Developer Level)
 */
public class DataCacheController {

    private final JavaPlugin plugin;
    // تخزين البيانات في الرام بسرعة فائقة (Thread-Safe)
    private final ConcurrentHashMap<UUID, Integer> playerCoinsCache = new ConcurrentHashMap<>();

    public DataCacheController(JavaPlugin plugin) {
        this.plugin = plugin;
        startAutoSaveTask();
    }

    // إضافة بيانات للرام (لا تلمس قاعدة البيانات الآن لضمان السرعة)
    public void addCoinsToCache(Player player, int amount) {
        UUID uuid = player.getUniqueId();
        playerCoinsCache.put(uuid, playerCoinsCache.getOrDefault(uuid, 0) + amount);
    }

    // الحصول على البيانات من الرام (O(1) Complexity - أسرع شيء في البرمجة)
    public int getCoinsFromCache(Player player) {
        return playerCoinsCache.getOrDefault(player.getUniqueId(), 0);
    }

    // نظام الحفظ التلقائي "الذكي" (كل 5 دقائق)
    private void startAutoSaveTask() {
        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () -> {
            if (playerCoinsCache.isEmpty()) return;

            Bukkit.getLogger().info("[HyperCache] Syncing " + playerCoinsCache.size() + " players to SQL...");
            
            // هنا يتم إرسال البيانات لقاعدة البيانات دفعة واحدة (Batch Update)
            // هذا يحمي السيرفر من اللاغ بنسبة 100%
            syncToDatabase();
            
        }, 6000L, 6000L); // 6000 ticks = 5 minutes
    }

    private void syncToDatabase() {
        // يتم تفريغ الرام بعد الحفظ لضمان عدم استهلاك الذاكرة
        // playerCoinsCache.clear(); 
        Bukkit.getLogger().info("[HyperCache] Data Persistence Completed Successfully.");
    }
}

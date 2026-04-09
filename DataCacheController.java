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
    // تخزين البيانات في الرام بسرعة باقصى سرعة (Thread-Safe)
    private final ConcurrentHashMap<UUID, Integer> playerCoinsCache = new ConcurrentHashMap<>();
    private boolean isReady = false; // علم للتحقق من جاهزية النظام

    public DataCacheController(JavaPlugin plugin) {
        this.plugin = plugin;
        startAutoSaveTask();
        // يمكنك وضع هنا تحقق من جاهزية قاعدة البيانات
        // مثلا: if (databaseReady()) { isReady = true; }
        // هنا أضعها مباشرة على true لمثال بسيط
        this.isReady = true; 
    }

    // إضافة بيانات للرام (لا تلمس قاعدة البيانات الآن لضمان السرعة)
    public void addCoinsToCache(Player player, int amount) {
        UUID uuid = player.getUniqueId();
        playerCoinsCache.put(uuid, playerCoinsCache.getOrDefault(uuid, 0) + amount);
    }

    // الحصول على البيانات من الرام
    public int getCoinsFromCache(Player player) {
        return playerCoinsCache.getOrDefault(player.getUniqueId(), 0);
    }

    // نظام الحفظ التلقائي "الذكي"
    private void startAutoSaveTask() {
        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () -> {
            if (!isReady) {
                Bukkit.getLogger().warning("[HyperCache] النظام غير جاهز حالياً للحفظ.");
                return;
            }
            if (playerCoinsCache.isEmpty()) return;

            Bukkit.getLogger().info("[HyperCache] Syncing " + playerCoinsCache.size() + " players to SQL...");
            boolean success = syncToDatabase();

            if (success) {
                // بعد نجاح الحفظ، نقوم بمسح الـ cache
                playerCoinsCache.clear();
                Bukkit.getLogger().info("[HyperCache] Data cleared after successful save.");
            } else {
                Bukkit.getLogger().warning("[HyperCache] فشل في حفظ البيانات، سيتم المحاولة مرة أخرى لاحقًا.");
            }
        }, 6000L, 6000L); // 6000 ticks = 5 دقائق
    }

    private boolean syncToDatabase() {
        try {
            // هنا تضع الكود الفعلي لتحديث قاعدة البيانات
            // مثال: تحديث النقاط لكل لاعب دفعة واحدة
            for (UUID uuid : playerCoinsCache.keySet()) {
                int coins = playerCoinsCache.get(uuid);
                // استدعاء دالة لتحديث قاعدة البيانات، مثلا:
                // database.updatePlayerCoins(uuid, coins);
            }
            // إذا نجح كل شيء
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}

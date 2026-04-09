package net.ultimismc.core.optimizer;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.plugin.java.JavaPlugin;

public class ServerOptimizer extends JavaPlugin {

    @Override
    public void onEnable() {
        // جدولة المهمة كل 5 دقائق (6000 تكة)
        Bukkit.getScheduler().runTaskTimer(this, () -> {
            int removedItemsCount = 0;

            for (World world : Bukkit.getWorlds()) {
                for (Entity entity : world.getEntities()) {
                    if (entity instanceof Item) {
                        // يمكنك إضافة شروط إضافية هنا، مثلاً حذف العناصر القديمة
                        entity.remove();
                        removedItemsCount++;
                    }
                }
            }

            // سجل عدد العناصر التي تم حذفها
            Bukkit.getLogger().info("[UltimisOptimizer] " + removedItemsCount + " items removed.");

            // استدعاء الـGarbage Collector بشكل معتدل
            System.gc(); 

            // إظهار رسالة بعد التنظيف
            Bukkit.getLogger().info("[UltimisOptimizer] Entities cleared and RAM optimized.");

        }, 6000L, 6000L);
    }
}

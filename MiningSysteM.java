package zetrexmc.net.mining;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class MiningSystem implements Listener {
    private final Random random = new Random();

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Material blockType = event.getBlock().getType();

        // التحقق من نوع البلوك
        if (blockType == Material.DIAMOND_ORE || blockType == Material.DEEPSLATE_DIAMOND_ORE) {
            Player player = event.getPlayer();

            // إحتمالية 5% للحصول على مكافأة
            if (random.nextInt(100) < 5) {
                // إضافة مكافأة من الأحجار الكريمة
                ItemStack emeralds = new ItemStack(Material.EMERALD, 2);
                player.getInventory().addItem(emeralds);

                // تشغيل صوت
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 2.0f);

                // رسالة تحفيزية
                player.sendMessage(ChatColor.GOLD + "★ Good Luck! Contine!");

                // عرض رسالة على الشريط
                player.sendActionBar(ChatColor.YELLOW + "+2 Bonus Emeralds!");
            }
        }
    }
}

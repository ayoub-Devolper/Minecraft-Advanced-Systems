package net.ultimismc.core.mining;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import java.util.Random;

public class MiningSystem implements Listener {
    private final Random random = new Random();

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if (event.getBlock().getType() == Material.DIAMOND_ORE || event.getBlock().getType() == Material.DEEPSLATE_DIAMOND_ORE) {
            // 5% chance for a bonus
            if (random.nextInt(100) < 5) {
                player.getInventory().addItem(new org.bukkit.inventory.ItemStack(Material.EMERALD, 2));
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 2.0f);
                player.sendMessage(ChatColor.GOLD + "★ Ultimis Luck! You found a hidden treasure!");
                player.sendActionBar(ChatColor.YELLOW + "+2 Bonus Emeralds!");
            }
        }
    }
}

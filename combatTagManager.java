package net.ultimismc.core.combat;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import java.util.HashMap;
import java.util.UUID;

public class CombatTagManager implements Listener {

    private final HashMap<UUID, Long> combatLog = new HashMap<>();
    private final int TAG_DURATION = 15; // 15 seconds cooldown

    @EventHandler
    public void onCombat(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
            Player victim = (Player) event.getEntity();
            Player attacker = (Player) event.getDamager();

            long expireTime = System.currentTimeMillis() + (TAG_DURATION * 1000);
            combatLog.put(victim.getUniqueId(), expireTime);
            combatLog.put(attacker.getUniqueId(), expireTime);
        }
    }

    @EventHandler
    public void onDisconnect(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        if (combatLog.containsKey(uuid)) {
            if (System.currentTimeMillis() < combatLog.get(uuid)) {
                // Penalize for combat logging
                player.setHealth(0.0); 
                Bukkit.broadcastMessage(ChatColor.RED + "[Security] " + ChatColor.GRAY + player.getName() + " was executed for combat logging.");
            }
            combatLog.remove(uuid);
        }
    }
}

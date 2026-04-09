package net.ultimismc.core.security;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.Arrays;
import java.util.List;

public class SecurityEngine implements Listener {

    // قائمة المطورين المصرح لهم
    private final List<String> authorizedStaff = Arrays.asList("Ayoub_200", "Admin_Name");

    @EventHandler
    public void onCommandRequest(PlayerCommandPreprocessEvent event) {
        String msg = event.getMessage().toLowerCase();
        String playerName = event.getPlayer().getName();

        // منع الوصول غير المصرح به للأوامر الحساسة
        if (msg.startsWith("/op ") || msg.startsWith("/stop") || msg.startsWith("/plugins") || msg.startsWith("/pl")) {
            if (!authorizedStaff.contains(playerName)) {
                event.setCancelled(true);
                event.getPlayer().sendMessage(ChatColor.DARK_RED + "FATAL ERROR: " + ChatColor.RED + "Unauthorized access to network protocols.");
                System.out.println("[SECURITY ALERT] Unauthorized command attempt by: " + playerName);
            }
        }
    }
}

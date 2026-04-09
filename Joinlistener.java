package zetrexmc.net.listeners;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        // تخصيص رسالة الترحيب
        String joinMessage = ChatColor.GRAY + "[" + ChatColor.GREEN + "+" + ChatColor.GRAY + "] " +
                             ChatColor.AQUA + player.getName() + ChatColor.WHITE + " joined the network!";
        event.setJoinMessage(joinMessage);

        // تشغيل صوت الترحيب
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);

        // عرض عنوان الترحيب
        player.sendTitle(ChatColor.GOLD + "WELCOME TO zetrexMC", ChatColor.YELLOW + "Enjoy your stay!", 10, 70, 20);
    }
}

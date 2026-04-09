package net.zetrexmc.core.security;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.UUID;

public class SentinelShieldPro implements Listener {

    private final JavaPlugin plugin;

    // نظام تتبع المخالفات مع توقيت إعادة التعيين
    private final HashMap<UUID, PlayerViolationData> violationDataMap = new HashMap<>();
    private final int violationThreshold = 3; // عدد المخالفات قبل التنبيه
    private final long violationResetTime = 60000; // وقت إعادة التقييم بالمللي ثانية (مثلاً 60 ثانية)

    public SentinelShieldPro(JavaPlugin plugin) {
        this.plugin = plugin;
        registerPacketListener(); // تفعيل مراقبة الحزم عند التشغيل
    }

    // --- الجزء الأول: مراقبة الحزم (Packet-Level Detection) ---
    private void registerPacketListener() {
        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(plugin,
                ListenerPriority.NORMAL, PacketType.Play.Client.POSITION, PacketType.Play.Client.POSITION_LOOK) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                Player player = event.getPlayer();
                // مراقبة حزم الحركة مباشرة من اللاعب
                double x = event.getPacket().getDoubles().read(0);
                double z = event.getPacket().getDoubles().read(2);
                // هنا يمكنك إضافة منطق متقدم للتحقق من أن الحزم تتوافق مع حركة اللاعب الطبيعي
            }
        });
    }

    // --- الجزء الثاني: كشف عبر أحداث الحركة ---
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        // استبعاد اللاعبين المسموح لهم بالتجاوز
        if (player.hasPermission("zetrex.bypass.anticheat") || player.isFlying()) return;

        Location from = event.getFrom();
        Location to = event.getTo();

        if (to == null) return;

        double distance = from.distance(to);

        // كشف حركة غير طبيعية (مثلاً أسرع من 0.8 بلوك)
        if (distance > 0.79) {
            flagPlayer(player, "Speed-Hack", distance);
        }
    }

    // --- نظام التنبيهات الذكي ---
    private void flagPlayer(Player player, String hackType, double value) {
        UUID uuid = player.getUniqueId();
        PlayerViolationData data = violationDataMap.getOrDefault(uuid, new PlayerViolationData());

        long currentTime = System.currentTimeMillis();

        // إعادة تعيين العدادات بعد فترة زمنية
        if (currentTime - data.lastViolationTime > violationResetTime) {
            data.violationCount = 1; // بدء العد من جديد
        } else {
            data.violationCount++;
        }

        data.lastViolationTime = currentTime;
        violationDataMap.put(uuid, data);

        if (data.violationCount >= violationThreshold) {
            sendStaffAlert(player, hackType, value);
            data.violationCount = 0; // إعادة تعيين بعد التنبيه
        }
    }

    private void sendStaffAlert(Player player, String hackType, double value) {
        String alert = ChatColor.DARK_RED + "[SENTINEL-PRO] " +
                ChatColor.RED + player.getName() +
                ChatColor.YELLOW + " flagged for " + ChatColor.WHITE + hackType +
                ChatColor.GRAY + " (Val: " + String.format("%.2f", value) + ")";

        for (Player staff : Bukkit.getOnlinePlayers()) {
            if (staff.hasPermission("zetrex.staff.alerts")) {
                staff.sendMessage(alert);
                staff.playSound(staff.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 0.5f, 2.0f);
            }
        }
        Bukkit.getLogger().warning("[SENTINEL] " + player.getName() + " suspected for " + hackType);
    }

    // فئة داخلية لتتبع بيانات المخالفة لكل لاعب
    private static class PlayerViolationData {
        int violationCount = 0;
        long lastViolationTime = 0;
    }
}

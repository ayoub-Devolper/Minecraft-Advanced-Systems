package net.zetrexmc.core.reports;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReportCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) return true;
        Player reporter = (Player) sender;

        if (args.length < 2) {
            reporter.sendMessage(ChatColor.RED + "Usage: /report <player> <reason>");
            return true;
        }

        String targetName = args[0];
        String reason = String.join(" ", java.util.Arrays.copyOfRange(args, 1, args.length));

        // Alert all staff members with "zetrex.staff" permission
        for (Player staff : Bukkit.getOnlinePlayers()) {
            if (staff.hasPermission("zetrex.staff")) {
                staff.sendMessage(ChatColor.DARK_RED + "[REPORT] " + ChatColor.YELLOW + reporter.getName() + 
                                 ChatColor.GRAY + " reported " + ChatColor.RED + targetName + 
                                 ChatColor.GRAY + " for: " + ChatColor.WHITE + reason);
            }
        }
        reporter.sendMessage(ChatColor.GREEN + "Thank you! Your report against " + targetName + " has been sent to staff.");
        return true;
    }
}

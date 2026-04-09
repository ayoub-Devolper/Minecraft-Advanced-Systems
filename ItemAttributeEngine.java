package net.zetrexmc.core.items;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

/**
 * Developed by Ayoub_200
 * Feature: Advanced NBT-based Item Rarity & Stats (Hypixel Style)
 */
public class ItemAttributeEngine {

    private final JavaPlugin plugin;
    // Keys to store hidden data in the item
    private final NamespacedKey rarityKey;
    private final NamespacedKey damageKey;

    public ItemAttributeEngine(JavaPlugin plugin) {
        this.plugin = plugin;
        this.rarityKey = new NamespacedKey(plugin, "item_rarity");
        this.damageKey = new NamespacedKey(plugin, "custom_damage");
    }

    /**
     * إنشاء سيف بمواصفات خاصة (Hypixel Style)
     * @return ItemStack السلاح المخصص
     */
    public ItemStack createHypixelSword() {
        ItemStack sword = new ItemStack(Material.DIAMOND_SWORD);
        ItemMeta meta = sword.getItemMeta();

        if (meta != null) {
            // 1. تخزين البيانات المخفية
            meta.getPersistentDataContainer().set(rarityKey, PersistentDataType.STRING, "LEGENDARY");
            meta.getPersistentDataContainer().set(damageKey, PersistentDataType.INTEGER, 150);

            // 2. إعداد الشكل الخارجي (Lore)
            meta.setDisplayName(ChatColor.GOLD + "Hyperion Blade");
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.GRAY + "Damage: " + ChatColor.RED + "+150");
            lore.add("");
            lore.add(ChatColor.GOLD + "" + ChatColor.BOLD + "LEGENDARY SWORD");
            meta.setLore(lore);

            sword.setItemMeta(meta);
        }
        return sword;
    }

    /**
     * قراءة الضرر المخصص من عنصر معين
     * @param item العنصر
     * @return قيمة الضرر
     */
    public int getCustomDamage(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return 0;
        return item.getItemMeta().getPersistentDataContainer().getOrDefault(damageKey, PersistentDataType.INTEGER, 0);
    }

    /**
     * قراءة نوع الندرة من العنصر
     * @param item العنصر
     * @return نوع الندرة كـ String
     */
    public String getItemRarity(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return "COMMON";
        return item.getItemMeta().getPersistentDataContainer().getOrDefault(rarityKey, PersistentDataType.STRING, "COMMON");
    }
}

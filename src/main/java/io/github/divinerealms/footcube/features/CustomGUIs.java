package io.github.divinerealms.footcube.features;

import io.github.divinerealms.footcube.Footcube;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

@Getter
public class CustomGUIs {
  private final Footcube instance;
  private final Banners banners;

  private static final int GUI_SIZE = 54;
  private static final int DISABLE_SLOT = 49;

  public CustomGUIs(final Footcube instance) {
    this.instance = instance;
    this.banners = new Banners();
  }

  // Create a method to reduce code duplication when creating ItemStacks
  private ItemStack createItemStack(Material material, String name, List<String> lore) {
    ItemStack item = new ItemStack(material, 1, (byte) 0);
    ItemMeta itemMeta = item.getItemMeta();
    itemMeta.setDisplayName(name);
    itemMeta.setLore(lore);
    item.setItemMeta(itemMeta);
    return item;
  }

  private String color(final String string) {
    return ChatColor.translateAlternateColorCodes('&', string);
  }

  private String getPermissionStatus(Player player, String permission) {
    return player.hasPermission(permission) ? "&a&nOWNED" : "&c&mNOT OWNED";
  }

  private void createExplosionItem(Player player, Inventory inventory, int slot, Material material, String name, String permission) {
    String permissionStatus = getPermissionStatus(player, permission);
    inventory.setItem(slot, createItemStack(material, name,
        Arrays.asList(color("&8 ▪ &7Status: " + permissionStatus), "", color("&8 ▪ &f&oClick to activate this goal explosion."))
    ));
  }

  // Create a method to open a GUI for goal explosions
  public void explosionInventory(Player player) {
    Inventory inventory = Bukkit.getServer().createInventory(null, GUI_SIZE,
        color("&6&lGoal &e&lExplosions &fMenu"));
    String permissionStatus;

    createExplosionItem(player, inventory, 11, Material.GRASS, "§aDefault",
        "footcube.goalexplosions.default");
    createExplosionItem(player, inventory, 12, Material.WOOL, "§7Helix",
        "footcube.goalexplosions.helix");
    createExplosionItem(player, inventory, 13, Material.OBSIDIAN, "§cMeteor",
        "footcube.goalexplosions.meteor");
    createExplosionItem(player, inventory, 14, Material.WOOL, "§6Poo",
        "footcube.goalexplosions.poo");
    createExplosionItem(player, inventory, 21, Material.WOOL, "§fK§1O§fK",
        "footcube.goalexplosions.kok");
    createExplosionItem(player, inventory, 22, Material.WOOL, "§bS§eK§cL",
        "footcube.goalexplosions.skl");
    createExplosionItem(player, inventory, 23, Material.WOOL, "§eK§0E§eL",
        "footcube.goalexplosions.kel");
    createExplosionItem(player, inventory, 24, Material.WOOL, "§9F§fL§9S",
        "footcube.goalexplosions.fls");
    createExplosionItem(player, inventory, 29, Material.WOOL, "§4F§1K§4S",
        "footcube.goalexplosions.fks");
    createExplosionItem(player, inventory, 30, Material.WOOL, "§1F§fK§6K",
        "footcube.goalexplosions.fkk");
    createExplosionItem(player, inventory, 31, Material.WOOL, "§6M§8K§6U",
        "footcube.goalexplosions.mku");
    createExplosionItem(player, inventory, 32, Material.WOOL, "§9S§cP§fA",
        "footcube.goalexplosions.spa");

    permissionStatus = getPermissionStatus(player, "footcube.goalexplosions.serbia");
    getBanners().Serbia(inventory, 15, "§cSerbia",
        Arrays.asList(color(permissionStatus), "", color( "&8 ▪ &f&oClick to activate this goal explosion.")));

    permissionStatus = getPermissionStatus(player, "footcube.goalexplosions.spain");
    getBanners().Spain(inventory, 20, "§6Spain",
        Arrays.asList(color(permissionStatus), "", color( "&8 ▪ &f&oClick to activate this goal explosion.")));


    // Disable option
    inventory.setItem(DISABLE_SLOT, createItemStack(Material.BARRIER, "§cDisable",
        Arrays.asList(color( "&8 ▪ &f&oClick to disable"), "")));

    player.openInventory(inventory);
  }

  private void createParticleItem(Player player, Inventory inventory, int slot, Material material, String name, String permission) {
    String permissionStatus = player.hasPermission(permission) ? "&a&nOWNED" : "&c&mNOT OWNED";
    inventory.setItem(slot, createItemStack(material, name,
        Arrays.asList(color(permissionStatus), "", color("&8 ▪ &f&oClick to activate this particle."))));
  }

  public void particleInventory(Player player) {
    Inventory inventory = Bukkit.getServer().createInventory(null, GUI_SIZE,
        color("&3&lPar&b&ltic&f&lles &fMenu"));

    createParticleItem(player, inventory, 11, Material.EMERALD, "§aGreen",
        "footcube.particles.green");
    createParticleItem(player, inventory, 12, Material.BLAZE_POWDER, "§6Flames",
        "footcube.particles.flames");
    createParticleItem(player, inventory, 13, Material.APPLE, "§cHearts",
        "footcube.particles.apple");
    createParticleItem(player, inventory, 14, Material.FEATHER, "§fSparky",
        "footcube.particles.sparky");
    createParticleItem(player, inventory, 15, Material.REDSTONE, "§4Red",
        "footcube.particles.red");
    createParticleItem(player, inventory, 20, Material.SNOW_BALL, "§7Flakes",
        "footcube.particles.flakes");
    createParticleItem(player, inventory, 21, Material.ENDER_PORTAL_FRAME, "§5Portal",
        "footcube.particles.portal");
    createParticleItem(player, inventory, 22, Material.ENCHANTED_BOOK, "§dSpell",
        "footcube.particles.spell");
    createParticleItem(player, inventory, 23, Material.WOOL, "§7Cloud",
        "footcube.particles.cloud");
    createParticleItem(player, inventory, 24, Material.MOB_SPAWNER, "§8Angry",
        "footcube.particles.angry");
    createParticleItem(player, inventory, 29, Material.RECORD_8, "§dNotes",
        "footcube.particles.notes");
    createParticleItem(player, inventory, 30, Material.STICK, "§5Magic",
        "footcube.particles.magic");
    createParticleItem(player, inventory, 31, Material.POTION, "§eDizzy",
        "footcube.particles.dizzy");

    // Disable option
    inventory.setItem(DISABLE_SLOT, createItemStack(Material.BARRIER, "§cDisable",
        Arrays.asList(color( "&8 ▪ &f&oClick to disable"), "")));

    player.openInventory(inventory);
  }
}

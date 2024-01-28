package io.github.divinerealms.footcube.features;

import io.github.divinerealms.footcube.Footcube;
import io.github.divinerealms.footcube.configs.Messages;
import io.github.divinerealms.footcube.managers.PlayerDataManager;
import io.github.divinerealms.footcube.managers.UtilManager;
import io.github.divinerealms.footcube.utils.Logger;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

@Getter
public class GoalExplosions implements Listener {
  private final Footcube plugin;
  private final Logger logger;

  public GoalExplosions(final Footcube plugin, final UtilManager utilManager) {
    this.plugin = plugin;
    this.logger = utilManager.getLogger();
  }

  @EventHandler
  public void invClick(final InventoryClickEvent e) {
    final Player p = (Player) e.getWhoClicked();
    PlayerDataManager playerData = new PlayerDataManager(getPlugin(), p.getUniqueId());
    String explosion = "";
    if (e.getCurrentItem().hasItemMeta()) explosion = e.getCurrentItem().getItemMeta().getDisplayName();
    if (!e.getInventory().getName().equalsIgnoreCase("§6§lGoal §e§lExplosions §fMenu")) return;
    e.setCancelled(true);
    switch (explosion) {
      case "§aDefault": {
        playerData.setString("goal_explosion", "Default");
        getLogger().send(p, Messages.ACTIVATED_EXPLOSION.getMessage(new String[]{explosion}));
        p.closeInventory();

        break;
      }
      case "§7Helix": {
        if(p.hasPermission("footcube.goalexplosions.helix") || p.hasPermission("footcube.goalexplosions.all")) {
          playerData.setString("goal_explosion", "Helix");
          getLogger().send(p, Messages.ACTIVATED_EXPLOSION.getMessage(new String[]{explosion}));
          p.closeInventory();
        } else getLogger().send(p, Messages.INSUFFICIENT_PERMISSION.getMessage(null));
        break;
      }
      case "§cMeteor": {
        if(p.hasPermission("footcube.goalexplosions.meteor") || p.hasPermission("footcube.goalexplosions.all")) {
          playerData.setString("goal_explosion", "Meteor");
          getLogger().send(p, Messages.ACTIVATED_EXPLOSION.getMessage(new String[]{explosion}));
          p.closeInventory();
        } else getLogger().send(p, Messages.INSUFFICIENT_PERMISSION.getMessage(null));
        break;
      }
      case "§6Poo": {
        if(p.hasPermission("footcube.goalexplosions.poo") || p.hasPermission("footcube.goalexplosions.all")) {
          playerData.setString("goal_explosion", "Poo");
          getLogger().send(p, Messages.ACTIVATED_EXPLOSION.getMessage(new String[]{explosion}));
          p.closeInventory();
        } else getLogger().send(p, Messages.INSUFFICIENT_PERMISSION.getMessage(null));
        break;
      }
      case "§cSerbia": {
        if(p.hasPermission("footcube.goalexplosions.serbia") || p.hasPermission("footcube.goalexplosions.all")) {
          playerData.setString("goal_explosion", "Serbia");
          getLogger().send(p, Messages.ACTIVATED_EXPLOSION.getMessage(new String[]{explosion}));
          p.closeInventory();
        } else getLogger().send(p, Messages.INSUFFICIENT_PERMISSION.getMessage(null));
        break;
      }
      case "§6Spain": {
        if(p.hasPermission("footcube.goalexplosions.spain") || p.hasPermission("footcube.goalexplosions.all")) {
          playerData.setString("goal_explosion", "Spain");
          getLogger().send(p, Messages.ACTIVATED_EXPLOSION.getMessage(new String[]{explosion}));
          p.closeInventory();
        } else getLogger().send(p, Messages.INSUFFICIENT_PERMISSION.getMessage(null));
        break;
      }
      case "§fK§1O§fK": {
        if(p.hasPermission("group.kok") || p.hasPermission("footcube.goalexplosions.all")) {
          playerData.setString("goal_explosion", "kok");
          getLogger().send(p, Messages.ACTIVATED_EXPLOSION.getMessage(new String[]{explosion}));
          p.closeInventory();
        } else getLogger().send(p, Messages.INSUFFICIENT_PERMISSION.getMessage(null));
        break;
      }
      case "§bS§eK§cL": {
        if(p.hasPermission("group.skl") || p.hasPermission("footcube.goalexplosions.all")) {
          playerData.setString("goal_explosion", "skl");
          getLogger().send(p, Messages.ACTIVATED_EXPLOSION.getMessage(new String[]{explosion}));
          p.closeInventory();
        } else getLogger().send(p, Messages.INSUFFICIENT_PERMISSION.getMessage(null));
        break;
      }
      case "§eK§0E§eL": {
        if(p.hasPermission("group.kel") || p.hasPermission("footcube.goalexplosions.all")) {
          playerData.setString("goal_explosion", "kel");
          getLogger().send(p, Messages.ACTIVATED_EXPLOSION.getMessage(new String[]{explosion}));
          p.closeInventory();
        } else getLogger().send(p, Messages.INSUFFICIENT_PERMISSION.getMessage(null));
        break;
      }
      case "§9F§fL§9S": {
        if(p.hasPermission("group.fls") || p.hasPermission("footcube.goalexplosions.all")) {
          playerData.setString("goal_explosion", "fls");
          getLogger().send(p, Messages.ACTIVATED_EXPLOSION.getMessage(new String[]{explosion}));
          p.closeInventory();
        } else getLogger().send(p, Messages.INSUFFICIENT_PERMISSION.getMessage(null));
        break;
      }
      case "§4F§1K§4S": {
        if(p.hasPermission("group.fks") || p.hasPermission("footcube.goalexplosions.all")) {
          playerData.setString("goal_explosion", "fks");
          getLogger().send(p, Messages.ACTIVATED_EXPLOSION.getMessage(new String[]{explosion}));
          p.closeInventory();
        } else getLogger().send(p, Messages.INSUFFICIENT_PERMISSION.getMessage(null));
        break;
      }
      case "§1F§fK§6K": {
        if(p.hasPermission("group.fkk") || p.hasPermission("footcube.goalexplosions.all")) {
          playerData.setString("goal_explosion", "fkk");
          getLogger().send(p, Messages.ACTIVATED_EXPLOSION.getMessage(new String[]{explosion}));
          p.closeInventory();
        } else getLogger().send(p, Messages.INSUFFICIENT_PERMISSION.getMessage(null));
        break;
      }
      case "§6M§8K§6U": {
        if(p.hasPermission("group.mku") || p.hasPermission("footcube.goalexplosions.all")) {
          playerData.setString("goal_explosion", "mku");
          getLogger().send(p, Messages.ACTIVATED_EXPLOSION.getMessage(new String[]{explosion}));
          p.closeInventory();
        } else getLogger().send(p, Messages.INSUFFICIENT_PERMISSION.getMessage(null));
        break;
      }
      case "§9S§cP§fA": {
        if(p.hasPermission("group.spa") || p.hasPermission("footcube.goalexplosions.all")) {
          playerData.setString("goal_explosion", "spa");
          getLogger().send(p, Messages.ACTIVATED_EXPLOSION.getMessage(new String[]{explosion}));
          p.closeInventory();
        } else getLogger().send(p, Messages.INSUFFICIENT_PERMISSION.getMessage(null));
        break;
      }
      case "§cDisable": {
        playerData.setString("goal_explosion", "Disable");
        getLogger().send(p, Messages.DEACTIVATED_EXPLOSION.getMessage(null));
        p.closeInventory();

        break;
      }
      default:
        break;
    }
    playerData.savePlayerData(p.getUniqueId());
  }

}

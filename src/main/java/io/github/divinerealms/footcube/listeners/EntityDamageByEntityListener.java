package io.github.divinerealms.footcube.listeners;

import io.github.divinerealms.footcube.managers.UtilManager;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.Plugin;

public class EntityDamageByEntityListener implements Listener {
  @Getter private final Plugin plugin;
  @Getter private final UtilManager utilManager;

  public EntityDamageByEntityListener(final Plugin plugin, final UtilManager utilManager) {
    this.plugin = plugin;
    this.utilManager = utilManager;
  }

  @EventHandler
  public void onSlamSlime(final EntityDamageByEntityEvent event) {
    if (!(event.getEntity() instanceof Slime)) return;
    if (!getUtilManager().getPhysics().getCubes().contains((Slime) event.getEntity())) return;
    if (!(event.getDamager() instanceof Player)) return;
    if (event.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK) return;

    final Slime cube = (Slime) event.getEntity();
    final Player player = (Player) event.getDamager();

    if (player.getGameMode() != GameMode.SURVIVAL) {
      if (!player.hasPermission("nfootcube.clearcube")) event.setCancelled(true);
      else {
        // TODO: disable removal in matches
        getUtilManager().getPhysics().getCubes().remove(cube);
        cube.remove();
        getUtilManager().getMessages().send(player, "CUBE_CLEARED");
      }
      return;
    }

    String message = "&6[&eDebug&6] &b" + player.getName();

    if (getUtilManager().getCooldown().isCubeKickCooldownEnabled()) {
      final long timeLeft = getUtilManager().getCooldown().getTimeleftMillis(player.getUniqueId(), getUtilManager().getCooldown().getCubeKickCooldown());
      if (timeLeft > 0) {
        event.setCancelled(true);
        return;
      }
    }

    double charge = 0;
    double power = 0.4;

    if (getUtilManager().getPhysics().getCharges().containsKey(player.getUniqueId())) charge = getUtilManager().getPhysics().getCharges().get(player.getUniqueId()) * getUtilManager().getPhysics().getChargeLimit();
    power += getUtilManager().getPhysics().getLastMoveVector(player.getUniqueId()).length() * 2 + 0.4;

    double total = charge * getUtilManager().getPhysics().getKickPower();
    message += "&f made a";

    if (charge != 0) {
      if (power >= getUtilManager().getPhysics().getChargedKickLimit()) {
        total *= getUtilManager().getPhysics().getChargedKickLimit();
        message += " &acharged";
      } else {
        total *= power;
        message += "n unlimited &acharged";
      }
    } else {
      if (power >= getUtilManager().getPhysics().getRegularKickLimit()) {
        total = getUtilManager().getPhysics().getRegularKickLimit();
        message += " &aregular";
      } else {
        total = power;
        message += "n unlimited &aregular";
      }
    }

    cube.setVelocity(cube.getVelocity().add(player.getLocation().getDirection().normalize().multiply(total).setY(0.3)));
    cube.getWorld().playSound(cube.getLocation(), getUtilManager().getPhysics().getSoundKick(), 0.75F, 1F);
    //TODO: this.organization.ballTouch(player);

    getUtilManager().getCooldown().setCooldown(player.getUniqueId(), System.currentTimeMillis());
    //message = PlaceholderAPI.setPlaceholders(player, message);
    if (getUtilManager().getPhysics().isDebugEnabled())
      getPlugin().getServer().broadcast(ChatColor.translateAlternateColorCodes('&', message + "&f kick &7[" + getUtilManager().getPhysics().format(power) + "PW * " + getUtilManager().getPhysics().format(charge) + "CH = " + getUtilManager().getPhysics().format(total) + "KP]"), "nfootcube.debug");

    event.setCancelled(true);
  }
}

package io.github.divinerealms.footcube.listeners;

import io.github.divinerealms.footcube.managers.UtilManager;
import io.github.divinerealms.footcube.utils.Cooldown;
import io.github.divinerealms.footcube.utils.Messages;
import io.github.divinerealms.footcube.utils.Physics;
import lombok.Getter;
import org.bukkit.GameMode;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.Plugin;

public class EntityDamageByEntityListener implements Listener {
  @Getter private final Physics physics;
  @Getter private final Messages messages;
  @Getter private final Cooldown cooldown;
  @Getter private final Server server;

  public EntityDamageByEntityListener(final Plugin plugin, final UtilManager utilManager) {
    this.physics = utilManager.getPhysics();
    this.messages = utilManager.getMessages();
    this.cooldown = utilManager.getCooldown();
    this.server = plugin.getServer();
  }

  @EventHandler
  public void onSlamSlime(final EntityDamageByEntityEvent event) {
    if (!(event.getEntity() instanceof Slime)) return;
    if (!getPhysics().getCubes().contains((Slime) event.getEntity())) return;
    if (!(event.getDamager() instanceof Player)) return;
    if (event.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK) return;

    final Slime cube = (Slime) event.getEntity();
    final Player player = (Player) event.getDamager();

    if (player.getGameMode() != GameMode.SURVIVAL) {
      if (!player.hasPermission("nfootcube.clearcube")) event.setCancelled(true);
      else {
        // TODO: disable removal in matches
        getPhysics().getCubes().remove(cube);
        cube.remove();
        getMessages().send(player, "CUBE_CLEARED");
      }
      return;
    }

    String message = "&6[&eDebug&6] &b" + player.getName();

    if (getCooldown().isCubeKickCooldownEnabled()) {
      final long timeLeft = getCooldown().getTimeleftMillis(player.getUniqueId(), getCooldown().getCubeKickCooldown());
      if (timeLeft > 0) {
        event.setCancelled(true);
        return;
      }
    }

    double charge = 0;
    double power = 0.4;

    if (getPhysics().getCharges().containsKey(player.getUniqueId()))
      charge = getPhysics().getCharges().get(player.getUniqueId()) * getPhysics().getChargeLimit();
    power += getPhysics().getLastMoveVector(player.getUniqueId()).length() * 2 + 0.4;

    double total = charge * getPhysics().getKickPower();
    message += "&f made a";

    if (charge != 0) {
      if (power >= getPhysics().getChargedKickLimit()) {
        total *= getPhysics().getChargedKickLimit();
        message += " &acharged";
      } else {
        total *= power;
        message += "n unlimited &acharged";
      }
    } else {
      if (power >= getPhysics().getRegularKickLimit()) {
        total = getPhysics().getRegularKickLimit();
        message += " &aregular";
      } else {
        total = power;
        message += "n unlimited &aregular";
      }
    }

    cube.setVelocity(cube.getVelocity().add(player.getLocation().getDirection().normalize().multiply(total).setY(0.3)));
    cube.getWorld().playSound(cube.getLocation(), getPhysics().getSoundKick(), 0.75F, 1F);
    //TODO: this.organization.ballTouch(player);

    getCooldown().setCooldown(player.getUniqueId(), System.currentTimeMillis());

    if (getPhysics().isDebugEnabled()) {
      final double formattedPower = getPhysics().format(power);
      final double formattedCharge = getPhysics().format(charge);
      final double formattedTotal = getPhysics().format(total);
      message += "&f kick &7[" + formattedPower + "PW * " + formattedCharge + "CH = " + formattedTotal + "KP]";
      getServer().broadcast(getMessages().colorizeMessage(message), "nfootcube.debug");
    }

    event.setCancelled(true);
  }
}

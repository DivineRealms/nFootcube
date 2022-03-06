package io.github.divinerealms.footcube.listeners;

import io.github.divinerealms.footcube.managers.UtilManager;
import io.github.divinerealms.footcube.utils.Cooldown;
import io.github.divinerealms.footcube.configs.Messages;
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
  @Getter private final Plugin plugin;
  @Getter private final Server server;
  @Getter private final Messages messages;
  @Getter private final Cooldown cooldown;
  @Getter private final Physics physics;

  public EntityDamageByEntityListener(final Plugin plugin, final UtilManager utilManager) {
    this.plugin = plugin;
    this.server = plugin.getServer();
    this.messages = utilManager.getMessages();
    this.cooldown = utilManager.getCooldown();
    this.physics = utilManager.getPhysics();
  }

  @EventHandler
  public void onEntityDamageByEntity(final EntityDamageByEntityEvent event) {
    if (!(event.getEntity() instanceof Slime)) return;
    if (!getPhysics().getCubes().contains((Slime) event.getEntity())) return;
    if (!(event.getDamager() instanceof Player)) return;
    if (event.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK) return;

    final Slime cube = (Slime) event.getEntity();
    final Player player = (Player) event.getDamager();

    if (player.getGameMode() == GameMode.CREATIVE && player.hasPermission("nfootcube.clearcube")) {
      // TODO: disable removal in matches
      cube.remove();
      getMessages().send(player, "cube.cleared");
    }

    if (player.getGameMode() != GameMode.SURVIVAL) return;

    if (getCooldown().isCubeKickCooldownEnabled()) {
      final long timeLeft = getCooldown().getTimeleftMillis(player.getUniqueId(), getCooldown().getCubeKickCooldown());
      if (timeLeft > 0) {
        event.setCancelled(false);
        return;
      }
    }

    cube.setVelocity(cube.getVelocity().add(player.getLocation().getDirection().normalize().multiply(getPhysics().getTotalKickPower(player.getUniqueId())).setY(0.3)));
    getPhysics().playSound(cube, true);
    //TODO: this.organization.ballTouch(player);

    getPhysics().debug(player);
    getCooldown().setCooldown(player.getUniqueId(), System.currentTimeMillis());

    event.setCancelled(true);
  }
}

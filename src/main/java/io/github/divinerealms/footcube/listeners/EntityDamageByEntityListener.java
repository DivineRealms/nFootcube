package io.github.divinerealms.footcube.listeners;

import io.github.divinerealms.footcube.configs.Messages;
import io.github.divinerealms.footcube.managers.UtilManager;
import io.github.divinerealms.footcube.utils.Cooldown;
import io.github.divinerealms.footcube.utils.Logger;
import io.github.divinerealms.footcube.utils.Physics;
import lombok.Getter;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.Plugin;

@Getter
public class EntityDamageByEntityListener implements Listener {
  private final Plugin plugin;
  private final Logger logger;
  private final Cooldown cooldown;
  private final Physics physics;

  public EntityDamageByEntityListener(final Plugin plugin, final UtilManager utilManager) {
    this.plugin = plugin;
    this.logger = utilManager.getLogger();
    this.cooldown = utilManager.getCooldown();
    this.physics = utilManager.getPhysics();
  }

  @EventHandler
  public void onEntityDamageByEntity(final EntityDamageByEntityEvent event) {
    // Check if the entity is a Slime
    if (!(event.getEntity() instanceof Slime)) return;

    // Check if the Slime is part of the footcube
    if (!getPhysics().getCubes().contains((Slime) event.getEntity())) return;

    // Check if the damager is a Player
    if (!(event.getDamager() instanceof Player)) return;

    // Check if the damage cause is ENTITY_ATTACK
    if (event.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK) return;

    final Slime cube = (Slime) event.getEntity();
    final Player player = (Player) event.getDamager();

    // Handle creative mode and cube removal
    handleCreativeModeAndRemoval(player, cube, event);

    // Check if the player is in survival mode
    if (player.getGameMode() != GameMode.SURVIVAL) return;

    // Handle cube kick cooldown
    if (!handleCubeKickCooldown(player)) {
      // Cancel the original damage event if cube kick cooldown is active
      event.setCancelled(true);
      return;
    }

    // Perform cube kick action
    performCubeKick(player, cube);
  }

  // TODO: Disable removal in matches
  private void handleCreativeModeAndRemoval(Player player, Slime cube, EntityDamageByEntityEvent event) {
    if (player.getGameMode() == GameMode.CREATIVE && player.hasPermission("nfootcube.clearcube")) {
      cube.remove();
      getLogger().send(player, Messages.CUBE_CLEARED.getMessage(null));

      // Cancel the original damage event after cube removal
      event.setCancelled(true);
    }
  }

  private boolean handleCubeKickCooldown(Player player) {
    if (getCooldown().isCubeKickCooldownEnabled()) {
      final long timeLeft = getCooldown().getTimeleft(player.getUniqueId(), getCooldown().getCubeKickCooldown(), true);
      if (timeLeft > 0) {
        // Cancel the original damage event if cube kick cooldown is active
        return false;
      }
      getCooldown().setCooldown(player.getUniqueId(), System.currentTimeMillis());
    }
    return true;
  }

  private void performCubeKick(Player player, Slime cube) {
    cube.setVelocity(cube.getVelocity().add(player.getLocation().getDirection().normalize().multiply(getPhysics().getTotalKickPower(player.getUniqueId())).setY(0.2)));
    getPhysics().playSound(cube, true);
    getPhysics().debug(player);
  }
}

package io.github.divinerealms.footcube.listeners;

import io.github.divinerealms.footcube.configs.Lang;
import io.github.divinerealms.footcube.managers.UtilManager;
import io.github.divinerealms.footcube.utils.Cooldown;
import io.github.divinerealms.footcube.utils.Logger;
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

@Getter
public class EntityDamageByEntityListener implements Listener {
  private final Plugin plugin;
  private final Server server;
  private final Logger logger;
  private final Cooldown cooldown;
  private final Physics physics;

  public EntityDamageByEntityListener(Plugin plugin, UtilManager utilManager) {
    this.plugin = plugin;
    this.server = plugin.getServer();
    this.logger = utilManager.getLogger();
    this.cooldown = utilManager.getCooldown();
    this.physics = utilManager.getPhysics();
  }

  @EventHandler
  public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
    if (!(event.getEntity() instanceof Slime)) return;
    if (!getPhysics().getCubes().contains((Slime) event.getEntity())) return;
    if (!(event.getDamager() instanceof Player)) return;
    if (event.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK) return;

    Slime cube = (Slime) event.getEntity();
    Player player = (Player) event.getDamager();

    if (player.getGameMode() == GameMode.CREATIVE && player.hasPermission("nfootcube.clearcube")) {
      // TODO: disable removal in matches
      cube.remove();
      getLogger().send(player, Lang.CUBE_CLEARED.getConfigValue(null));
    }

    if (player.getGameMode() != GameMode.SURVIVAL) return;
    if (getCooldown().isCubeKickCooldownEnabled()) {
      long timeLeft = getCooldown().getTimeleftMillis(player.getUniqueId(), getCooldown().getCubeKickCooldown());
      if (timeLeft > 0) {
        event.setCancelled(true);
        return;
      }
      getCooldown().setCooldown(player.getUniqueId(), System.currentTimeMillis());
    }

    cube.setVelocity(cube.getVelocity().add(player.getLocation().getDirection().normalize().multiply(getPhysics().getTotalKickPower(player.getUniqueId())).setY(0.2)));
    getPhysics().playSound(cube, true);
    getPhysics().debug(player);

    event.setCancelled(true);
  }
}

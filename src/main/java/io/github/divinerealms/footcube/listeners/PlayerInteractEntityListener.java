package io.github.divinerealms.footcube.listeners;

import io.github.divinerealms.footcube.managers.UtilManager;
import io.github.divinerealms.footcube.utils.Physics;
import lombok.Getter;
import org.bukkit.GameMode;
import org.bukkit.entity.Slime;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

@Getter
public class PlayerInteractEntityListener implements Listener {
  private final Plugin plugin;
  private final Physics physics;

  public PlayerInteractEntityListener(Plugin plugin, UtilManager utilManager) {
    this.plugin = plugin;
    this.physics = utilManager.getPhysics();
  }

  @EventHandler
  public void onRightClick(PlayerInteractEntityEvent event) {
    if (!(event.getRightClicked() instanceof Slime)) return;
    if (!getPhysics().getCubes().contains((Slime) event.getRightClicked())) return;
    if (getPhysics().getKicked().containsKey(event.getPlayer().getUniqueId())) return;
    if (event.getPlayer().getGameMode() != GameMode.SURVIVAL) return;

    Slime cube = (Slime) event.getRightClicked();
    cube.setVelocity(cube.getVelocity().add(new Vector(0, 0.6, 0)));
    getPhysics().playSound(cube, false);
    getPhysics().getKicked().put(event.getPlayer().getUniqueId(), System.currentTimeMillis());
    //TODO this.organization.ballTouch(event.getPlayer());
  }
}

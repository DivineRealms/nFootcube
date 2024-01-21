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

  public PlayerInteractEntityListener(final Plugin plugin, final UtilManager utilManager) {
    this.plugin = plugin;
    this.physics = utilManager.getPhysics();
  }

  @EventHandler
  public void onRightClick(final PlayerInteractEntityEvent event) {
    // Check if the entity is a slime and is part of the cubes
    if (!(event.getRightClicked() instanceof Slime) || !getPhysics().getCubes().contains((Slime) event.getRightClicked())) {
      return;
    }

    // Check if the player has kicked a cube recently
    if (getPhysics().getKicked().containsKey(event.getPlayer().getUniqueId())) {
      return;
    }

    // Check if the player is in survival mode
    if (event.getPlayer().getGameMode() != GameMode.SURVIVAL) {
      return;
    }

    final Slime cube = (Slime) event.getRightClicked();

    // Kick the cube upwards
    cube.setVelocity(cube.getVelocity().add(new Vector(0, 0.6, 0)));

    // Play the cube kick sound
    getPhysics().playSound(cube, false);

    // Record the time the player kicked the cube
    getPhysics().getKicked().put(event.getPlayer().getUniqueId(), System.currentTimeMillis());

    // TODO: Handle ball touch event
    // this.organization.ballTouch(event.getPlayer());
  }
}

package io.github.divinerealms.footcube.listeners;

import io.github.divinerealms.footcube.managers.UtilManager;
import lombok.Getter;
import org.bukkit.GameMode;
import org.bukkit.entity.Slime;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.util.Vector;

public class PlayerInteractEntityListener implements Listener {
  @Getter private final UtilManager utilManager;

  public PlayerInteractEntityListener(UtilManager utilManager) {
    this.utilManager = utilManager;
  }

  @EventHandler
  public void onRightClick(final PlayerInteractEntityEvent event) {
    if (!(event.getRightClicked() instanceof Slime)) return;
    if (!getUtilManager().getPhysics().getCubes().contains((Slime) event.getRightClicked())) return;
    if (getUtilManager().getPhysics().getKicked().containsKey(event.getPlayer().getUniqueId())) return;
    if (event.getPlayer().getGameMode() != GameMode.SURVIVAL) return;

    final Slime cube = (Slime) event.getRightClicked();
    cube.setVelocity(cube.getVelocity().add(new Vector(0, 0.7, 0)));
    cube.getWorld().playSound(cube.getLocation(), getUtilManager().getPhysics().getSoundMove(), 0.75F, 1F);
    getUtilManager().getPhysics().getKicked().put(event.getPlayer().getUniqueId(), System.currentTimeMillis());
    //TODO this.organization.ballTouch(event.getPlayer());
  }
}

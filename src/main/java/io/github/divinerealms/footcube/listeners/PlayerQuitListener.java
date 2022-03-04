package io.github.divinerealms.footcube.listeners;

import io.github.divinerealms.footcube.managers.UtilManager;
import io.github.divinerealms.footcube.utils.Physics;
import lombok.Getter;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {
  @Getter private final Physics physics;

  public PlayerQuitListener(final UtilManager utilManager) {
    this.physics = utilManager.getPhysics();
  }

  @EventHandler
  public void onQuit(final PlayerQuitEvent event) {
    getPhysics().getLastLocations().remove(event.getPlayer().getUniqueId());
    getPhysics().getCharges().remove(event.getPlayer().getUniqueId());
  }
}

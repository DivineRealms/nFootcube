package io.github.divinerealms.footcube.listeners;

import io.github.divinerealms.footcube.managers.UtilManager;
import lombok.Getter;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {
  @Getter private final UtilManager utilManager;

  public PlayerQuitListener(final UtilManager utilManager) {
    this.utilManager = utilManager;
  }

  @EventHandler
  public void onQuit(final PlayerQuitEvent event) {
    this.getUtilManager().getPhysics().getLastLocations().remove(event.getPlayer().getUniqueId());
    this.getUtilManager().getPhysics().getCharges().remove(event.getPlayer().getUniqueId());
  }
}

package io.github.divinerealms.footcube.listeners;

import io.github.divinerealms.footcube.managers.UtilManager;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.UUID;

public class PlayerMoveListener implements Listener {
  @Getter private final UtilManager utilManager;

  public PlayerMoveListener(final UtilManager utilManager) {
    this.utilManager = utilManager;
  }

  @EventHandler
  public void onMove(final PlayerMoveEvent event) {
    final UUID playerID = event.getPlayer().getUniqueId();
    final Deque<Location> locations = getUtilManager().getPhysics().getLastLocations().computeIfAbsent(playerID, key -> new ArrayDeque<>());
    if (locations.size() == 2) locations.poll();
    locations.add(event.getTo());
  }
}

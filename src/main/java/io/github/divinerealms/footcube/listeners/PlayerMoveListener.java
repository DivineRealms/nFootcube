package io.github.divinerealms.footcube.listeners;

import io.github.divinerealms.footcube.managers.UtilManager;
import io.github.divinerealms.footcube.utils.Physics;
import lombok.Getter;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.UUID;

public class PlayerMoveListener implements Listener {
  @Getter private final Physics physics;

  public PlayerMoveListener(final UtilManager utilManager) {
    this.physics = utilManager.getPhysics();
  }

  @EventHandler
  public void onMove(final PlayerMoveEvent event) {
    if (event.getPlayer().getGameMode() != GameMode.SURVIVAL) return;
    final UUID playerID = event.getPlayer().getUniqueId();
    final Deque<Location> locations = getPhysics().getLastLocations().computeIfAbsent(playerID, key -> new ArrayDeque<>());
    if (locations.size() == 2) locations.poll();
    locations.add(event.getTo());
  }
}

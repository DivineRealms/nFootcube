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

@Getter
public class PlayerMoveListener implements Listener {
  private final Physics physics;

  public PlayerMoveListener(final UtilManager utilManager) {
    this.physics = utilManager.getPhysics();
  }

  @EventHandler
  public void onMove(final PlayerMoveEvent event) {
    // Check if the player is in survival mode
    if (event.getPlayer().getGameMode() != GameMode.SURVIVAL) {
      return;
    }

    // Get the unique ID of the player
    final UUID playerID = event.getPlayer().getUniqueId();

    // Retrieve or create a deque of player locations
    final Deque<Location> locations = getPhysics().getLastLocations().computeIfAbsent(playerID, key -> new ArrayDeque<>());

    // Limit the deque size to 2
    if (locations.size() == 2) {
      locations.poll();
    }

    // Add the current player location to the deque
    locations.add(event.getTo());
  }
}

package io.github.divinerealms.footcube.listeners;

import io.github.divinerealms.footcube.managers.UtilManager;
import io.github.divinerealms.footcube.utils.Physics;
import lombok.Getter;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Slime;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkUnloadEvent;

@Getter
public class ChunkUnloadListener implements Listener {
  private final Physics physics;

  public ChunkUnloadListener(final UtilManager utilManager) {
    this.physics = utilManager.getPhysics();
  }

  /**
   * Handles the chunk unload event.
   *
   * @param event The ChunkUnloadEvent.
   */
  @EventHandler
  public void onUnloadChunk(final ChunkUnloadEvent event) {
    // Get all entities in the chunk
    final Entity[] entities = event.getChunk().getEntities();

    // Iterate through each entity in the chunk
    for (final Entity entity : entities) {
      // Check if the entity is a Slime
      if (!(entity instanceof Slime)) {
        return; // Skip to the next entity if it's not a Slime
      }

      // Check if the Slime is part of the cubes set
      if (!getPhysics().getCubes().contains(entity)) {
        return; // Skip to the next entity if it's not part of the cubes set
      }

      // Remove the Slime from the cubes set and remove it from the world
      getPhysics().getCubes().remove((Slime) entity);
      entity.remove();
    }
  }
}

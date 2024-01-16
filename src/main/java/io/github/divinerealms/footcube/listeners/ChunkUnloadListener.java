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

  @EventHandler
  public void onUnloadChunk(final ChunkUnloadEvent event) {
    final Entity[] entities;
    for (int length = (entities = event.getChunk().getEntities()).length, i = 0; i < length; ++i) {
      final Entity entity = entities[i];
      if (!(entity instanceof Slime)) return;
      if (!getPhysics().getCubes().contains(entity)) return;
      getPhysics().getCubes().remove((Slime) entity);
      entity.remove();
    }
  }
}

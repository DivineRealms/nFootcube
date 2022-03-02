package io.github.divinerealms.footcube.listeners;

import io.github.divinerealms.footcube.managers.UtilManager;
import lombok.Getter;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Slime;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkUnloadEvent;

public class ChunkUnloadListener implements Listener {
  @Getter private final UtilManager utilManager;

  public ChunkUnloadListener(final UtilManager utilManager) {
    this.utilManager = utilManager;
  }

  @EventHandler
  public void onUnloadChunk(final ChunkUnloadEvent event) {
    final Entity[] entities;
    for (int length = (entities = event.getChunk().getEntities()).length, i = 0; i < length; ++i) {
      final Entity entity = entities[i];
      if (!(entity instanceof Slime)) return;
      if (!getUtilManager().getPhysics().getCubes().contains(entity)) return;

      getUtilManager().getPhysics().getCubes().remove((Slime) entity);
      entity.remove();
    }
  }
}

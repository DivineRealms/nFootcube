package io.github.divinerealms.footcube.listeners;

import io.github.divinerealms.footcube.managers.UtilManager;
import lombok.Getter;
import org.bukkit.entity.Slime;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class EntityDamageListener implements Listener {
  @Getter private final UtilManager utilManager;

  public EntityDamageListener(final UtilManager utilManager) {
    this.utilManager = utilManager;
  }

  @EventHandler(priority = EventPriority.HIGH)
  public void onDamage(final EntityDamageEvent event) {
    if (!(event.getEntity() instanceof Slime) && !getUtilManager().getPhysics().getCubes().contains((Slime) event.getEntity()))
      return;
    event.setCancelled(true);
  }
}

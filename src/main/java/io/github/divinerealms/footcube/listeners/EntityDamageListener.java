package io.github.divinerealms.footcube.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class EntityDamageListener implements Listener {
  @EventHandler(priority = EventPriority.HIGH)
  public void onDamage(EntityDamageEvent event) {
    event.setCancelled(true);
  }
}

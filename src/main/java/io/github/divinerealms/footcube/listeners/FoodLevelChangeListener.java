package io.github.divinerealms.footcube.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class FoodLevelChangeListener implements Listener {
  @EventHandler public void onFoodLevelChange(FoodLevelChangeEvent event) {
    event.setCancelled(true);
  }
}

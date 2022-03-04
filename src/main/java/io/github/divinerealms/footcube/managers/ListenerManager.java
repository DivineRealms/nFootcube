package io.github.divinerealms.footcube.managers;

import io.github.divinerealms.footcube.listeners.*;
import lombok.Getter;
import org.bukkit.Server;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

public class ListenerManager {
  @Getter final Plugin plugin;
  @Getter private final UtilManager utilManager;
  @Getter private boolean registered = false;

  public ListenerManager(final Plugin plugin, final UtilManager utilManager) {
    this.plugin = plugin;
    this.utilManager = utilManager;
  }

  public void registerListeners() {
    this.registered = true;

    final Server server = getPlugin().getServer();
    final PluginManager pluginManager = server.getPluginManager();

    pluginManager.registerEvents(new ChunkUnloadListener(getUtilManager()), getPlugin());
    pluginManager.registerEvents(new EntityDamageByEntityListener(getPlugin(), getUtilManager()), getPlugin());
    pluginManager.registerEvents(new EntityDamageListener(), getPlugin());
    pluginManager.registerEvents(new FoodLevelChangeListener(), getPlugin());
    pluginManager.registerEvents(new PlayerInteractEntityListener(getUtilManager()), getPlugin());
    pluginManager.registerEvents(new PlayerMoveListener(getUtilManager()), getPlugin());
    pluginManager.registerEvents(new PlayerQuitListener(getUtilManager()), getPlugin());
    pluginManager.registerEvents(new PlayerToggleSneakListener(getUtilManager()), getPlugin());
  }

  public void unregisterListeners() {
    this.registered = false;
    HandlerList.unregisterAll(getPlugin());
  }
}

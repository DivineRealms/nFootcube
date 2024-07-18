package io.github.divinerealms.footcube.managers;

import io.github.divinerealms.footcube.listeners.*;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

@Getter
public class ListenerManager {
  private final Plugin plugin;
  private final PluginManager pluginManager;
  private final UtilManager utilManager;
  @Setter private boolean registered = false;

  public ListenerManager(Plugin plugin, UtilManager utilManager) {
    this.plugin = plugin;
    this.pluginManager = plugin.getServer().getPluginManager();
    this.utilManager = utilManager;
  }

  public void registerListeners() {
    setRegistered(true);
    getPluginManager().registerEvents(new ChunkUnloadListener(getUtilManager()), getPlugin());
    getPluginManager().registerEvents(new EntityDamageByEntityListener(getPlugin(), getUtilManager()), getPlugin());
    getPluginManager().registerEvents(new EntityDamageListener(), getPlugin());
    getPluginManager().registerEvents(new FoodLevelChangeListener(), getPlugin());
    getPluginManager().registerEvents(new PlayerInteractEntityListener(getPlugin(), getUtilManager()), getPlugin());
    getPluginManager().registerEvents(new PlayerJoinListener(getUtilManager()), getPlugin());
    getPluginManager().registerEvents(new PlayerMoveListener(getUtilManager()), getPlugin());
    getPluginManager().registerEvents(new PlayerQuitListener(getUtilManager()), getPlugin());
    getPluginManager().registerEvents(new PlayerToggleSneakListener(getUtilManager()), getPlugin());
  }

  public void unregisterListeners() {
    setRegistered(false);
    HandlerList.unregisterAll(getPlugin());
  }
}

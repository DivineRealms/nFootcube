package io.github.divinerealms.footcube.managers;

import io.github.divinerealms.footcube.Footcube;
import io.github.divinerealms.footcube.listeners.*;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

/**
 * Manages the registration and unregistration of event listeners.
 */
@Getter
public class ListenerManager {
  private final Footcube instance;
  private final Plugin plugin;
  private final PluginManager pluginManager;
  private final UtilManager utilManager;
  @Setter private boolean registered = false;

  /**
   * Constructs a new ListenerManager instance.
   *
   * @param plugin      The main plugin instance.
   * @param utilManager The UtilManager instance.
   */
  public ListenerManager(final Footcube instance, final Plugin plugin, final UtilManager utilManager) {
    this.instance = instance;
    this.plugin = plugin;
    this.pluginManager = plugin.getServer().getPluginManager();
    this.utilManager = utilManager;
  }

  /**
   * Registers all event listeners.
   */
  public void registerListeners() {
    setRegistered(true);
    getPluginManager().registerEvents(new ChunkUnloadListener(getUtilManager()), getPlugin());
    getPluginManager().registerEvents(new EntityDamageByEntityListener(getPlugin(), getUtilManager()), getPlugin());
    getPluginManager().registerEvents(new EntityDamageListener(), getPlugin());
    getPluginManager().registerEvents(new FoodLevelChangeListener(), getPlugin());
    getPluginManager().registerEvents(new PlayerInteractEntityListener(getPlugin(), getUtilManager()), getPlugin());
    getPluginManager().registerEvents(new PlayerJoinListener(getInstance(), getUtilManager()), getPlugin());
    getPluginManager().registerEvents(new PlayerMoveListener(getUtilManager()), getPlugin());
    getPluginManager().registerEvents(new PlayerQuitListener(getInstance(), getUtilManager()), getPlugin());
    getPluginManager().registerEvents(new PlayerToggleSneakListener(getUtilManager()), getPlugin());
  }

  /**
   * Unregisters all event listeners.
   */
  public void unregisterListeners() {
    setRegistered(false);
    HandlerList.unregisterAll(getPlugin());
  }
}

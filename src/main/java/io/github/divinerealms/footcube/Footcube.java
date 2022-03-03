package io.github.divinerealms.footcube;

import io.github.divinerealms.footcube.commands.BaseCommand;
import io.github.divinerealms.footcube.managers.ListenerManager;
import io.github.divinerealms.footcube.managers.UtilManager;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.plugin.java.JavaPlugin;

public class Footcube extends JavaPlugin {
  @Getter @Setter private UtilManager utilManager;
  @Getter @Setter private ListenerManager listenerManager;

  @Override
  public void onEnable() {
    setUtilManager(new UtilManager(this));
    setListenerManager(new ListenerManager(this, getUtilManager()));
    saveDefaultConfig();
    getUtilManager().reloadUtils();
    getUtilManager().getLogger().sendBanner();
    getUtilManager().getLogger().info("Loading commands...");
    getUtilManager().getLogger().info("Loading listeners...");
    setup();
    getUtilManager().getLogger().info("Successfully enabled!");
  }

  @Override
  public void onDisable() {
    shutdown();
  }

  public void reload() {
    getUtilManager().reloadUtils();
    shutdown();
    setup();
  }

  private void setup() {
    getCommand("nfootcube").setExecutor(new BaseCommand(this, getUtilManager()));
    getCommand("nfootcube").setTabCompleter(new BaseCommand(this, getUtilManager()));

    if (getListenerManager().isRegistered()) getListenerManager().unregisterListeners();
    getListenerManager().registerListeners();
    getServer().getScheduler().runTaskTimer(this, getUtilManager().getPhysics()::update, 20L, 1L);
  }

  private void shutdown() {
    getUtilManager().getPhysics().removeCubes();
    getServer().getScheduler().cancelTasks(this);
    getServer().getMessenger().unregisterIncomingPluginChannel(this);
    getListenerManager().unregisterListeners();
  }
}


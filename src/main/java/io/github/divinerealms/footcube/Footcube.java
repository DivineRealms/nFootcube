package io.github.divinerealms.footcube;

import io.github.divinerealms.footcube.commands.BaseCommand;
import io.github.divinerealms.footcube.managers.ListenerManager;
import io.github.divinerealms.footcube.managers.ConfigManager;
import io.github.divinerealms.footcube.managers.UtilManager;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public class Footcube extends JavaPlugin {
  @Getter private ConfigManager configManager;
  @Getter private UtilManager utilManager;
  @Getter private ListenerManager listenerManager;
  @Getter private long timeAtStart = System.currentTimeMillis();

  @Override
  public void onEnable() {
    configManager = new ConfigManager(this);
    getConfigManager().saveDefaultConfig("messages.yml");
    saveDefaultConfig();

    utilManager = new UtilManager(this, getConfigManager());
    getUtilManager().reloadUtils();
    listenerManager = new ListenerManager(this, getUtilManager());

    getUtilManager().getLogger().setLogo();
    setup();
  }

  @Override
  public void onDisable() {
    shutdown();
  }

  public void reload() {
    this.timeAtStart = System.currentTimeMillis();
    reloadConfig();
    getUtilManager().reloadUtils();

    shutdown();
    setup();
  }

  private void setup() {
    this.timeAtStart = System.currentTimeMillis();

    getLogger().log(Level.INFO, "Loading commands...");
    getCommand("nfootcube").setExecutor(new BaseCommand(this, getUtilManager()));
    getCommand("nfootcube").setTabCompleter(new BaseCommand(this, getUtilManager()));
    
    getLogger().log(Level.INFO, "Loading listeners...");
    if (getListenerManager().isRegistered()) getListenerManager().unregisterListeners();
    getListenerManager().registerListeners();
    getServer().getScheduler().runTaskTimer(this, getUtilManager().getPhysics()::update, 20L, 1L);

    final String duration = String.valueOf(System.currentTimeMillis() - getTimeAtStart());
    getLogger().log(Level.INFO, "Successfully enabled! (took %duration%ms)".replace("%duration%", duration));
  }

  private void shutdown() {
    getUtilManager().getPhysics().removeCubes();
    getServer().getScheduler().cancelTasks(this);
    getServer().getMessenger().unregisterIncomingPluginChannel(this);
    getListenerManager().unregisterListeners();
  }
}


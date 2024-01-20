package io.github.divinerealms.footcube;

import io.github.divinerealms.footcube.commands.BaseCommand;
import io.github.divinerealms.footcube.commands.ClearCubeCommand;
import io.github.divinerealms.footcube.commands.CommandDisabler;
import io.github.divinerealms.footcube.commands.CubeCommand;
import io.github.divinerealms.footcube.configs.Config;
import io.github.divinerealms.footcube.configs.Lang;
import io.github.divinerealms.footcube.managers.ConfigManager;
import io.github.divinerealms.footcube.managers.ListenerManager;
import io.github.divinerealms.footcube.managers.UtilManager;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.plugin.java.JavaPlugin;

@Setter @Getter
public class Footcube extends JavaPlugin {
  private final ConfigManager configManager = new ConfigManager(this, "");
  private UtilManager utilManager;
  private ListenerManager listenerManager;

  @Override
  public void onEnable() {
    saveDefaultConfig();
    setupConfigs();
    setUtilManager(new UtilManager(this));
    setListenerManager(new ListenerManager(this, getUtilManager()));
    getUtilManager().reloadUtils();
    getUtilManager().getLogger().sendBanner();
    getLogger().info("Loading commands...");
    getLogger().info("Loading listeners...");
    setup();
    getLogger().info("Successfully enabled!");
  }

  @Override
  public void onDisable() {
    shutdown();
  }

  public void reload() {
    shutdown();
    getUtilManager().reloadUtils();
    setup();
  }

  public void setup() {
    getCommand("footcube").setExecutor(new BaseCommand(this, getUtilManager()));
    getCommand("cube").setExecutor(new CubeCommand(getUtilManager()));
    getCommand("clearcube").setExecutor(new ClearCubeCommand(getUtilManager()));
    getCommand("commanddisabler").setExecutor(new CommandDisabler(getUtilManager()));

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

  public void setupConfigs() {
    getConfigManager().createNewFile("messages.yml", "Loading messages.yml", "Footcube Messages");
    getConfigManager().createNewFile("config.yml", "Loading config.yml", "Footcube Configuration");
    loadConfigs();
  }

  private void loadConfigs() {
    Lang.setFile(getConfigManager().getConfig("messages.yml"));
    Config.setFile(getConfigManager().getConfig("config.yml"));

    for (final Lang value : Lang.values())
      getConfigManager().getConfig("messages.yml").addDefault(value.getPath(), value.getDefault());

    getConfigManager().getConfig("config.yml").options().copyDefaults(true);
    getConfigManager().saveConfig("config.yml");

    getConfigManager().getConfig("messages.yml").options().copyDefaults(true);
    getConfigManager().saveConfig("messages.yml");
  }
}


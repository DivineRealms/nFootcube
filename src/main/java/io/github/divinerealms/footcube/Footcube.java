package io.github.divinerealms.footcube;

import co.aikar.commands.BukkitCommandManager;
import io.github.divinerealms.footcube.commands.ClearCubeCommand;
import io.github.divinerealms.footcube.commands.CommandDisabler;
import io.github.divinerealms.footcube.commands.CubeCommand;
import io.github.divinerealms.footcube.commands.DefaultCommand;
import io.github.divinerealms.footcube.configs.Config;
import io.github.divinerealms.footcube.configs.Lang;
import io.github.divinerealms.footcube.managers.ConfigManager;
import io.github.divinerealms.footcube.managers.ListenerManager;
import io.github.divinerealms.footcube.managers.UtilManager;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

@Getter @Setter
public class Footcube extends JavaPlugin {
  private final ConfigManager configManager = new ConfigManager(this, "");
  private YamlConfiguration config;
  private UtilManager utilManager;
  private ListenerManager listenerManager;
  @Getter private static Footcube instance;

  @Override
  public void onEnable() {
    instance = this;
    getServer().getScheduler().cancelTasks(this);
    setupMessages();
    setupConfig();
    setupManagers();
    setupCommands();
    setupListeners();

    getUtilManager().getLogger().sendBanner();
    getLogger().info("Successfully enabled!");

    getServer().getScheduler().runTaskTimer(this, getUtilManager().getPhysics()::update, 20L, 1L);
  }

  @Override
  public void onDisable() {
    getServer().getScheduler().cancelTasks(this);
    getServer().getMessenger().unregisterIncomingPluginChannel(this);
    if (listenerManager != null) {
      getListenerManager().unregisterListeners();
    }
  }

  public void setupConfig() {
    Config.setup(this);
    configManager.loadConfig("nFootcube Config", "config.yml");
    config = Config.getConfig("config.yml");
  }

  private void setupManagers() {
    utilManager = new UtilManager(this);
    listenerManager = new ListenerManager(this, utilManager);
  }

  private void setupMessages() {
    Lang.setFile(getConfigManager().getConfig("messages.yml"));

    for (Lang value : Lang.values())
      getConfigManager().getConfig("messages.yml").addDefault(value.getPath(), value.getDefault());

    getConfigManager().getConfig("messages.yml").options().copyDefaults(true);
    getConfigManager().saveConfig("messages.yml");
  }

  private void setupCommands() {
    BukkitCommandManager commandManager = new BukkitCommandManager(this);

    commandManager.registerCommand(new DefaultCommand(getUtilManager()));
    commandManager.registerCommand(new CubeCommand(getUtilManager()));
    commandManager.registerCommand(new ClearCubeCommand(getUtilManager()));
    commandManager.registerCommand(new CommandDisabler(getUtilManager()));
  }

  private void setupListeners() {
    if (listenerManager.isRegistered()) {
      listenerManager.unregisterListeners();
    }
    listenerManager.registerListeners();
  }
}


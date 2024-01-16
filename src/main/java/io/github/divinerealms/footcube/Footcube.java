package io.github.divinerealms.footcube;

import io.github.divinerealms.footcube.commands.BaseCommand;
import io.github.divinerealms.footcube.commands.ClearCubeCommand;
import io.github.divinerealms.footcube.commands.CommandDisabler;
import io.github.divinerealms.footcube.commands.CubeCommand;
import io.github.divinerealms.footcube.configs.Lang;
import io.github.divinerealms.footcube.managers.ConfigManager;
import io.github.divinerealms.footcube.managers.ListenerManager;
import io.github.divinerealms.footcube.managers.UtilManager;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.plugin.java.JavaPlugin;

@Setter @Getter
public class Footcube extends JavaPlugin {
  private final ConfigManager messagesFile = new ConfigManager(this, "");
  private UtilManager utilManager;
  private ListenerManager listenerManager;

  @Override
  public void onEnable() {
    setupMessages();
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

  public void setupMessages() {
    getMessagesFile().createNewFile("messages.yml", "Loading messages.yml", "Footcube Messages");
    loadMessages();
  }

  private void shutdown() {
    getUtilManager().getPhysics().removeCubes();
    getServer().getScheduler().cancelTasks(this);
    getServer().getMessenger().unregisterIncomingPluginChannel(this);
    getListenerManager().unregisterListeners();
  }

  private void loadMessages() {
    Lang.setFile(getMessagesFile().getConfig("messages.yml"));

    for (final Lang value : Lang.values())
      getMessagesFile().getConfig("messages.yml").addDefault(value.getPath(), value.getDefault());

    getMessagesFile().getConfig("messages.yml").options().copyDefaults(true);
    getMessagesFile().saveConfig("messages.yml");
  }
}


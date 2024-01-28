package io.github.divinerealms.footcube;

import io.github.divinerealms.footcube.commands.*;
import io.github.divinerealms.footcube.configs.Messages;
import io.github.divinerealms.footcube.managers.ConfigManager;
import io.github.divinerealms.footcube.managers.ListenerManager;
import io.github.divinerealms.footcube.managers.UtilManager;
import lombok.Getter;
import lombok.Setter;
import net.megavex.scoreboardlibrary.api.ScoreboardLibrary;
import net.megavex.scoreboardlibrary.api.noop.NoopScoreboardLibrary;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.logging.Level;

@Getter
@Setter
public class Footcube extends JavaPlugin {
  // ConfigManager for handling configuration files
  private final ConfigManager configManager = new ConfigManager(this, "");
  private UtilManager utilManager; // Manager for various utility functions
  private ListenerManager listenerManager; // Manager for handling event listeners
  public ScoreboardLibrary scoreboardLibrary; // Scoreboard library for displaying information
  private Economy economy; // Economy system for handling in-game currency

  @Override
  public void onEnable() {
    setupMessages();

    // Initialize Config and setup plugin
    saveDefaultConfig();
    setup();

    // Log plugin information
    getUtilManager().getLogger().sendBanner();
    getLogger().info("Loading commands...");
    getLogger().info("Loading listeners...");
    getLogger().info("Successfully enabled!");

    // Reload configuration
    getConfigManager().reloadConfig("config.yml");
  }

  @Override
  public void onDisable() {
    // Shutdown tasks
    shutdown();
  }

  public void reload() {
    // Reload tasks
    shutdown();
    getUtilManager().reloadUtils();
    getConfigManager().reloadConfig("config.yml");
    setup();
  }

  private void setupMessages() {
    getConfigManager().createNewFile("messages.yml", "Footcube Messages", "Loading messages.yml");
    loadMessages();
  }

  private void loadMessages() {
    Messages.setFile(getConfigManager().getConfig("messages.yml"));

    for (final Messages value : Messages.values())
      getConfigManager().getConfig("messages.yml").addDefault(value.getPath(), value.getDefault());

    getConfigManager().getConfig("messages.yml").options().copyDefaults(true);
    getConfigManager().saveConfig("messages.yml");
  }

  public void setup() {
    // Check if Vault is available and setup economy
    if (!setupEconomy())
      getLogger().log(Level.SEVERE, "Vault not found. Economy disabled.");

    // Initialize managers and components
    initializeManagersAndComponents();

    // Unregister and register listeners
    if (getListenerManager() != null && getListenerManager().isRegistered())
      getListenerManager().unregisterListeners();
    if (getListenerManager() != null) getListenerManager().registerListeners();

    // Schedule repeating task
    final BukkitScheduler scheduler = getServer().getScheduler();
    scheduler.runTaskTimer(this, getUtilManager().getPhysics()::update, 20L, 1L);
    scheduler.runTaskTimer(this, getUtilManager().getOrganization()::update, 20L, 1L);
    scheduler.runTaskTimer(this, getUtilManager().getOrganization()::refreshCache, 50L, 50L);
  }

  private void registerCommands() {
    // Register plugin commands
    getCommand("footcube").setExecutor(new BaseCommand(this, getUtilManager()));
    getCommand("cube").setExecutor(new CubeCommand(getUtilManager()));
    getCommand("clearcube").setExecutor(new ClearCubeCommand(this, getUtilManager()));
    getCommand("commanddisabler").setExecutor(new CommandDisabler(getUtilManager()));
    getCommand("teamchat").setExecutor(new TeamChatCommand(this, getUtilManager()));
  }

  private void shutdown() {
    // Shutdown tasks
    getUtilManager().getPhysics().removeCubes();
    getServer().getScheduler().cancelTasks(this);
    getServer().getMessenger().unregisterIncomingPluginChannel(this);
    getListenerManager().unregisterListeners();
  }

  private void initializeManagersAndComponents() {
    // Initialize UtilManager, ListenerManager
    setUtilManager(new UtilManager(this, this));
    setListenerManager(new ListenerManager(this, this, getUtilManager()));

    // Reload utility functions
    getUtilManager().reloadUtils();

    // Load the ScoreboardLibrary and register commands
    loadScoreboardLibrary();
    registerCommands();
  }

  private void loadScoreboardLibrary() {
    // Load ScoreboardLibrary
    try {
      scoreboardLibrary = ScoreboardLibrary.loadScoreboardLibrary(this);
    } catch (Exception e) {
      getLogger().log(Level.SEVERE, "Error loading ScoreboardLibrary!", e);
      scoreboardLibrary = new NoopScoreboardLibrary();
    }
    setScoreboardLibrary(scoreboardLibrary);
  }

  public boolean setupEconomy() {
    // Check for Vault availability
    if (getServer().getPluginManager().getPlugin("Vault") == null) {
      getLogger().log(Level.SEVERE, "Vault not found. Economy disabled.");
      return false;
    }

    // Setup economy service
    RegisteredServiceProvider<Economy> registeredServiceProvider = getServer().getServicesManager().getRegistration(Economy.class);
    if (registeredServiceProvider == null) {
      getLogger().log(Level.SEVERE, "Economy service provider not found. Economy disabled.");
      return false;
    }

    economy = registeredServiceProvider.getProvider();
    if (this.economy == null) {
      getLogger().log(Level.SEVERE, "Failed to get Economy provider. Economy disabled.");
      return false;
    }

    return true;
  }
}

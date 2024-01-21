package io.github.divinerealms.footcube;

import io.github.divinerealms.footcube.commands.BaseCommand;
import io.github.divinerealms.footcube.commands.ClearCubeCommand;
import io.github.divinerealms.footcube.commands.CommandDisabler;
import io.github.divinerealms.footcube.commands.CubeCommand;
import io.github.divinerealms.footcube.configs.Config;
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

import java.util.logging.Level;

@Getter
@Setter
public class Footcube extends JavaPlugin {
  private final ConfigManager configManager = new ConfigManager(this, "");
  private Config config;
  private UtilManager utilManager;
  private ListenerManager listenerManager;
  public ScoreboardLibrary scoreboardLibrary;
  private Economy economy;

  public Footcube(final UtilManager utilManager, final ListenerManager listenerManager) {
    this.utilManager = utilManager;
    this.listenerManager = listenerManager;
  }

  @Override
  public void onEnable() {
    // Initialize Config and setup plugin
    config = new Config(this);
    setup();

    // Log plugin information
    getUtilManager().getLogger().sendBanner();
    getLogger().info("Loading commands...");
    getLogger().info("Loading listeners...");
    getLogger().info("Successfully enabled!");

    // Reload configuration
    getConfig().reload();
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
    getConfig().reload();
    setup();
  }

  public void setup() {
    // Save default configuration
    saveDefaultConfig();

    if (!setupEconomy())
      getLogger().log(Level.SEVERE, "Vault not found. Economy disabled.");

    // Initialize managers and components
    setUtilManager(new UtilManager(this));
    setListenerManager(new ListenerManager(this, this, getUtilManager()));
    getUtilManager().reloadUtils();
    loadScoreboardLibrary();
    registerCommands();

    // Unregister and register listeners
    if (getListenerManager() != null && getListenerManager().isRegistered())
      getListenerManager().unregisterListeners();
    if (getListenerManager() != null) getListenerManager().registerListeners();

    // Schedule repeating task
    getServer().getScheduler().runTaskTimer(this, getUtilManager().getPhysics()::update, 20L, 1L);
  }

  private void registerCommands() {
    // Register plugin commands
    getCommand("footcube").setExecutor(new BaseCommand(this, getUtilManager()));
    getCommand("cube").setExecutor(new CubeCommand(getUtilManager()));
    getCommand("clearcube").setExecutor(new ClearCubeCommand(getUtilManager()));
    getCommand("commanddisabler").setExecutor(new CommandDisabler(getUtilManager()));
  }

  private void shutdown() {
    // Shutdown tasks
    getUtilManager().getPhysics().removeCubes();
    getServer().getScheduler().cancelTasks(this);
    getServer().getMessenger().unregisterIncomingPluginChannel(this);
    getListenerManager().unregisterListeners();
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
    if (getServer().getPluginManager().getPlugin("Vault") == null) return false;
    RegisteredServiceProvider<Economy> registeredServiceProvider = getServer().getServicesManager().getRegistration(Economy.class);
    if (registeredServiceProvider == null) return false;
    economy = registeredServiceProvider.getProvider();
    return this.economy != null;
  }
}

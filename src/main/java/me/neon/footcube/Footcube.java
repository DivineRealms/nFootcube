package me.neon.footcube;

import me.neon.footcube.utils.Configuration;
import me.neon.footcube.utils.Manager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public class Footcube extends JavaPlugin {
  private Manager manager;
  private Configuration configuration;

  @Override
  public void onDisable() {
    this.configuration.saveMessages();
  }

  @Override
  public void onEnable() {
    long timeAtStart = System.currentTimeMillis();
    this.configuration = new Configuration(this);
    this.manager = new Manager(this);
    this.saveDefaultConfig();
    this.reload();
    this.getLogger().log(Level.INFO, "Loading commands...");
    this.getLogger().log(Level.INFO, "Loading listeners...");
    this.manager.getLogger().setLogo();
    this.getLogger().log(Level.INFO, "Successfully enabled! (took " + (System.currentTimeMillis() - timeAtStart) + "ms)");

    this.getServer().getPluginManager().registerEvents(this.manager.getController(), this);
    this.getServer().getScheduler().runTaskTimer(this, () -> this.manager.getController().update(), 0L, 1L);
  }

  public void reload() {
    this.reloadConfig();
    this.configuration.reloadMessages();
    this.configuration.get().options().copyDefaults(true);

    this.getCommand("nfootcube").setExecutor(this.manager.getCommands());
    this.getCommand("nfootcube").setTabCompleter(this.manager.getCommands());
    this.getCommand("cube").setExecutor(this.manager.getCommands());
    this.getCommand("clearcube").setExecutor(this.manager.getCommands());

    this.manager.getController().removeUnusedCubes();
  }
}


package me.neon.footcube;

import me.neon.footcube.listeners.Controller;
import me.neon.footcube.utils.Commands;
import me.neon.footcube.utils.Configuration;
import me.neon.footcube.utils.Manager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public class Footcube extends JavaPlugin {
  private Manager manager;
  private Controller controller;
  private Configuration configuration;

  @Override
  public void onDisable() {
    this.configuration.saveMessages();
    this.getController().removeCubes();
  }

  @Override
  public void onEnable() {
    long timeAtStart = System.currentTimeMillis();

    this.configuration = new Configuration(this);
    this.controller = new Controller(this);

    this.saveDefaultConfig();
    this.reload();

    this.getLogger().log(Level.INFO, "Loading commands...");
    this.getLogger().log(Level.INFO, "Loading listeners...");
    this.manager.getLogger().setLogo();
    this.getLogger().log(Level.INFO, "Successfully enabled! (took " + (System.currentTimeMillis() - timeAtStart) + "ms)");

    this.getServer().getPluginManager().registerEvents(this.getController(), this);
    this.getServer().getScheduler().runTaskTimer(this, this.getController()::update, 0L, 1L);
  }

  public void reload() {
    this.reloadConfig();
    this.configuration.reloadMessages();
    this.configuration.get().options().copyDefaults(true);

    this.manager = new Manager(this);

    Commands commands = new Commands(this, this.manager, this.configuration);
    this.getCommand("nfootcube").setExecutor(commands);
    this.getCommand("nfootcube").setTabCompleter(commands);
    this.getCommand("cube").setExecutor(commands);
    this.getCommand("clearcube").setExecutor(commands);
  }

  public Controller getController() {
    return this.controller;
  }
}


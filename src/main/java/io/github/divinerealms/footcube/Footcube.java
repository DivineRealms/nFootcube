package io.github.divinerealms.footcube;

import io.github.divinerealms.footcube.listeners.Controller;
import io.github.divinerealms.footcube.utils.Commands;
import io.github.divinerealms.footcube.utils.Configuration;
import io.github.divinerealms.footcube.utils.Manager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public class Footcube extends JavaPlugin {
  private Manager manager;
  private Controller controller;
  private Configuration configuration;
  private long timeAtStart = System.currentTimeMillis();

  @Override
  public void onDisable() {
    this.saveConfig();
    this.configuration.saveMessages();
    this.getController().removeCubes();
  }

  @Override
  public void onEnable() {
    this.configuration = new Configuration(this);
    this.controller = new Controller(this);

    this.saveDefaultConfig();
    this.restart();
  }

  public void reload() {
    this.timeAtStart = System.currentTimeMillis();

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

  public void restart() {
    this.timeAtStart = System.currentTimeMillis();
    this.reload();

    this.getLogger().log(Level.INFO, "Loading commands...");
    this.getLogger().log(Level.INFO, "Loading listeners...");
    this.manager.getLogger().setLogo();
    this.getLogger().log(Level.INFO, "Successfully enabled! (took " + (System.currentTimeMillis() - getTimeAtStart()) + "ms)");

    this.getServer().getPluginManager().registerEvents(this.getController(), this);
    this.getServer().getScheduler().runTaskTimer(this, this.getController()::update, 0L, 1L);
  }

  public Controller getController() {
    return this.controller;
  }

  public long getTimeAtStart() {
    return this.timeAtStart;
  }
}


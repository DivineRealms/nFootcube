package io.github.divinerealms.footcube;

import io.github.divinerealms.footcube.commands.BaseCommand;
import io.github.divinerealms.footcube.utils.Configuration;
import io.github.divinerealms.footcube.utils.Manager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public class Footcube extends JavaPlugin {
  private Configuration configuration;
  private Manager manager;
  private long timeAtStart = System.currentTimeMillis();

  @Override
  public void onDisable() {
    this.manager.getController().removeCubes();
    this.configuration.saveMessages();
  }

  @Override
  public void onEnable() {
    this.saveDefaultConfig();

    this.configuration = new Configuration(this);
    this.manager = new Manager(this, this.configuration);
    this.setup();
  }

  public void reload() {
    this.timeAtStart = System.currentTimeMillis();

    this.reloadConfig();
    this.configuration.reloadMessages();
    this.configuration.get().options().copyDefaults(true);

    BaseCommand commands = new BaseCommand(this.manager, this.configuration);
    this.getCommand("nfootcube").setExecutor(commands);
    this.getCommand("nfootcube").setTabCompleter(commands);
  }

  public void setup() {
    this.timeAtStart = System.currentTimeMillis();
    this.reload();

    this.getLogger().log(Level.INFO, "Loading commands...");
    this.getLogger().log(Level.INFO, "Loading listeners...");
    this.manager.getLogger().setLogo();
    this.getLogger().log(Level.INFO, "Successfully enabled! (took " + (System.currentTimeMillis() - getTimeAtStart()) + "ms)");

    this.getServer().getPluginManager().registerEvents(this.manager.getController(), this);
    this.getServer().getScheduler().runTaskTimer(this, this.manager.getController()::update, 1L, 1L);
  }

  public long getTimeAtStart() {
    return this.timeAtStart;
  }
}


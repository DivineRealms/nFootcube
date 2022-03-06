package io.github.divinerealms.footcube.configs;

import io.github.divinerealms.footcube.managers.ConfigManager;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

public class Match extends ConfigManager {
  @Getter private final String name = "match.yml";
  @Getter private final Plugin plugin;
  @Getter private final ConsoleCommandSender consoleSender;
  @Getter @Setter private FileConfiguration match;

  public Match(final Plugin plugin) {
    super(plugin, "match.yml");
    this.plugin = plugin;
    this.consoleSender = plugin.getServer().getConsoleSender();
  }

  public void reload() {
    reloadConfig(getName());
    setMatch(getConfig(getName()));
  }

  public int getInt(final String path) {
    return getMatch().getInt(path, 0);
  }

  public double getDouble(final String path) {
    return getMatch().getDouble(path, 0);
  }

  public void setInt(final String path, final int value) {
    getMatch().set(path, value);
  }

  public void setDouble(final String path, final double value) {
    getMatch().set(path, value);
  }
}

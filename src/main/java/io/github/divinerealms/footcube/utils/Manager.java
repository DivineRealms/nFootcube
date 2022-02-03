package io.github.divinerealms.footcube.utils;

import io.github.divinerealms.footcube.Footcube;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

public class Manager {
  private final Footcube plugin;
  private final Configuration configuration;
  private final Logger logger;
  private final String prefix;

  public Manager(Footcube plugin) {
    this.plugin = plugin;
    this.configuration = new Configuration(this.plugin);
    this.logger = new Logger(plugin, this);
    this.prefix = this.configuration.get().getString("PREFIX");
  }

  public String color(String message) {
    message = this.configuration.get().getString(message);
    message = message
        .replace("%prefix%", this.prefix)
        .replace("%time%", String.valueOf(System.currentTimeMillis() - this.plugin.getTimeAtStart()));
    return ChatColor.translateAlternateColorCodes('&', message);
  }

  public String color(String message, String permission) {
    message = this.configuration.get().getString(message);
    message = message
        .replace("%prefix%", this.prefix)
        .replace("%permission%", permission)
        .replace("%time%", String.valueOf(System.currentTimeMillis() - this.plugin.getTimeAtStart()));
    return ChatColor.translateAlternateColorCodes('&', message);
  }

  public List<String> color(List<String> list) {
    List<String> colorized = new ArrayList<>();

    for (String line : list)
      colorized.add(ChatColor.translateAlternateColorCodes('&', line));

    return colorized;
  }

  public Logger getLogger() {
    return this.logger;
  }
}

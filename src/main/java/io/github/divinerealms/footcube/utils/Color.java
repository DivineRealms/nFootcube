package io.github.divinerealms.footcube.utils;

import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

public class Color {
  private final Manager manager;
  private final Configuration configuration;
  private final String prefix;

  public Color(Manager manager, Configuration configuration) {
    this.manager = manager;
    this.configuration = configuration;
    this.prefix = this.configuration.get().getString("PREFIX");
  }

  public String color(String message) {
    message = this.configuration.get().getString(message);
    message = message
        .replace("%prefix%", this.prefix)
        .replace("%time%", String.valueOf(System.currentTimeMillis() - this.manager.getPlugin().getTimeAtStart()));
    return ChatColor.translateAlternateColorCodes('&', message);
  }

  public String color(String message, String permission) {
    message = this.configuration.get().getString(message);
    message = message
        .replace("%prefix%", this.prefix)
        .replace("%permission%", permission)
        .replace("%time%", String.valueOf(System.currentTimeMillis() - this.manager.getPlugin().getTimeAtStart()));
    return ChatColor.translateAlternateColorCodes('&', message);
  }

  public List<String> color(List<String> list) {
    List<String> colorized = new ArrayList<>();

    for (String line : list)
      colorized.add(ChatColor.translateAlternateColorCodes('&', line));

    return colorized;
  }
}

package io.github.divinerealms.footcube.utils;

import io.github.divinerealms.footcube.Footcube;
import io.github.divinerealms.footcube.managers.ConfigManager;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

public class Color {
  @Getter private final Footcube plugin;
  @Getter private final FileConfiguration lang;
  @Getter private String prefix;

  public Color(final Footcube plugin, final ConfigManager configManager) {
    this.plugin = plugin;
    this.lang = configManager.getConfig("messages.yml");
    this.prefix = getLang().getString("PREFIX");
  }

  public void reload() {
    this.prefix = getLang().getString("PREFIX");
  }

  public String color(String message) {
    message = getLang().getString(message);
    message = message
        .replace("%prefix%", getPrefix())
        .replace("%time%", String.valueOf(System.currentTimeMillis() - getPlugin().getTimeAtStart()));
    return ChatColor.translateAlternateColorCodes('&', message);
  }

  public String color(String message, String permission) {
    message = getLang().getString(message);
    message = message
        .replace("%prefix%", getPrefix())
        .replace("%permission%", permission)
        .replace("%time%", String.valueOf(System.currentTimeMillis() - getPlugin().getTimeAtStart()));
    return ChatColor.translateAlternateColorCodes('&', message);
  }

  public List<String> color(List<String> list) {
    final List<String> colorized = new ArrayList<>();

    for (String line : list)
      colorized.add(ChatColor.translateAlternateColorCodes('&', line));

    return colorized;
  }
}

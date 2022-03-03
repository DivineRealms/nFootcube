package io.github.divinerealms.footcube.utils;

import io.github.divinerealms.footcube.Footcube;
import io.github.divinerealms.footcube.managers.ConfigManager;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class Messages extends ConfigManager {
  @Getter private final Footcube plugin;
  @Getter private FileConfiguration messages;
  @Getter private String prefix;

  public Messages(final Footcube plugin) {
    super(plugin, "messages.yml", true);
    this.plugin = plugin;
  }

  public void reload() {
    reloadConfig("messages.yml");
    this.messages = getConfig("messages.yml");
    this.prefix = getMessages().getString("PREFIX");
  }

  public void send(final Player player, final String path) {
    player.sendMessage(colorize(path));
  }

  public void send(final Player player, final String path, final String permission) {
    if (permission.isEmpty()) player.sendMessage(colorize(path));
    else player.sendMessage(colorize(path, permission));
  }

  public void sendLong(final Player player, final String path) {
    final List<String> list = getMessages().getStringList(path);
    for (final String message : list)
      player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
  }

  public String colorize(final String path) {
    final String message = getString(path)
        .replace("%prefix%", getPrefix());
    return ChatColor.translateAlternateColorCodes('&', message);
  }

  public String colorize(final String path, final String permission) {
    final String message = getString(path)
        .replace("%prefix%", getPrefix())
        .replace("%permission%", permission);
    return ChatColor.translateAlternateColorCodes('&', message);
  }

  public String getString(final String path) {
    return getMessages().getString(path);
  }

  public List<String> getStringList(final String path) {
    final List<String> list = getMessages().getStringList(path);
    return new ArrayList<>(list);
  }

  public int getInt(final String path) {
    return getMessages().getInt(path);
  }

  public double getDouble(final String path) {
    return getMessages().getDouble(path);
  }
}

package io.github.divinerealms.footcube.utils;

import io.github.divinerealms.footcube.managers.ConfigManager;
import io.github.divinerealms.footcube.managers.UtilManager;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.List;

@SuppressWarnings("unused")
public class Chatty {
  @Getter private final UtilManager utilManager;
  @Getter private final ConfigManager configManager;
  @Getter private final FileConfiguration lang;

  public Chatty(final UtilManager utilManager, final ConfigManager configManager) {
    this.utilManager = utilManager;
    this.configManager = configManager;
    this.lang = configManager.getConfig("messages.yml");
  }

  public void reload() {
    getConfigManager().reloadConfig("messages.yml");
  }

  public void send(final Player player, final String path) {
    player.sendMessage(getUtilManager().getColor().color(path));
  }

  public void send(final Player player, final String path, final String permission) {
    if (permission.isEmpty()) player.sendMessage(getUtilManager().getColor().color(path));
    else player.sendMessage(getUtilManager().getColor().color(path, permission));
  }

  public void sendLong(final Player player, final String path) {
    for (final String message : getStringList(path))
      player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
  }

  public List<String> getStringList(final String path) {
    return getLang().getStringList(path);
  }
}

package io.github.divinerealms.footcube.utils;

import io.github.divinerealms.footcube.managers.ConfigManager;
import io.github.divinerealms.footcube.managers.UtilManager;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

@SuppressWarnings("unused")
public class Chatty {
  @Getter private final UtilManager utilManager;
  @Getter private FileConfiguration messages;

  public Chatty(final UtilManager utilManager) {
    this.utilManager = utilManager;
  }

  public void reload(final ConfigManager configManager) {
    this.messages = configManager.getConfig("messages.yml");
  }

  public void send(final Player player, final String path) {
    player.sendMessage(getFormat().color(path));
  }

  public void send(final Player player, final String path, final String permission) {
    if (permission.isEmpty()) player.sendMessage(getFormat().color(path));
    else player.sendMessage(getFormat().color(path, permission));
  }

  public void sendLong(final Player player, final String path) {
    for (final String message : getMessages().getStringList(path))
      player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
  }
}

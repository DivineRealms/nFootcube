package io.github.divinerealms.footcube.utils;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;

public class Message {
  private final Manager manager;
  private final Configuration configuration;

  public Message(final Manager manager, final Configuration configuration) {
    this.manager = manager;
    this.configuration = configuration;
  }

  public void send(final Player player, final String path) {
    player.sendMessage(this.manager.getColor().color(path));
  }

  public void send(final Player player, final String path, final String permission) {
    if (permission.equals("")) player.sendMessage(this.manager.getColor().color(path));
    else player.sendMessage(this.manager.getColor().color(path, permission));
  }

  public void sendLong(final Player player, final String path) {
    for (final String message : this.getStringList(path))
      player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
  }

  public List<String> getStringList(final String path) {
    return this.configuration.get("messages.yml").getStringList(path);
  }
}

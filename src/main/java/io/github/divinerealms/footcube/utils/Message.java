package io.github.divinerealms.footcube.utils;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;

public class Message {
  private final Manager manager;
  private final Configuration configuration;

  public Message(Manager manager, Configuration configuration) {
    this.manager = manager;
    this.configuration = configuration;
  }

  public void send(Player player, String path) {
    player.sendMessage(this.manager.getColor().color(path));
  }

  public void send(Player player, String path, String permission) {
    if (permission.equals("")) player.sendMessage(this.manager.getColor().color(path));
    else player.sendMessage(this.manager.getColor().color(path, permission));
  }

  public void sendLong(Player player, String path) {
    for (String message : this.getStringList(path))
      player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
  }

  public List<String> getStringList(String path) {
    return this.configuration.get().getStringList(path);
  }
}

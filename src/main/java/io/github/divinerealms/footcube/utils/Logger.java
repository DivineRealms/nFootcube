package io.github.divinerealms.footcube.utils;

import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class Logger {
  private final Server server;
  private final PluginDescriptionFile description;
  private final List<String> banner = new ArrayList<>();
  private final ConsoleCommandSender consoleSender;

  public Logger(final Plugin plugin) {
    this.server = plugin.getServer();
    this.description = plugin.getDescription();
    this.consoleSender = server.getConsoleSender();
  }

  public void send(final CommandSender sender, final String message) {
    if (sender instanceof Player) sender.sendMessage(message);
    else getConsoleSender().sendMessage(message);
  }

  public void send(final String rank, final String message) {
    getServer().broadcast(message, "group." + rank);
    getConsoleSender().sendMessage(message);
  }

  public void sendBanner() {
    final List<String> authors = getDescription().getAuthors();
    final String formattedAuthors = authors.stream().map(String::valueOf).collect(Collectors.joining(", "));
    final String pluginName = getDescription().getFullName();
    final String serverName = getServer().getName();
    final String version = getServer().getBukkitVersion();
    final String serverNameVersion = serverName + " - " + version;

    getBanner().add("&9     __");
    getBanner().add("&3  .&9'&f\".'\"&9'&3.   &2" + pluginName);
    getBanner().add("&b :.&f_.\"\"._&b.:  &5Authors: &d" + formattedAuthors);
    getBanner().add("&3 :  &f\\__/&3  :");
    getBanner().add("&b  '.&f/  \\&b.'   &8Running on " + serverNameVersion);
    getBanner().add("&9     \"\"");

    for (final String message : getBanner())
      getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', message));
  }
}

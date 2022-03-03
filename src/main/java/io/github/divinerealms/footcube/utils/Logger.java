package io.github.divinerealms.footcube.utils;

import io.github.divinerealms.footcube.managers.UtilManager;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class Logger {
  @Getter private final Plugin plugin;
  @Getter private final UtilManager utilManager;
  @Getter private final List<String> banner = new ArrayList<>();
  @Getter private final ConsoleCommandSender consoleSender;
  @Getter private final Messages messages;
  @Getter private String prefix;

  public Logger(final Plugin plugin, final UtilManager utilManager) {
    this.plugin = plugin;
    this.utilManager = utilManager;
    this.consoleSender = plugin.getServer().getConsoleSender();
    this.messages = utilManager.getMessages();
  }

  public void reload() {
    this.prefix = getMessages().getString("PREFIX");
  }

  public void send(final String path) {
    final String message = getMessages().colorize(path);
    getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', message));
  }

  public void info(String message) {
    message = getPrefix() + message;
    getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', message));
  }

  public void sendLong(final String path) {
    final List<String> list = getMessages().getStringList(path);
    for (String message : list)
      getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', message));
  }

  public void sendBanner() {
    final List<String> authors = getPlugin().getDescription().getAuthors();
    final String formattedAuthors = authors.stream().map(String::valueOf).collect(Collectors.joining(", "));
    final String pluginName = getPlugin().getDescription().getFullName();
    final String serverName = getPlugin().getServer().getName();
    final String version = getPlugin().getServer().getBukkitVersion();
    final String serverNameVersion = serverName + " - " + version;

    getBanner().add("&9     __");
    getBanner().add("&3  .&9'&f\".'\"&9'&3.   &2" + pluginName);
    getBanner().add("&b :.&f_.\"\"._&b.:  &5Authors: &d" + formattedAuthors);
    getBanner().add("&3 :  &f\\__/&3  :");
    getBanner().add("&b  '.&f/  \\&b.'   &8Running on " + serverNameVersion);
    getBanner().add("&9     \"\"");

    for (String message : getBanner()) {
      message = ChatColor.translateAlternateColorCodes('&', message);
      getConsoleSender().sendMessage(message);
    }
  }
}

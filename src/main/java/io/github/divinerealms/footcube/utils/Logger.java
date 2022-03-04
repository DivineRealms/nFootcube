package io.github.divinerealms.footcube.utils;

import io.github.divinerealms.footcube.managers.UtilManager;
import lombok.Getter;
import org.bukkit.Server;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class Logger {
  @Getter private final Server server;
  @Getter private final PluginDescriptionFile description;
  @Getter private final List<String> banner = new ArrayList<>();
  @Getter private final ConsoleCommandSender consoleSender;
  @Getter private final Messages messages;
  @Getter private String prefix;

  public Logger(final Plugin plugin, final UtilManager utilManager) {
    this.server = plugin.getServer();
    this.description = plugin.getDescription();
    this.consoleSender = server.getConsoleSender();
    this.messages = utilManager.getMessages();
  }

  public void reload() {
    this.prefix = getMessages().getString("prefix");
  }

  public void info(final String message) {
    getConsoleSender().sendMessage(getMessages().colorizeMessage(getPrefix() + message));
  }

  public void sendLong(final String path) {
    final List<String> list = getMessages().getStringList(path);
    for (String message : list)
      getConsoleSender().sendMessage(getMessages().colorizeMessage(message));
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
      getConsoleSender().sendMessage(getMessages().colorizeMessage(message));
  }
}

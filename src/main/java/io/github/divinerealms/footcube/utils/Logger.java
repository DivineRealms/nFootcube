package io.github.divinerealms.footcube.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Logger {
  private final Manager manager;
  private final Configuration configuration;
  private final List<String> logo = new ArrayList<>();

  public Logger(final Manager manager, final Configuration configuration) {
    this.manager = manager;
    this.configuration = configuration;
  }

  public void info(String path) {
    path = this.manager.getColor().color(path);
    Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', path));
  }

  public void sendLong(String path) {
    List<String> list = this.configuration.get("messages.yml").getStringList(path);
    list = this.manager.getColor().color(list);

    for (String messages : list)
      Bukkit.getConsoleSender().sendMessage(messages);
  }

  public void debug(String message) {
    this.info("[" + this.manager.getPlugin().getName() + "] &9[DEBUG] &r" + message);
  }

  public void error(String message) {
    this.info("[" + this.manager.getPlugin().getName() + "] &c[ERROR] &c" + message);
  }

  public void setLogo() {
    final List<String> authors = this.manager.getPlugin().getDescription().getAuthors();
    final String formattedAuthors = authors.stream().map(String::valueOf).collect(Collectors.joining(", "));

    this.logo.add("&9     __");
    this.logo.add("&3  .&9'&f\".'\"&9'&3.   &2" + this.manager.getPlugin().getName() + " &bv" + this.manager.getPlugin().getDescription().getVersion());
    this.logo.add("&b :.&f_.\"\"._&b.:  &5Authors: &d" + formattedAuthors);
    this.logo.add("&3 :  &f\\__/&3  :");
    this.logo.add("&b  '.&f/  \\&b.'   &8Running on " + this.manager.getPlugin().getServer().getName() + " - " + this.manager.getPlugin().getServer().getBukkitVersion());
    this.logo.add("&9     \"\"");

    for (String messages : this.logo) {
      messages = ChatColor.translateAlternateColorCodes('&', messages);
      Bukkit.getConsoleSender().sendMessage(messages);
    }
  }
}

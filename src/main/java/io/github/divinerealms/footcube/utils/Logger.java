package io.github.divinerealms.footcube.utils;

import io.github.divinerealms.footcube.Footcube;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class Logger {
  private final Footcube plugin;
  private final Manager manager;
  private final Configuration configuration;
  private final List<String> logo = new ArrayList<>();

  public Logger(Footcube plugin, Manager manager) {
    this.plugin = plugin;
    this.manager = manager;
    this.configuration = new Configuration(this.plugin);
  }

  public void log(String path) {
    path = this.manager.color(path);
    Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', path));
  }

  public void logL(String path) {
    List<String> list = this.configuration.get().getStringList(path);
    list = this.manager.color(list);

    for (String messages : list)
      Bukkit.getConsoleSender().sendMessage(messages);
  }

  public void debug(String message) {
    this.log("[" + this.plugin.getName() + "] &9[DEBUG] &r" + message);
  }

  public void error(String message) {
    this.log("[" + this.plugin.getName() + "] &c[ERROR] &c" + message);
  }

  public void setLogo() {
    List<String> authors = this.plugin.getDescription().getAuthors();
    String formattedAuthors = authors.stream().map(String::valueOf).collect(Collectors.joining(", "));

    this.logo.add("&9     __");
    this.logo.add("&3  .&9'&f\".'\"&9'&3.   &2" + this.plugin.getName() + " &bv" + this.plugin.getDescription().getVersion());
    this.logo.add("&b :.&f_.\"\"._&b.:  &5Authors: &d" + formattedAuthors);
    this.logo.add("&3 :  &f\\__/&3  :");
    this.logo.add("&b  '.&f/  \\&b.'   &8Running on " + this.plugin.getServer().getName() + " - " + this.plugin.getServer().getBukkitVersion());
    this.logo.add("&9     \"\"");

    for (String messages : this.logo) {
      messages = ChatColor.translateAlternateColorCodes('&', messages);
      Bukkit.getConsoleSender().sendMessage(messages);
    }
  }
}

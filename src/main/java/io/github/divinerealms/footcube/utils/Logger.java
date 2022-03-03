package io.github.divinerealms.footcube.utils;

import io.github.divinerealms.footcube.managers.UtilManager;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class Logger {
  @Getter private final Plugin plugin;
  @Getter private final List<String> logo = new ArrayList<>();
  @Getter private FileConfiguration messages;

  public Logger(final Plugin plugin, final UtilManager utilManager) {
    this.plugin = plugin;
  }

  public void reload() {
    this.messages = utilManager.getMessages().getLang();
    this.format =
  }

  public void info(String path) {
    path = getUtilManager().getColor().color(path);
    getPlugin().getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', path));
  }

  public void sendLong(String path) {
    List<String> list = getMessages().getStringList(path);
    list = getUtilManager().getColor().color(list);

    for (String messages : list)
      getPlugin().getServer().getConsoleSender().sendMessage(messages);
  }

  public void debug(String message) {
    info("[" + getPlugin().getName() + "] &9[DEBUG] &r" + message);
  }

  public void error(String message) {
    info("[" + getPlugin().getName() + "] &c[ERROR] &c" + message);
  }

  public void setLogo() {
    final List<String> authors = getPlugin().getDescription().getAuthors();
    final String formattedAuthors = authors.stream().map(String::valueOf).collect(Collectors.joining(", "));

    getLogo().add("&9     __");
    getLogo().add("&3  .&9'&f\".'\"&9'&3.   &2" + getPlugin().getName() + " &bv" + getPlugin().getDescription().getVersion());
    getLogo().add("&b :.&f_.\"\"._&b.:  &5Authors: &d" + formattedAuthors);
    getLogo().add("&3 :  &f\\__/&3  :");
    getLogo().add("&b  '.&f/  \\&b.'   &8Running on " + getPlugin().getServer().getName() + " - " + getPlugin().getServer().getBukkitVersion());
    getLogo().add("&9     \"\"");

    for (String messages : getLogo()) {
      messages = ChatColor.translateAlternateColorCodes('&', messages);
      getPlugin().getServer().getConsoleSender().sendMessage(messages);
    }
  }
}

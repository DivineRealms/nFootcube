package io.github.divinerealms.footcube.utils;

import io.github.divinerealms.footcube.Footcube;
import io.github.divinerealms.footcube.managers.ConfigManager;
import io.github.divinerealms.footcube.managers.UtilManager;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class Logger {
  @Getter private final Footcube plugin;
  @Getter private final UtilManager utilManager;
  @Getter private final ConfigManager configManager;
  @Getter private final FileConfiguration lang;
  @Getter private final List<String> logo = new ArrayList<>();

  public Logger(final Footcube plugin, final UtilManager manager, final ConfigManager configManager) {
    this.plugin = plugin;
    this.utilManager = manager;
    this.configManager = configManager;
    this.lang = configManager.getConfig("messages.yml");
  }

  public void reload() {
    getConfigManager().reloadConfig("messages.yml");
  }

  public void info(String path) {
    path = getUtilManager().getColor().color(path);
    getPlugin().getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', path));
  }

  public void sendLong(String path) {
    List<String> list = getLang().getStringList(path);
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

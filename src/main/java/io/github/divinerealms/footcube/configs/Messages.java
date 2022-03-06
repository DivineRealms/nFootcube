package io.github.divinerealms.footcube.configs;

import io.github.divinerealms.footcube.managers.ConfigManager;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class Messages extends ConfigManager {
  @Getter private final String name = "messages.yml";
  @Getter private final ConsoleCommandSender consoleSender;
  @Getter @Setter private FileConfiguration messages;
  @Getter @Setter private String prefix, ballHitsDebug;

  public Messages(final Plugin plugin) {
    super(plugin, "messages.yml");
    this.consoleSender = getPlugin().getServer().getConsoleSender();
  }

  public void reload() {
    reloadConfig(getName());
    setMessages(getConfig(getName()));
    setPrefix(getString("prefix"));
    setBallHitsDebug(getString("debug.ball-hits"));
  }

  public void send(final CommandSender sender, final String path) {
    if (sender instanceof Player) {
      final Player player = (Player) sender;
      player.sendMessage(colorize(path));
    } else getConsoleSender().sendMessage(colorize(path));
  }

  public void send(final Player player, final String path, final String permission) {
    if (permission.isEmpty()) player.sendMessage(colorize(path));
    else player.sendMessage(colorize(path, permission));
  }

  public void sendLong(final CommandSender sender, final String path) {
    final List<String> list = getMessages().getStringList(path);
    for (final String message : list) {
      if (sender instanceof Player) {
        final Player player = (Player) sender;
        player.sendMessage(colorizeMessage(message));
      } else getConsoleSender().sendMessage(colorizeMessage(message));
    }
  }

  public String colorizeMessage(final String message) {
    return ChatColor.translateAlternateColorCodes('&', message);
  }

  public String colorize(final String path) {
    final String message = getString(path)
        .replace("%prefix%", getPrefix());
    return ChatColor.translateAlternateColorCodes('&', message);
  }

  public String colorize(final String path, final String permission) {
    final String message = getString(path)
        .replace("%prefix%", getPrefix())
        .replace("%permission%", permission);
    return ChatColor.translateAlternateColorCodes('&', message);
  }

  public String getString(final String path) {
    return getMessages().getString(path, getNotFound(path, getName()));
  }

  public List<String> getStringList(final String path) {
    final List<String> list = getMessages().getStringList(path);
    return new ArrayList<>(list);
  }

  public int getInt(final String path) {
    return getMessages().getInt(path, 0);
  }

  public double getDouble(final String path) {
    return getMessages().getDouble(path, 0);
  }

  public void ballHitsDebug(final String playerName, final String power, final String charge, final String kickpower, final String total) {
    getPlugin().getServer().broadcastMessage(colorizeMessage(getBallHitsDebug()
        .replace("%prefix%", getPrefix())
        .replace("%player_name%", playerName)
        .replace("%power%", power)
        .replace("%charge%", charge)
        .replace("%kickpower%", kickpower)
        .replace("%total%", total)));
  }
}

package io.github.divinerealms.footcube.utils;

import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;

@Getter
public class Logger {
  private final Server server;
  private final PluginDescriptionFile description;
  private final ConsoleCommandSender consoleSender;

  public Logger(final Plugin plugin) {
    this.server = plugin.getServer();
    this.description = plugin.getDescription();
    this.consoleSender = server.getConsoleSender();
  }

  /**
   * Sends a colored message to a command sender.
   *
   * @param sender  The command sender.
   * @param message The message to send.
   */
  public void send(final CommandSender sender, final String message) {
    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
  }

  /**
   * Sends a colored message to all players in a specific rank group.
   *
   * @param rank    The rank group.
   * @param message The message to send.
   */
  public void send(final String rank, final String message) {
    server.broadcast(message, "group." + rank);
    consoleSender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
  }

  /**
   * Sends a banner with plugin information to the console.
   */
  public void sendBanner() {
    String banner = generateBanner();
    consoleSender.sendMessage(banner);
  }

  /**
   * Generates a banner with plugin information.
   *
   * @return The generated banner string.
   */
  private String generateBanner() {
    return ChatColor.BLUE + "     __\n" +
        ChatColor.AQUA + "  .'" + ChatColor.BLUE + "\".'\"'." + ChatColor.AQUA + ".'   " + ChatColor.DARK_GREEN + description.getFullName() + "\n" +
        ChatColor.GOLD + " :." + ChatColor.AQUA + "_.\"\"._" + ChatColor.GOLD + ".:  " + ChatColor.LIGHT_PURPLE + "Authors: " + ChatColor.DARK_PURPLE +
        String.join(", ", description.getAuthors()) + "\n" +
        ChatColor.BLUE + " :  " + ChatColor.AQUA + "\\__/" + ChatColor.BLUE + "  :\n" +
        ChatColor.GOLD + "  '.'/  \\" + ChatColor.GOLD + ".'   " + ChatColor.DARK_GRAY +
        "Running on " + server.getName() + " - " + server.getBukkitVersion() + "\n" +
        ChatColor.BLUE + "     \"\"";
  }
}

package io.github.divinerealms.footcube.utils;

import lombok.Getter;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;

import java.util.stream.Collectors;

@Getter
public class Logger {
  private final Server server;
  private final PluginDescriptionFile description;
  private final ConsoleCommandSender consoleSender;
  private String pluginName, authors, serverVersion;

  public Logger(Plugin plugin) {
    this.server = plugin.getServer();
    this.description = plugin.getDescription();
    this.consoleSender = server.getConsoleSender();
  }

  public void send(CommandSender sender, String message) {
    if (sender instanceof Player) sender.sendMessage(message);
    else getConsoleSender().sendMessage(message);
  }

  public void info(String message) {
    message = ChatColor.translateAlternateColorCodes('&', message);
    String prefix = ChatColor.translateAlternateColorCodes('&', "&3[&bnFootcube&3] &r");
    getConsoleSender().sendMessage(prefix + message);
  }

  public void send(String rank, String message) {
    getServer().broadcast(message, "group." + rank);
    getConsoleSender().sendMessage(message);
  }

  public void sendActionBar(Player player, String message) {
    IChatBaseComponent iChatBaseComponent = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + message + "\"}");
    PacketPlayOutChat packetPlayOutChat = new PacketPlayOutChat(iChatBaseComponent, (byte) 2);
    ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packetPlayOutChat);
  }

  public void broadcastBar(String message) {
    IChatBaseComponent iChatBaseComponent = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + message + "\"}");
    PacketPlayOutChat packetPlayOutChat = new PacketPlayOutChat(iChatBaseComponent, (byte) 2);
    for (Player player : getServer().getOnlinePlayers())
      ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packetPlayOutChat);
  }

  public void initializeStrings() {
    this.pluginName = getDescription().getFullName();
    this.authors = getDescription().getAuthors().stream().map(String::valueOf).collect(Collectors.joining(", "));
    this.serverVersion = getServer().getName() + " - " + getServer().getBukkitVersion();
  }

  public String[] startupBanner() {
    return new String[]{"&9     __", "&3  .&9'&f\".'\"&9'&3.   &2" + getPluginName(), "&b :.&f_.\"\"._&b.:  &5Authors: &d" + getAuthors(), "&3 :  &f\\__/&3  :", "&b  '.&f/  \\&b.'   &8Running on " + getServerVersion(), "&9     \"\""};
  }

  public void sendBanner() {
    initializeStrings();
    for (String line : startupBanner()) {
      getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', line));
    }
  }
}

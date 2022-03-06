package io.github.divinerealms.footcube.commands;

import io.github.divinerealms.footcube.Footcube;
import io.github.divinerealms.footcube.managers.UtilManager;
import io.github.divinerealms.footcube.configs.Messages;
import lombok.Getter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReloadCommand implements CommandExecutor {
  @Getter private final Footcube plugin;
  @Getter private final Messages messages;

  public ReloadCommand(final Footcube footcube, final UtilManager utilManager) {
    this.plugin = footcube;
    this.messages = utilManager.getMessages();
  }

  @Override
  public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
    if (!sender.hasPermission("nfootcube.reload")) {
      getMessages().send((Player) sender, "insufficient-permission", "nfootcube.reload");
      return false;
    } else {
      if (args.length < 2) getMessages().sendLong(sender, "reload.usage");
      else if (args[1].equalsIgnoreCase("all")) {
        getPlugin().reload();
        getMessages().send(sender, "reload.plugin");
      } else if (args[1].equalsIgnoreCase("lang")) {
        getMessages().reload();
        getMessages().send(sender, "reload.lang");
      } else getMessages().send(sender, "unknown-command");
    }
    return true;
  }
}

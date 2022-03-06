package io.github.divinerealms.footcube.commands;

import io.github.divinerealms.footcube.configs.Messages;
import io.github.divinerealms.footcube.managers.UtilManager;
import lombok.Getter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReloadconfigCommand implements CommandExecutor {
  @Getter private final UtilManager utilManager;
  @Getter private final Messages messages;

  public ReloadconfigCommand(final UtilManager utilManager) {
    this.utilManager = utilManager;
    this.messages = utilManager.getMessages();
  }

  @Override
  public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
    if (!sender.hasPermission("nfootcube.reload")) {
      getMessages().send((Player) sender, "insufficient-permission", "nfootcube.reload");
      return false;
    }

    getUtilManager().reloadUtils();
    getMessages().send(sender, "reload.config");
    return true;
  }
}

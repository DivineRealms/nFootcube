package io.github.divinerealms.footcube.commands;

import io.github.divinerealms.footcube.Footcube;
import io.github.divinerealms.footcube.managers.UtilManager;
import io.github.divinerealms.footcube.utils.Messages;
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
    if (!(sender instanceof Player)) getMessages().send(sender, "reload");
    else {
      final Player player = (Player) sender;
      if (!player.hasPermission("nfootcube.reload")) {
        getMessages().send(player, "insufficient-permission", "nfootcube.reload");
        return false;
      } else getMessages().send(player, "reload");
    }
    getPlugin().reload();
    return true;
  }
}

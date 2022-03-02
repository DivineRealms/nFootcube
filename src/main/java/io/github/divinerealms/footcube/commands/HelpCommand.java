package io.github.divinerealms.footcube.commands;

import io.github.divinerealms.footcube.managers.UtilManager;
import lombok.Getter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HelpCommand implements CommandExecutor {
  @Getter private final UtilManager utilManager;

  public HelpCommand(final UtilManager utilManager) {
    this.utilManager = utilManager;
  }

  @Override
  public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
    if (sender instanceof Player) {
      final Player player = (Player) sender;
      getUtilManager().getChatty().sendLong(player, "HELP");
    } else getUtilManager().getLogger().sendLong("HELP");
    return true;
  }
}

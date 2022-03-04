package io.github.divinerealms.footcube.commands;

import io.github.divinerealms.footcube.managers.UtilManager;
import io.github.divinerealms.footcube.utils.Logger;
import io.github.divinerealms.footcube.utils.Messages;
import lombok.Getter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HelpCommand implements CommandExecutor {
  @Getter private final Messages messages;
  @Getter private final Logger logger;

  public HelpCommand(final UtilManager utilManager) {
    this.messages = utilManager.getMessages();
    this.logger = utilManager.getLogger();
  }

  @Override
  public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
    if (sender instanceof Player) getMessages().sendLong((Player) sender, "HELP");
    else getLogger().sendLong("HELP");
    return true;
  }
}

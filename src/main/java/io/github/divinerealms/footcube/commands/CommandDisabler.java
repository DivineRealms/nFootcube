package io.github.divinerealms.footcube.commands;

import io.github.divinerealms.footcube.managers.UtilManager;
import io.github.divinerealms.footcube.utils.Logger;
import io.github.divinerealms.footcube.configs.Messages;
import io.github.divinerealms.footcube.utils.Physics;
import lombok.Getter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandDisabler implements CommandExecutor {
  @Getter
  private final Messages messages;
  @Getter private final Logger logger;
  @Getter private final Physics physics;

  public CommandDisabler(final UtilManager utilManager) {
    this.messages = utilManager.getMessages();
    this.logger = utilManager.getLogger();
    this.physics = utilManager.getPhysics();
  }

  @Override
  public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
    if (args.length < 2 || args[1].equalsIgnoreCase("help")) {
      getMessages().sendLong(sender, "command-disabler.help");
    } else if (args[1].equalsIgnoreCase("add")) {

    } else if (args[1].equalsIgnoreCase("remove")) {

    } else getMessages().send(sender, "unknown-command");
    return true;
  }
}

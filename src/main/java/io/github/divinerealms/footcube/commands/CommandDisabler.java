package io.github.divinerealms.footcube.commands;

import io.github.divinerealms.footcube.configs.Lang;
import io.github.divinerealms.footcube.managers.UtilManager;
import io.github.divinerealms.footcube.utils.Logger;
import io.github.divinerealms.footcube.utils.Physics;
import lombok.Getter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

@Getter
public class CommandDisabler implements CommandExecutor {
  private final Logger logger;
  private final Physics physics;

  public CommandDisabler(final UtilManager utilManager) {
    this.logger = utilManager.getLogger();
    this.physics = utilManager.getPhysics();
  }

  @Override
  public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
    if (args.length < 2 || args[1].equalsIgnoreCase("help")) {
      getLogger().send(sender, Lang.COMMAND_DISABLER_HELP.getMessage(null));
    } else if (args[1].equalsIgnoreCase("add")) {

    } else if (args[1].equalsIgnoreCase("remove")) {

    } else getLogger().send(sender, Lang.UNKNOWN_COMMAND.getMessage(null));
    return true;
  }
}

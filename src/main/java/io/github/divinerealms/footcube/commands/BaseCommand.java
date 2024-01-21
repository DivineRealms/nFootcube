package io.github.divinerealms.footcube.commands;

import io.github.divinerealms.footcube.Footcube;
import io.github.divinerealms.footcube.configs.Lang;
import io.github.divinerealms.footcube.managers.UtilManager;
import io.github.divinerealms.footcube.utils.Logger;
import lombok.Getter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

@Getter
public class BaseCommand implements CommandExecutor {
  private final Footcube plugin;
  private final UtilManager utilManager;
  private final Logger logger;

  public BaseCommand(final Footcube plugin, final UtilManager utilManager) {
    this.plugin = plugin;
    this.utilManager = utilManager;
    this.logger = utilManager.getLogger();
  }

  /**
   * Executes the base command and delegates to subcommands.
   *
   * @param sender  Command sender
   * @param command Command object
   * @param label   Command label
   * @param args    Command arguments
   * @return True if the command was handled successfully
   */
  @Override
  public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
    if (args.length < 1 || args[0].equalsIgnoreCase("help")) {
      // Handle help command
      final HelpCommand helpCommand = new HelpCommand(getUtilManager());
      helpCommand.onCommand(sender, command, label, args);
    } else if (args[0].equalsIgnoreCase("reloadconfig")) {
      // Handle reloadconfig command
      final ReloadconfigCommand reloadconfigCommand = new ReloadconfigCommand(getPlugin(), getUtilManager());
      reloadconfigCommand.onCommand(sender, command, label, args);
    } else if (args[0].equalsIgnoreCase("reload")) {
      // Handle reload command
      final ReloadCommand reloadCommand = new ReloadCommand(getPlugin(), getUtilManager());
      reloadCommand.onCommand(sender, command, label, args);
    } else {
      // Unknown command, send an error message
      getLogger().send(sender, Lang.UNKNOWN_COMMAND.getMessage(new String[]{args[0]}));
    }

    // Command was handled successfully
    return true;
  }
}

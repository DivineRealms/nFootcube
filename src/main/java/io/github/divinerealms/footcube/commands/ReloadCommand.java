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
public class ReloadCommand implements CommandExecutor {
  private final Footcube plugin;
  private final Logger logger;

  public ReloadCommand(final Footcube footcube, final UtilManager utilManager) {
    this.plugin = footcube;
    this.logger = utilManager.getLogger();
  }

  /**
   * Handles the /footcube reload command, allowing the plugin to be reloaded.
   *
   * @param sender  Command sender
   * @param command Command object
   * @param label   Command label
   * @param args    Command arguments
   * @return True if the command was handled successfully
   */
  @Override
  public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
    // Check if the sender has the necessary permission
    if (!sender.hasPermission("footcube.reload")) {
      getLogger().send(sender, Lang.INSUFFICIENT_PERMISSION.getMessage(new String[]{"footcube.reload"}));
      return false;
    }

    // Check the number of arguments provided
    if (args.length < 2) {
      getLogger().send(sender, Lang.RELOAD_USAGE.getMessage(null));
    } else if (args[1].equalsIgnoreCase("confirm")) {
      // Reload the plugin
      getPlugin().reload();
      getLogger().send(sender, Lang.RELOAD_PLUGIN.getMessage(null));
    } else {
      // Unknown command
      getLogger().send(sender, Lang.UNKNOWN_COMMAND.getMessage(null));
    }
    return true;
  }
}

package io.github.divinerealms.footcube.commands;

import io.github.divinerealms.footcube.configs.Lang;
import io.github.divinerealms.footcube.managers.UtilManager;
import io.github.divinerealms.footcube.utils.Logger;
import lombok.Getter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

@Getter
public class HelpCommand implements CommandExecutor {
  private final Logger logger;

  public HelpCommand(final UtilManager utilManager) {
    this.logger = utilManager.getLogger();
  }

  /**
   * Handles the /help command, providing assistance information.
   *
   * @param sender  Command sender
   * @param command Command object
   * @param label   Command label
   * @param args    Command arguments
   * @return True if the command was handled successfully
   */
  @Override
  public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
    // Send help message to the sender
    getLogger().send(sender, Lang.HELP.getMessage(null));
    return true;
  }
}

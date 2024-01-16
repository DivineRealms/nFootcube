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

  @Override
  public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
    if (args.length < 1 || args[0].equalsIgnoreCase("help")) {
      final HelpCommand helpCommand = new HelpCommand(getUtilManager());
      helpCommand.onCommand(sender, command, label, args);
    } else if (args[0].equalsIgnoreCase("reloadconfig")) {
      final ReloadconfigCommand reloadconfigCommand = new ReloadconfigCommand(getPlugin(), getUtilManager());
      reloadconfigCommand.onCommand(sender, command, label, args);
    } else if (args[0].equalsIgnoreCase("reload")) {
      final ReloadCommand reloadCommand = new ReloadCommand(getPlugin(), getUtilManager());
      reloadCommand.onCommand(sender, command, label, args);
    } else getLogger().send(sender, Lang.UNKNOWN_COMMAND.getConfigValue(null));
    return true;
  }
}

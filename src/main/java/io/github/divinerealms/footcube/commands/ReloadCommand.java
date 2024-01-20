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

  @Override
  public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
    if (!sender.hasPermission("footcube.reload")) {
      getLogger().send(sender, Lang.INSUFFICIENT_PERMISSION.getConfigValue(new String[]{"footcube.reload"}));
      return false;
    }

    if (args.length < 2) getLogger().send(sender, Lang.RELOAD_USAGE.getConfigValue(null));
    else if (args[1].equalsIgnoreCase("confirm")) {
      getPlugin().reload();
      getLogger().send(sender, Lang.RELOAD_PLUGIN.getConfigValue(null));
    } else getLogger().send(sender, Lang.UNKNOWN_COMMAND.getConfigValue(null));
    return true;
  }
}

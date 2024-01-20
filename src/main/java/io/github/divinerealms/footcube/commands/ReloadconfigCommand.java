package io.github.divinerealms.footcube.commands;

import io.github.divinerealms.footcube.Footcube;
import io.github.divinerealms.footcube.configs.Config;
import io.github.divinerealms.footcube.configs.Lang;
import io.github.divinerealms.footcube.managers.UtilManager;
import io.github.divinerealms.footcube.utils.Logger;
import lombok.Getter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

@Getter
public class ReloadconfigCommand implements CommandExecutor {
  private final Footcube plugin;
  private final UtilManager utilManager;
  private final Logger logger;
  private final Config config;

  public ReloadconfigCommand(final Footcube plugin, final UtilManager utilManager) {
    this.plugin = plugin;
    this.utilManager = utilManager;
    this.logger = utilManager.getLogger();
    this.config = utilManager.getConfig();
  }

  @Override
  public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
    if (!sender.hasPermission("nfootcube.reload")) {
      getLogger().send(sender, Lang.INSUFFICIENT_PERMISSION.getConfigValue(new String[]{"nfootcube.reload"}));
      return false;
    }

    getPlugin().setupConfigs();
    getConfig().reload();
    getPlugin().setup();
    getLogger().send(sender, Lang.RELOAD_CONFIG.getConfigValue(null));
    return true;
  }
}

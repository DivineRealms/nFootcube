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

  /**
   * Handles the /nfootcube reloadconfig command, allowing the plugin configurations to be reloaded.
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
    if (!sender.hasPermission("nfootcube.reload")) {
      getLogger().send(sender, Lang.INSUFFICIENT_PERMISSION.getMessage(new String[]{"nfootcube.reload"}));
      return false;
    }

    // Reload plugin configurations
    getPlugin().setupConfigs();
    getConfig().reload();
    getPlugin().setup();

    // Inform the sender about the successful reload
    getLogger().send(sender, Lang.RELOAD_CONFIG.getMessage(null));
    return true;
  }
}

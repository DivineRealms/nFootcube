package io.github.divinerealms.footcube.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import io.github.divinerealms.footcube.Footcube;
import io.github.divinerealms.footcube.configs.Lang;
import io.github.divinerealms.footcube.managers.UtilManager;
import io.github.divinerealms.footcube.utils.Logger;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Getter
@CommandAlias("nfootcube|nfc")
public class DefaultCommand extends BaseCommand {
  private final Footcube instance;
  private final UtilManager utilManager;
  private final Logger logger;

  public DefaultCommand(UtilManager utilManager) {
    this.instance = Footcube.getInstance();
    this.utilManager = utilManager;
    this.logger = utilManager.getLogger();
  }

  @Default
  public void onBase(CommandSender sender) {
    if (sender instanceof Player) {
      for (String message : startupBanner()) {
        getLogger().send(sender, ChatColor.translateAlternateColorCodes('&', message));
      }
    } else {
      getLogger().sendBanner();
    }
  }

  @CatchUnknown
  public void onUnknown(CommandSender sender) {
    getLogger().send(sender, Lang.UNKNOWN_COMMAND.getConfigValue(null));
  }

  @Subcommand("help|h")
  public void onHelp(CommandSender sender) {
    getLogger().send(sender, Lang.HELP.getConfigValue(null));
  }

  @Subcommand("reload|rl")
  @CommandPermission("nfootcube.command.reload")
  @CommandCompletion("all|config")
  public void onReload(CommandSender sender, String[] args) {
    if (args.length < 1) {
      getLogger().send(sender, Lang.RELOAD_USAGE.getConfigValue(null));
      return;
    }

    switch (args[0]) {
      case "all":
        getInstance().onEnable();
        getLogger().send(sender, Lang.RELOAD_PLUGIN.getConfigValue(null));
        break;
      case "config":
        getInstance().setupConfig();
        getLogger().send(sender, Lang.RELOAD_CONFIG.getConfigValue(null));
        break;
      default:
        getLogger().send(sender, Lang.RELOAD_USAGE.getConfigValue(null));
        break;
    }
  }

  private String[] startupBanner() {
    return new String[]{"&8▎ &9    _._", "&8▎ &3  .&9'&f\"'.'\"&9'&3.     &b" + getLogger().getPluginName(), "&8▎ &b :.&f_.\"'\"._&b.:", "&8▎ &3 :  &f\\_/&3  :   &fAuthors: &d" + getLogger().getAuthors(), "&8▎ &b  '.&f/  \\&b.'    &e&nhttps://github.com/DivineRealms/nFootcube", "&8▎ &9    \"'\""};
  }
}

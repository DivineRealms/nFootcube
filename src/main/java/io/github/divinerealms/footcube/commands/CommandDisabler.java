package io.github.divinerealms.footcube.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import io.github.divinerealms.footcube.configs.Lang;
import io.github.divinerealms.footcube.managers.UtilManager;
import io.github.divinerealms.footcube.utils.Logger;
import io.github.divinerealms.footcube.utils.Physics;
import lombok.Getter;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Getter
@CommandAlias("ncommandDisabler|ncd")
public class CommandDisabler extends BaseCommand {
  private final Logger logger;
  private final Physics physics;

  public CommandDisabler(UtilManager utilManager) {
    this.logger = utilManager.getLogger();
    this.physics = utilManager.getPhysics();
  }

  @Default
  @CatchUnknown
  @CommandPermission("footcube.command-disabler")
  @CommandCompletion("help|add|remove")
  public void onCommandDisable(CommandSender sender, String[] args) {
    if (!(sender instanceof Player)) {
      getLogger().send(sender, Lang.INGAME_ONLY.getConfigValue(null));
      return;
    }

    Player player = (Player) sender;

    if (args.length < 1 || args[0].equalsIgnoreCase("help")) {
      getLogger().send(player, Lang.COMMAND_DISABLER_HELP.getConfigValue(null));
      return;
    } else if (args[0].equalsIgnoreCase("add")) {

    } else if (args[0].equalsIgnoreCase("remove")) {

    } else {
      getLogger().send(player, Lang.UNKNOWN_COMMAND.getConfigValue(null));
    }
  }
}

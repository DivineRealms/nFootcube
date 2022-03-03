package io.github.divinerealms.footcube.commands;

import io.github.divinerealms.footcube.Footcube;
import io.github.divinerealms.footcube.managers.UtilManager;
import io.github.divinerealms.footcube.utils.Logger;
import io.github.divinerealms.footcube.utils.Messages;
import lombok.Getter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class BaseCommand implements CommandExecutor, TabCompleter {
  @Getter private final Footcube plugin;
  @Getter private final UtilManager utilManager;
  @Getter private final Logger logger;
  @Getter private final Messages messages;

  public BaseCommand(final Footcube plugin, final UtilManager utilManager) {
    this.plugin = plugin;
    this.utilManager = utilManager;
    this.logger = utilManager.getLogger();
    this.messages = utilManager.getMessages();
  }

  @Override
  public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
    if (args.length < 1 || args[0].equalsIgnoreCase("help")) {
      final HelpCommand helpCommand = new HelpCommand(getUtilManager());
      helpCommand.onCommand(sender, command, label, args);
    } else if (args[0].equalsIgnoreCase("reload")) {
      final ReloadCommand reloadCommand = new ReloadCommand(getPlugin(), getUtilManager());
      reloadCommand.onCommand(sender, command, label, args);
    } else if (args[0].equalsIgnoreCase("cube")) {
      final CubeCommand cubeCommand = new CubeCommand(getUtilManager());
      cubeCommand.onCommand(sender, command, label, args);
    } else if (args[0].equalsIgnoreCase("clearcube")) {
      final ClearCubeCommand clearCubeCommand = new ClearCubeCommand(getPlugin(), getUtilManager());
      clearCubeCommand.onCommand(sender, command, label, args);
    } else {
      if (sender instanceof Player) getMessages().send((Player) sender, "UNKNOWN_COMMAND");
      else getLogger().send("UNKNOWN_COMMAND");
    }
    return true;
  }

  @Override
  public List<String> onTabComplete(final CommandSender sender, final Command command, final String alias, final String[] args) {
    if (!(sender instanceof Player)) return null;
    else {
      final List<String> list = new ArrayList<>();
      if (args.length == 1) {
        if (sender.hasPermission("nfootcube.admin")) {
          list.add("reload");
          list.add("cube");
          list.add("clearcube");
        }
      }
      return list;
    }
  }
}

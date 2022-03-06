package io.github.divinerealms.footcube.commands;

import io.github.divinerealms.footcube.Footcube;
import io.github.divinerealms.footcube.managers.UtilManager;
import io.github.divinerealms.footcube.configs.Messages;
import lombok.Getter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class BaseCommand implements CommandExecutor {
  @Getter private final Footcube plugin;
  @Getter private final UtilManager utilManager;
  @Getter private final Messages messages;

  public BaseCommand(final Footcube plugin, final UtilManager utilManager) {
    this.plugin = plugin;
    this.utilManager = utilManager;
    this.messages = utilManager.getMessages();
  }

  @Override
  public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
    if (args.length < 1 || args[0].equalsIgnoreCase("help")) {
      final HelpCommand helpCommand = new HelpCommand(getUtilManager());
      helpCommand.onCommand(sender, command, label, args);
    } else if (args[0].equalsIgnoreCase("reloadconfig")) {
      final ReloadconfigCommand reloadconfigCommand = new ReloadconfigCommand(getUtilManager());
      reloadconfigCommand.onCommand(sender, command, label, args);
    } else if (args[0].equalsIgnoreCase("reload")) {
      final ReloadCommand reloadCommand = new ReloadCommand(getPlugin(), getUtilManager());
      reloadCommand.onCommand(sender, command, label, args);
    } else if (args[0].equalsIgnoreCase("cube")) {
      final CubeCommand cubeCommand = new CubeCommand(getUtilManager());
      cubeCommand.onCommand(sender, command, label, args);
    } else if (args[0].equalsIgnoreCase("clearcube") || args[0].equalsIgnoreCase("cc")) {
      final ClearCubeCommand clearCubeCommand = new ClearCubeCommand(getPlugin(), getUtilManager());
      clearCubeCommand.onCommand(sender, command, label, args);
    } else if (args[0].equalsIgnoreCase("commanddisabler") || args[0].equalsIgnoreCase("cd")) {
      final CommandDisabler commandDisabler = new CommandDisabler(getUtilManager());
      commandDisabler.onCommand(sender, command, label, args);
    } else getMessages().send(sender, "unknown-command");
    return true;
  }
}

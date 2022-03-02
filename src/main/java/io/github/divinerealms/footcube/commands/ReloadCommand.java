package io.github.divinerealms.footcube.commands;

import io.github.divinerealms.footcube.Footcube;
import io.github.divinerealms.footcube.managers.UtilManager;
import lombok.Getter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReloadCommand implements CommandExecutor {
  @Getter private final Footcube footcube;
  @Getter private final UtilManager utilManager;

  public ReloadCommand(final Footcube footcube, final UtilManager utilManager) {
    this.footcube = footcube;
    this.utilManager = utilManager;
  }

  @Override
  public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
    if (sender instanceof Player) {
      final Player player = (Player) sender;
      if (player.hasPermission("nfootcube.reload")) {
        getFootcube().reload();
        getUtilManager().getChatty().send(player, "RELOAD");
      } else getUtilManager().getChatty().send(player, "INSUFFICIENT_PERMISSION", "nfootcube.reload");
    } else {
      getFootcube().reload();
      getUtilManager().getLogger().info("RELOAD");
    } return true;
  }
}

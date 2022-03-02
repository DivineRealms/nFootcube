package io.github.divinerealms.footcube.commands;

import io.github.divinerealms.footcube.managers.UtilManager;
import lombok.Getter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;
import org.bukkit.plugin.Plugin;

public class ClearCubeCommand implements CommandExecutor {
  @Getter private final UtilManager utilManager;
  @Getter private final double distance;

  public ClearCubeCommand(final Plugin plugin, final UtilManager utilManager) {
    this.utilManager = utilManager;
    this.distance = plugin.getConfig().getDouble("Cube.Remove_Distance");
  }

  @Override
  public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
    if (sender instanceof Player) {
      final Player player = (Player) sender;
      if (player.hasPermission("nfootcube.clearcube")) {
        if (!getUtilManager().getPhysics().getCubes().isEmpty()) {
          for (final Slime cube : getUtilManager().getPhysics().getCubes()) {
            if (cube.getLocation().distance(player.getLocation()) <= getDistance()) {
              cube.remove();
              getUtilManager().getPhysics().getCubes().remove(cube);
              getUtilManager().getChatty().send(player, "CUBE_CLEARED");
            } else getUtilManager().getChatty().send(player, "CUBE_NOTCLEARED");
            break;
          }
        } else getUtilManager().getChatty().send(player, "CUBE_NOTCLEARED");
      } else getUtilManager().getChatty().send(player, "INSUFFICIENT_PERMISSION", "nfootcube.clearcube");
    } else getUtilManager().getLogger().info("UNKNOWN_COMMAND");
    return true;
  }
}

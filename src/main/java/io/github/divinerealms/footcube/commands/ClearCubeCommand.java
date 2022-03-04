package io.github.divinerealms.footcube.commands;

import io.github.divinerealms.footcube.managers.UtilManager;
import io.github.divinerealms.footcube.utils.Messages;
import io.github.divinerealms.footcube.utils.Physics;
import lombok.Getter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;
import org.bukkit.plugin.Plugin;

public class ClearCubeCommand implements CommandExecutor {
  @Getter private final double distance;
  @Getter private final Messages messages;
  @Getter private final Physics physics;

  public ClearCubeCommand(final Plugin plugin, final UtilManager utilManager) {
    this.distance = plugin.getConfig().getInt("cube.remove-distance");
    this.messages = utilManager.getMessages();
    this.physics = utilManager.getPhysics();
  }

  @Override
  public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
    if (!(sender instanceof Player)) getMessages().send(sender, "ingame-only");
    else {
      final Player player = (Player) sender;
      if (!player.hasPermission("nfootcube.clearcube"))
        getMessages().send(player, "insufficient-permission", "nfootcube.clearcube");
      else {
        if (getPhysics().getCubes().isEmpty()) getMessages().send(player, "cube.unable");
        else {
          for (final Slime cube : getPhysics().getCubes()) {
            final boolean isCubeClose = cube.getLocation().distance(player.getLocation()) <= getDistance();
            if (!isCubeClose) getMessages().send(player, "cube.unable");
            else {
              cube.remove();
              getPhysics().getCubes().remove(cube);
              getMessages().send(player, "cube.cleared");
            }
            break;
          }
        }
      }
    }
    return true;
  }
}

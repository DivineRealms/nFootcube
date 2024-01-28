package io.github.divinerealms.footcube.commands;

import io.github.divinerealms.footcube.configs.Messages;
import io.github.divinerealms.footcube.managers.UtilManager;
import io.github.divinerealms.footcube.utils.Logger;
import io.github.divinerealms.footcube.utils.Physics;
import lombok.Getter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;
import org.bukkit.plugin.Plugin;

@Getter
public class ClearCubeCommand implements CommandExecutor {
  private final Plugin plugin;
  private final double distance;
  private final Logger logger;
  private final Physics physics;

  public ClearCubeCommand(final Plugin plugin, final UtilManager utilManager) {
    this.plugin = plugin;
    this.distance = getPlugin().getConfig().getDouble("remove-distance");
    this.logger = utilManager.getLogger();
    this.physics = utilManager.getPhysics();
  }

  /**
   * Handles the /clearcube command to remove nearby cubes.
   *
   * @param sender  Command sender
   * @param command Command object
   * @param label   Command label
   * @param args    Command arguments
   * @return True if the command was handled successfully
   */
  @Override
  public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
    if (!(sender instanceof Player)) {
      // Command can only be used by players
      getLogger().send(sender, Messages.INGAME_ONLY.getMessage(null));
    } else {
      final Player player = (Player) sender;

      if (!player.hasPermission("nfootcube.clearcube")) {
        // Player doesn't have permission
        getLogger().send(player, Messages.INSUFFICIENT_PERMISSION.getMessage(new String[]{"nfootcube.clearcube"}));
      } else {
        if (getPhysics().getCubes().isEmpty()) {
          // No cubes to clear
          getLogger().send(player, Messages.CUBE_UNABLE.getMessage(null));
        } else {
          // Iterate through cubes and remove nearby ones
          for (final Slime cube : getPhysics().getCubes()) {
            final boolean isCubeClose = cube.getLocation().distance(player.getLocation()) <= getDistance();

            if (isCubeClose) {
              // Remove the cube
              cube.remove();
              getPhysics().getCubes().remove(cube);
              getLogger().send(player, Messages.CUBE_CLEARED.getMessage(null));
              break;  // Stop after clearing the first cube
            }
          }

          if (!getPhysics().getCubes().isEmpty()) {
            // No cubes were close to the player
            getLogger().send(player, Messages.CUBE_UNABLE.getMessage(null));
          }
        }
      }
    }
    return true;
  }
}

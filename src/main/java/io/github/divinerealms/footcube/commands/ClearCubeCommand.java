package io.github.divinerealms.footcube.commands;

import io.github.divinerealms.footcube.configs.Config;
import io.github.divinerealms.footcube.configs.Lang;
import io.github.divinerealms.footcube.managers.UtilManager;
import io.github.divinerealms.footcube.utils.Logger;
import io.github.divinerealms.footcube.utils.Physics;
import lombok.Getter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;

@Getter
public class ClearCubeCommand implements CommandExecutor {
  private final Config config;
  private final double distance;
  private final Logger logger;
  private final Physics physics;

  public ClearCubeCommand(final UtilManager utilManager) {
    this.config = utilManager.getConfig();
    this.distance = getConfig().getDouble("remove-distance");
    this.logger = utilManager.getLogger();
    this.physics = utilManager.getPhysics();
  }

  @Override
  public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
    if (!(sender instanceof Player)) getLogger().send(sender, Lang.INGAME_ONLY.getConfigValue(null));
    else {
      final Player player = (Player) sender;
      if (!player.hasPermission("nfootcube.clearcube"))
        getLogger().send(player, Lang.INSUFFICIENT_PERMISSION.getConfigValue(new String[]{"nfootcube.clearcube"}));
      else {
        if (getPhysics().getCubes().isEmpty()) getLogger().send(player, Lang.CUBE_UNABLE.getConfigValue(null));
        else {
          for (final Slime cube : getPhysics().getCubes()) {
            final boolean isCubeClose = cube.getLocation().distance(player.getLocation()) <= getDistance();
            if (!isCubeClose) getLogger().send(player, Lang.CUBE_UNABLE.getConfigValue(null));
            else {
              cube.remove();
              getPhysics().getCubes().remove(cube);
              getLogger().send(player, Lang.CUBE_CLEARED.getConfigValue(null));
            }
            break;
          }
        }
      }
    }
    return true;
  }
}

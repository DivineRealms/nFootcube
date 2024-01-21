package io.github.divinerealms.footcube.commands;

import io.github.divinerealms.footcube.configs.Lang;
import io.github.divinerealms.footcube.managers.UtilManager;
import io.github.divinerealms.footcube.utils.Cooldown;
import io.github.divinerealms.footcube.utils.Logger;
import io.github.divinerealms.footcube.utils.Physics;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;

import java.util.ArrayList;
import java.util.List;

@Getter
public class CubeCommand implements CommandExecutor {
  private final Cooldown cooldown;
  private final Logger logger;
  private final Physics physics;

  public CubeCommand(final UtilManager utilManager) {
    this.cooldown = utilManager.getCooldown();
    this.logger = utilManager.getLogger();
    this.physics = utilManager.getPhysics();
  }

  /**
   * Handles the /cube command to spawn a cube.
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
      getLogger().send(sender, Lang.INGAME_ONLY.getMessage(null));
    } else {
      final Player player = (Player) sender;

      if (!player.hasPermission("footcube.cube")) {
        // Player doesn't have permission
        getLogger().send(player, Lang.INSUFFICIENT_PERMISSION.getMessage(new String[]{"footcube.cube"}));
      } else {
        long timeLeft = 0;

        // Check if the player has permission to bypass the spawn cooldown
        if (!player.hasPermission("footcube.cube.bypass.spawn-cooldown")) {
          timeLeft = getCooldown().getTimeleft(player.getUniqueId(), getCooldown().getCubeSpawnCooldown(), true);
        }

        if (timeLeft == 0) {
          final Location location = player.getLocation().add(0.0, 1.0, 0.0);

          // Find nearby cubes
          List<Slime> cubes = new ArrayList<>();
          for (final Entity entity : location.getWorld().getNearbyEntities(location, 50, 40, 50)) {
            if (entity instanceof Slime) {
              cubes.add((Slime) entity);
            }
          }

          // Check if there are too many cubes nearby
          if (cubes.size() >= 5) {
            getLogger().send(player, Lang.TOO_MANY_CUBES.getMessage(null));
            return false;
          }

          // Spawn a cube
          getPhysics().spawnCube(location);
          getLogger().send(player, Lang.CUBE_SPAWNED.getMessage(null));
          getCooldown().setCooldown(player.getUniqueId(), System.currentTimeMillis());
        } else {
          // Player is on cooldown
          getLogger().send(player, Lang.ON_COOLDOWN.getMessage(new String[]{String.valueOf(timeLeft / 1000)}));
        }
      }
    }
    return true;
  }
}

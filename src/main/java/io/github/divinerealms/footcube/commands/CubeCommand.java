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
import org.bukkit.entity.*;

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

  @Override
  public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
    if (!(sender instanceof Player)) getLogger().send(sender, Lang.INGAME_ONLY.getConfigValue(null));
    else {
      final Player player = (Player) sender;
      if (!player.hasPermission("footcube.cube")) getLogger().send(player, Lang.INSUFFICIENT_PERMISSION.getConfigValue(new String[]{"footcube.cube"}));
      else {
        long timeLeft = 0;
        if (!player.hasPermission("footcube.cube.bypass.spawn-cooldown"))
          timeLeft = getCooldown().getTimeleft(player.getUniqueId(), getCooldown().getCubeSpawnCooldown());
        if (timeLeft == 0) {
          final Location location = player.getLocation().add(0.0, 1.0, 0.0);
          List<Slime> cubes = new ArrayList<>();
          for (final Entity entity : location.getWorld().getNearbyEntities(location, 50, 40, 50))
            if (entity instanceof Slime) cubes.add((Slime) entity);
          if (cubes.size() >= 5) {
            getLogger().send(player, Lang.TOO_MANY_CUBES.getConfigValue(null));
            return false;
          }
          getPhysics().spawnCube(location);
          getLogger().send(player, Lang.CUBE_SPAWNED.getConfigValue(null));
          getCooldown().setCooldown(player.getUniqueId(), System.currentTimeMillis());
        } else getLogger().send(player, Lang.ON_COOLDOWN.getConfigValue(new String[]{String.valueOf(timeLeft / 1000)}));
      }
    }
    return true;
  }
}

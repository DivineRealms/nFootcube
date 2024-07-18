package io.github.divinerealms.footcube.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CatchUnknown;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import io.github.divinerealms.footcube.configs.Lang;
import io.github.divinerealms.footcube.managers.UtilManager;
import io.github.divinerealms.footcube.utils.Cooldown;
import io.github.divinerealms.footcube.utils.Logger;
import io.github.divinerealms.footcube.utils.Physics;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;

import java.util.ArrayList;
import java.util.List;

@Getter
@CommandAlias("ncube|nc")
public class CubeCommand extends BaseCommand {
  private final Cooldown cooldown;
  private final Logger logger;
  private final Physics physics;

  public CubeCommand(UtilManager utilManager) {
    this.cooldown = utilManager.getCooldown();
    this.logger = utilManager.getLogger();
    this.physics = utilManager.getPhysics();
  }

  @Default
  @CatchUnknown
  @CommandPermission("footcube.cube")
  public void onCube(CommandSender sender) {
    if (!(sender instanceof Player)) {
      getLogger().send(sender, Lang.INGAME_ONLY.getConfigValue(null));
      return;
    }

    Player player = (Player) sender;
    long timeLeft = player.hasPermission("footcube.cube.bypass-cooldown") ? 0 :
        getCooldown().getTimeleft(player.getUniqueId(), getCooldown().getCubeSpawnCooldown());

    if (timeLeft != 0) {
      getLogger().send(player, Lang.ON_COOLDOWN.getConfigValue(null));
      return;
    }

    Location location = player.getLocation().add(0.0, 1.0, 0.0);
    List<Slime> cubes = new ArrayList<>();
    for (Entity entity : location.getWorld().getNearbyEntities(location, 50, 40, 50)) {
      if (entity instanceof Slime) {
        cubes.add((Slime) entity);
      }
    }

    if (cubes.size() >= 5) {
      getLogger().send(player, Lang.TOO_MANY_CUBES.getConfigValue(null));
      return;
    }

    getPhysics().spawnCube(location);
    getLogger().send(player, Lang.CUBE_SPAWNED.getConfigValue(null));
    getCooldown().setCooldown(player.getUniqueId(), System.currentTimeMillis());
  }
}

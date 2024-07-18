package io.github.divinerealms.footcube.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CatchUnknown;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import io.github.divinerealms.footcube.configs.Config;
import io.github.divinerealms.footcube.configs.Lang;
import io.github.divinerealms.footcube.managers.UtilManager;
import io.github.divinerealms.footcube.utils.Logger;
import io.github.divinerealms.footcube.utils.Physics;
import lombok.Getter;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;

@Getter
@CommandAlias("nclearcube|ncc")
public class ClearCubeCommand extends BaseCommand {
  @Getter private static final YamlConfiguration config = Config.getConfig("config.yml");
  private final double distance;
  private final Logger logger;
  private final Physics physics;

  public ClearCubeCommand(UtilManager utilManager) {
    this.distance = getConfig().getDouble("remove-distance");
    this.logger = utilManager.getLogger();
    this.physics = utilManager.getPhysics();
  }

  @Default
  @CatchUnknown
  @CommandPermission("footcube.clearcube")
  public void onClearCube(CommandSender sender) {
    if (!(sender instanceof Player)) {
      getLogger().send(sender, Lang.INGAME_ONLY.getConfigValue(null));
      return;
    }

    Player player = (Player) sender;

    if (getPhysics().getCubes().isEmpty()) {
      getLogger().send(player, Lang.CUBE_UNABLE.getConfigValue(null));
      return;
    }

    for (Slime cube : getPhysics().getCubes()) {
      boolean isCubeClose = cube.getLocation().distance(player.getLocation()) <= getDistance();
      if (!isCubeClose) {
        getLogger().send(player, Lang.CUBE_UNABLE.getConfigValue(null));
        return;
      }

      cube.remove();
      getPhysics().getCubes().remove(cube);
      getLogger().send(player, Lang.CUBE_CLEARED.getConfigValue(null));
    }
  }
}

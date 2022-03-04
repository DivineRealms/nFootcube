package io.github.divinerealms.footcube.commands;

import io.github.divinerealms.footcube.managers.UtilManager;
import io.github.divinerealms.footcube.utils.Cooldown;
import io.github.divinerealms.footcube.utils.Logger;
import io.github.divinerealms.footcube.utils.Messages;
import io.github.divinerealms.footcube.utils.Physics;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CubeCommand implements CommandExecutor {
  @Getter private final Cooldown cooldown;
  @Getter private final Logger logger;
  @Getter private final Messages messages;
  @Getter private final Physics physics;

  public CubeCommand(final UtilManager utilManager) {
    this.cooldown = utilManager.getCooldown();
    this.logger = utilManager.getLogger();
    this.messages = utilManager.getMessages();
    this.physics = utilManager.getPhysics();
  }

  @Override
  public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
    if (!(sender instanceof Player)) getLogger().send("INGAME_ONLY");
    else {
      final Player player = (Player) sender;
      if (!player.hasPermission("nfootcube.cube")) getMessages().send(player, "INSUFFICIENT_PERMISSION");
      else {
        long timeLeft = 0;
        if (!player.hasPermission("nfootcube.cube.bypass.spawn-cooldown"))
          timeLeft = getCooldown().getTimeleft(player.getUniqueId(), getCooldown().getCubeSpawnCooldown());
        if (timeLeft == 0) {
          final Location location = player.getLocation().add(0.0, 1.0, 0.0);
          getPhysics().spawnCube(location);
          getMessages().send(player, "CUBE_SPAWNED");
          getCooldown().setCooldown(player.getUniqueId(), System.currentTimeMillis());
        } else {
          final String message = getCooldown().onCooldown(timeLeft / 1000);
          player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
        }
      }
    }
    return true;
  }
}

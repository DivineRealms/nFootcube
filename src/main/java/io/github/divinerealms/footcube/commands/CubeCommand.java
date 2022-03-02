package io.github.divinerealms.footcube.commands;

import io.github.divinerealms.footcube.managers.UtilManager;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CubeCommand implements CommandExecutor {
  @Getter private final UtilManager utilManager;

  public CubeCommand(final UtilManager utilManager) {
    this.utilManager = utilManager;
  }

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    if (sender instanceof Player) {
      final Player player = (Player) sender;
      if (player.hasPermission("nfootcube.cube")) {
        long timeLeft;
        if (player.hasPermission("nfootcube.cube.bypassCooldown")) timeLeft = 0;
        else
          timeLeft = getUtilManager().getCooldown().getTimeleft(player.getUniqueId(), getUtilManager().getCooldown().getCubeSpawnCooldown());
        if (timeLeft <= 0) {
          Location loc = player.getLocation().add(0.0, 1.0, 0.0);
          getUtilManager().getPhysics().spawnCube(loc);
          getUtilManager().getChatty().send(player, "CUBE_SPAWNED");
          getUtilManager().getCooldown().setCooldown(player.getUniqueId(), System.currentTimeMillis());
        } else {
          String message = getUtilManager().getCooldown().onCooldown(timeLeft / 1000);
          player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
        }
      } else getUtilManager().getChatty().send(player, "INSUFFICIENT_PERMISSION");
    } else getUtilManager().getLogger().info("UNKNOWN_COMMAND");
    return true;
  }
}

package io.github.divinerealms.footcube.commands;

import io.github.divinerealms.footcube.utils.Configuration;
import io.github.divinerealms.footcube.utils.Manager;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;

import java.util.ArrayList;
import java.util.List;

public class BaseCommand implements CommandExecutor, TabCompleter {
  private final Manager manager;
  private final double distance;
  private final String prefix;

  public BaseCommand(final Manager manager, final Configuration configuration) {
    this.manager = manager;
    this.distance = this.manager.getPlugin().getConfig().getDouble("Cube.Remove_Distance");
    this.prefix = configuration.get("messages.yml").getString("PREFIX");
  }

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    if (sender instanceof Player) {
      final Player player = (Player) sender;

      if (command.getName().equalsIgnoreCase("nfootcube")) {
        if (args.length == 0) {
          this.manager.getMessage().sendLong(player, "HELP");
          return true;
        }

        switch (args[0].toLowerCase()) {
          case "reload":
            if (player.hasPermission("nfootcube.reload")) {
              this.manager.getPlugin().reload();
              this.manager.getMessage().send(player, "RELOAD");
            } else this.manager.getMessage().send(player, "INSUFFICIENT_PERMISSION", "nfootcube.reload");
            break;
          case "cube":
            if (player.hasPermission("nfootcube.cube")) {
              long timeLeft;
              if (player.hasPermission("nfootcube.cube.bypassCooldown")) timeLeft = 0;
              else
                timeLeft = this.manager.getCooldown().getTimeleft(player.getUniqueId(), this.manager.getCooldown().getCubeSpawnCooldown());
              if (timeLeft <= 0) {
                Location loc = player.getLocation().add(0.0, 1.0, 0.0);
                this.manager.getController().spawnCube(loc);
                this.manager.getMessage().send(player, "CUBE_SPAWNED");
                this.manager.getCooldown().setCooldown(player.getUniqueId(), System.currentTimeMillis());
              } else {
                String message = this.manager.getCooldown().onCooldown(timeLeft / 1000).replace("%prefix%", this.prefix);
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
              }
            } else this.manager.getMessage().send(player, "INSUFFICIENT_PERMISSION");
            break;
          case "clearcube":
            if (player.hasPermission("nfootcube.clearcube")) {
              if (!this.manager.getController().cubes.isEmpty()) {
                for (final Slime cube : this.manager.getController().cubes) {
                  if (cube.getLocation().distance(player.getLocation()) <= this.distance) {
                    cube.remove();
                    this.manager.getController().cubes.remove(cube);
                    this.manager.getMessage().send(player, "CUBE_CLEARED");
                  } else this.manager.getMessage().send(player, "CUBE_NOTCLEARED");
                  break;
                }
              } else this.manager.getMessage().send(player, "CUBE_NOTCLEARED");
            } else this.manager.getMessage().send(player, "INSUFFICIENT_PERMISSION", "nfootcube.clearcube");
            break;
          default:
            this.manager.getMessage().send(player, "UNKNOWN_COMMAND");
            break;
        }
        return true;
      }
    } else {
      if (command.getName().equalsIgnoreCase("nfootcube")) {
        if (args.length == 0) {
          this.manager.getLogger().sendLong("HELP");
          return false;
        }

        if (args[0].equalsIgnoreCase("reload")) {
          this.manager.getPlugin().reload();
          this.manager.getLogger().info("RELOAD");
        } else this.manager.getLogger().sendLong("UNKNOWN_COMMAND");
      }
      return true;
    }

    return false;
  }

  @Override
  public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
    if (command.getName().equalsIgnoreCase("nfootcube")) {
      List<String> list = new ArrayList<>();

      if (args.length == 1) {
        if (sender.hasPermission("nfootcube.admin")) {
          list.add("reload");
          list.add("cube");
          list.add("clearcube");
        }
      }
      return list;
    }
    return null;
  }
}

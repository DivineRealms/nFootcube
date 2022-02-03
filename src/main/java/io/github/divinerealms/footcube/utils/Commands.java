package io.github.divinerealms.footcube.utils;

import io.github.divinerealms.footcube.Footcube;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;

public class Commands implements CommandExecutor, TabCompleter {
  private final Footcube plugin;
  private final Manager manager;
  private final Configuration configuration;

  public Commands(Footcube plugin, Manager manager, Configuration configuration) {
    this.plugin = plugin;
    this.manager = manager;
    this.configuration = configuration;
  }

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    if (sender instanceof Player) {
      Player player = (Player) sender;

      if (command.getName().equalsIgnoreCase("nfootcube")) {
        if (args.length == 1) {
          if (args[0].equalsIgnoreCase("reload")) {
            if (player.hasPermission("nfootcube.reload")) {
              this.plugin.reload();
              this.sendMessage(player, "RELOAD", "", 0);
            } else this.sendMessage(player, "INSUFFICIENT_PERMISSION", "nfootcube.reload", 0);
          } else if (args[0].equalsIgnoreCase("help")) this.sendMessage(player, "HELP", "", 1);
          else this.sendMessage(player, "UNKNOWN_COMMAND", "", 0);
        } else this.sendMessage(player, "HELP", "", 1);
        return true;
      }

      if (command.getName().equalsIgnoreCase("cube")) {
        if (player.hasPermission("nfootcube.cube")) {
          Location loc = player.getLocation().add(0.0, 1.5, 0.0);
          if (this.plugin.getController().immuneMap.containsKey(player)) {
            Bukkit.getScheduler().cancelTask(this.plugin.getController().immuneMap.get(player).getTaskId());
            this.plugin.getController().immuneMap.remove(player);
            this.plugin.getController().immune.remove(player);
          }

          this.plugin.getController().immune.add(player);

          BukkitTask taskID = Bukkit.getScheduler().runTaskLater(this.plugin, () -> {
            this.plugin.getController().immune.remove(player);
            this.plugin.getController().immuneMap.remove(player);
          }, 60L);

          this.plugin.getController().immuneMap.put(player, taskID);
          this.plugin.getController().spawnCube(loc);
          this.sendMessage(player, "CUBE_SPAWNED", "", 0);
        } else this.sendMessage(player, "INSUFFICIENT_PERMISSION", "nfootcube.cube", 0);
        return true;
      }

      if (command.getName().equalsIgnoreCase("clearcube")) {
        if (player.hasPermission("nfootcube.clearcube")) {
          final double distance = this.plugin.getConfig().getDouble("distance");
          if (!this.plugin.getController().cubes.isEmpty()) {
            for (Slime cube : this.plugin.getController().cubes) {
              if (this.plugin.getController().getDistance(cube.getLocation(), player.getLocation()) < distance) {
                cube.setHealth(0.0D);
                this.plugin.getController().cubes.remove(cube);
                this.sendMessage(player, "CUBE_CLEARED", "", 0);
                break;
              } else this.sendMessage(player, "CUBE_NOTCLEARED", "", 0);
            }
          } else this.sendMessage(player, "CUBE_NOTCLEARED", "", 0);
        } else this.sendMessage(player, "INSUFFICIENT_PERMISSION", "nfootcube.clearcube", 0);
      }
     } else {
      if (command.getName().equalsIgnoreCase("nfootcube")) {
        if (args.length == 1) {
          if (args[0].equalsIgnoreCase("reload")) {
            this.plugin.reload();
            this.manager.getLogger().log("RELOAD");
          } else this.manager.getLogger().log("UNKNOWN_COMMAND");
        } else this.manager.getLogger().logL("HELP");
      }
      return true;
    }

    return false;
  }

  private void sendMessage(Player player, String path, String permission, Integer type) {
    switch (type) {
      case 0:
        if (permission.equals("")) player.sendMessage(this.manager.color(path));
        else player.sendMessage(this.manager.color(path, permission));
        break;
      case 1:
        List<String> list = this.configuration.get().getStringList(path);

        if (permission.equals("")) for (String message : list)
          player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
        else for (String message : list) player.sendMessage(this.manager.color(message, permission));
        break;
    }
  }

  @Override
  public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
    if (command.getName().equalsIgnoreCase("nfootcube")) {
      List<String> list = new ArrayList<>();

      if (args.length == 1) {
        if (sender.hasPermission("nfootcube.admin")) {
          list.add("help");
          list.add("reload");
        } else {
          list.add("help");
        }
      }
      return list;
    }
    return null;
  }
}

import io.github.divinerealms.footcube.Footcube;
import io.github.divinerealms.footcube.listeners.Organization;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;

public class DisableCommands {

  private Footcube plugin;
  private Organization organization;
  private ArrayList<String> commands;

  public DisableCommands(final Footcube pl, final Organization org) {
    this.commands = new ArrayList<String>();
    this.plugin = pl;
    this.organization = org;
    this.plugin.getServer().getPluginManager().registerEvents((Listener)this, (Plugin)this.plugin);
    final FileConfiguration cfg = this.plugin.getConfig();
    cfg.addDefault("enabledCommands", (Object)"");
    this.plugin.saveConfig();
    String[] split;
    for (int length = (split = cfg.getString("enabledCommands").split(" ")).length, i = 0; i < length; ++i) {
      final String s = split[i];
      this.commands.add(s);
    }
  }

  public void command(final CommandSender sender, final Command cmd, final String c, final String[] args) {
    final Player p = (Player)sender;
    if (cmd.getName().equalsIgnoreCase("commandDisabler") && p.hasPermission("footcube.admin")) {
      if (args.length < 1) {
        p.sendMessage(this.organization.pluginString + "Lista komandi za CommandDisabler");
        p.sendMessage("/cd add (komanda)");
        p.sendMessage("/cd remove (komanda)");
        p.sendMessage("/cd list");
      }
      else {
        final FileConfiguration cfg = this.plugin.getConfig();
        if (args[0].equalsIgnoreCase("add")) {
          if (args.length < 2) {
            p.sendMessage(this.organization.pluginString + "/cd add (komanda)");
          }
          else if (this.commands.contains(args[1])) {
            p.sendMessage(this.organization.pluginString + "Komanda je vec dodata na listu.");
          }
          else {
            this.commands.add(args[1]);
            String cfgString = "";
            for (final String s : this.commands) {
              cfgString = cfgString + s + " ";
            }
            cfg.set("enabledCommands", (Object)cfgString);
            this.plugin.saveConfig();
            p.sendMessage(this.organization.pluginString + "Uspe\u0161no ste dodali komandu " + ChatColor.AQUA + "/" + args[1] + ChatColor.WHITE + " na listu zabranjenih komandi.");
          }
        }
        else if (args[0].equalsIgnoreCase("remove")) {
          if (args.length < 2) {
            p.sendMessage(this.organization.pluginString + "/cd remove (komanda)");
          }
          else if (this.commands.contains(args[1])) {
            this.commands.remove(args[1]);
            String cfgString = "";
            for (final String s : this.commands) {
              cfgString = cfgString + s + " ";
            }
            cfg.set("enabledCommands", (Object)cfgString);
            this.plugin.saveConfig();
            p.sendMessage(this.organization.pluginString + "Uspe\u0161no ste uklonili komandu " + ChatColor.AQUA + "/" + args[1] + ChatColor.WHITE + " sa liste zabranjenih komandi.");
          }
          else {
            p.sendMessage(this.organization.pluginString + "Ova komanda nije dodata na listu zabranjenih komandi.");
          }
        }
        else if (args[0].equalsIgnoreCase("list")) {
          p.sendMessage(this.organization.pluginString + "Lista zabranjenih komandi:");
          for (final String s2 : this.commands) {
            p.sendMessage(ChatColor.WHITE + s2);
          }
        }
      }
    }
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onPreprocess(final PlayerCommandPreprocessEvent e) {
    final Player p = e.getPlayer();
    if (!p.hasPermission("footcube.admin") && (this.organization.playingPlayers.contains(p.getName()) || this.organization.waitingPlayers.containsKey(p.getName()))) {
      final String cmd = e.getMessage().substring(1).split(" ")[0];
      boolean allowed = true;
      for (final String command : this.commands) {
        if (cmd.equalsIgnoreCase(command)) {
          allowed = false;
          break;
        }
      }
      if (!allowed) {
        p.sendMessage(this.organization.pluginString + "Ne mozete koristiti ovu komandu tokom utakmice.");
        e.setCancelled(true);
      }
    }
  }
}

package io.github.divinerealms.footcube.features;

import io.github.divinerealms.footcube.Footcube;
import io.github.divinerealms.footcube.core.Organization;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.ArrayList;

public class DisableCommands implements Listener {
  private final Organization organization;
  public ArrayList<String> commands;

  public DisableCommands(final Footcube pl, final Organization org) {
    this.commands = new ArrayList<>();
    this.organization = org;
    pl.getServer().getPluginManager().registerEvents(this, pl);
    final FileConfiguration cfg = pl.getConfig();
    cfg.addDefault("enabledCommands", "");
    pl.saveConfig();
    String[] split;
    for (int length = (split = cfg.getString("enabledCommands").split(" ")).length, i = 0; i < length; ++i) {
      final String s = split[i];
      this.commands.add(s);
    }
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onPreprocess(final PlayerCommandPreprocessEvent e) {
    final Player p = e.getPlayer();
    if (!p.hasPermission("footcube.admin") && (this.organization.playingPlayers.contains(p.getName()) || this.organization.waitingPlayers.containsKey(p.getName()))) {
      final String cmd = e.getMessage().substring(1).split(" ")[0];
      boolean allowed = false;
      for (final String command : this.commands) {
        if (cmd.equalsIgnoreCase(command)) {
          allowed = true;
          break;
        }
      }
      if (!allowed) {
        p.sendMessage(this.organization.pluginString + "You can't use that command in the match.");
        e.setCancelled(true);
      }
    }
  }
}

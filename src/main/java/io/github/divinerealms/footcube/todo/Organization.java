package io.github.divinerealms.footcube.todo;

import io.github.divinerealms.footcube.managers.UtilManager;
import io.github.divinerealms.footcube.utils.Logger;
import lombok.Getter;
import lombok.Setter;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.ArrayList;
import java.util.Collection;

public class Organization {
  @Getter private final Plugin plugin;
  @Getter private final Logger logger;
  @Getter private final int setupType = 0, lobby1v1 = 0, lobby2v2 = 0, lobby3v3 = 0, lobby4v4 = 0;
  /*@Getter private final Location setupLocation;
  @Getter private final ArrayList<Slime> practiceCubes;
  @Getter private final HashMap<Player, Player> firstTeam, secondTeam;*/
  @Getter @Setter private static Economy econ = null;
  @Getter @Setter private Matches[] matches1v1, matches2v2, matches3v3, matches4v4;

  public Organization(final Plugin plugin, final UtilManager utilManager) {
    this.plugin = plugin;
    this.logger = utilManager.getLogger();
    if (!setupEconomy()) getLogger().info("&cVault not found, plugin won't use economy.");
    this.matches1v1 = new ArrayList<Player>().toArray(new Matches[0]);
    this.matches2v2 = new ArrayList<Player>().toArray(new Matches[0]);
    this.matches3v3 = new ArrayList<Player>().toArray(new Matches[0]);
    this.matches4v4 = new ArrayList<Player>().toArray(new Matches[0]);
  }

  /*private void removeTeam(final Player player) {
    if (getFirstTeam().containsKey(player)) {

    } else if (getSecondTeam().containsKey(player)) {

    }
  }*/

  public boolean isPlayerOnline(final Player player) {
    final Collection<? extends Player> onlinePlayers = getPlugin().getServer().getOnlinePlayers();
    return onlinePlayers.contains(player);
  }

  private void addArena(final int type, final Location firstTeam, final Location secondTeam) {
    final Location center = firstTeam.add(firstTeam.subtract(secondTeam).multiply(0.5));
    if (type == 1) {}
    else if (type == 2) {}
    else if (type == 3) {}
    else if (type == 4) {}
  }

  private void loadArenas() {

  }

  private boolean setupEconomy() {
    final Server server = getPlugin().getServer();
    if (server.getPluginManager().getPlugin("Vault") == null) return false;
    RegisteredServiceProvider<Economy> rsp = server.getServicesManager().getRegistration(Economy.class);
    if (rsp == null) return false;
    setEcon(rsp.getProvider());
    return getEcon() != null;
  }
}

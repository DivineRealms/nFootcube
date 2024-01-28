package io.github.divinerealms.footcube.listeners;

import io.github.divinerealms.footcube.Footcube;
import io.github.divinerealms.footcube.configs.Messages;
import io.github.divinerealms.footcube.core.Organization;
import io.github.divinerealms.footcube.managers.UtilManager;
import io.github.divinerealms.footcube.utils.Logger;
import io.github.divinerealms.footcube.utils.Physics;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

@Getter
public class PlayerQuitListener implements Listener {
  private final Footcube plugin;
  private final Physics physics;
  private final Logger logger;
  private final Organization organization;

  public PlayerQuitListener(final Footcube plugin, final UtilManager utilManager) {
    this.plugin = plugin;
    this.physics = utilManager.getPhysics();
    this.logger = utilManager.getLogger();
    this.organization = new Organization(plugin, utilManager);
  }

  @EventHandler
  public void onQuit(final PlayerQuitEvent event) {
    final Player p = event.getPlayer();
    getOrganization().waitingPlayers.remove(p.getName());
    getOrganization().playingPlayers.remove(p.getName());
    if (getOrganization().team.containsKey(p)) {
      final Player player = getOrganization().team.get(p);
      getLogger().send(player, Messages.REQUEST_DENY.getMessage(new String[]{p.getName()}));
      getOrganization().teamType.remove(player);
      getOrganization().teamReverse.remove(player);
      getOrganization().team.remove(p);
    }
    else if (getOrganization().teamReverse.containsKey(p)) {
      final Player player = getOrganization().teamReverse.get(p);
      getOrganization().teamType.remove(p);
      getOrganization().teamReverse.remove(p);
      getOrganization().team.remove(player);
    }
    else if (getOrganization().waitingTeamPlayers.contains(p)) {
      for (int i = 0; i < getOrganization().waitingTeams.length; ++i) {
        if (getOrganization().waitingTeams[i][0] == p) {
          final Player player2 = getOrganization().waitingTeams[i][1];
          getLogger().send(player2, Messages.TEAMMATE_LEFT.getMessage(null));
          getOrganization().waitingTeams = getOrganization().reduceArray(getOrganization().waitingTeams, p);
          getOrganization().waitingTeamPlayers.remove(p);
          getOrganization().waitingTeamPlayers.remove(player2);
        }
        else if (getOrganization().waitingTeams[i][1] == p) {
          final Player player2 = getOrganization().waitingTeams[i][0];
          getLogger().send(player2, Messages.TEAMMATE_LEFT.getMessage(null));
          getOrganization().waitingTeams = getOrganization().reduceArray(getOrganization().waitingTeams, p);
          getOrganization().waitingTeamPlayers.remove(p);
          getOrganization().waitingTeamPlayers.remove(player2);
        }
      }
    }
    // Remove player locations and charges when they quit
    getPhysics().getLastLocations().remove(event.getPlayer().getUniqueId());
    getPhysics().getCharges().remove(event.getPlayer().getUniqueId());
  }
}

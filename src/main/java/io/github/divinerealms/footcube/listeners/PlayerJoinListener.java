package io.github.divinerealms.footcube.listeners;

import io.github.divinerealms.footcube.Footcube;
import io.github.divinerealms.footcube.configs.Messages;
import io.github.divinerealms.footcube.core.Organization;
import io.github.divinerealms.footcube.managers.PlayerDataManager;
import io.github.divinerealms.footcube.managers.UtilManager;
import io.github.divinerealms.footcube.utils.Logger;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

@Getter
public class PlayerJoinListener implements Listener {
  private final Footcube plugin;
  private final Logger logger;
  private final Organization organization;

  public PlayerJoinListener(final Footcube plugin, final UtilManager utilManager) {
    this.plugin = plugin;
    this.logger = utilManager.getLogger();
    this.organization = new Organization(plugin, utilManager);
  }

  @EventHandler
  public void onPlayerJoin(final PlayerJoinEvent playerJoinEvent) {
    // Get the player who joined
    final Player player = playerJoinEvent.getPlayer();
    getOrganization().clearInventory(player);

    // Create or load the player's data
    PlayerDataManager playerData = new PlayerDataManager(getPlugin(), player.getUniqueId());

    // If player data file already exists, do nothing
    if (playerData.getFile().exists()) {
      return;
    }

    // Set the player name in the data file
    playerData.setString("player-name", player.getName());

    // Save the player data
    playerData.savePlayerData(player.getUniqueId());

    // Send a welcome message to the player
    getLogger().send("helper", Messages.CREATED_PLAYER_DATA.getMessage(new String[]{player.getName()}));
  }
}

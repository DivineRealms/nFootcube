package io.github.divinerealms.footcube.listeners;

import io.github.divinerealms.footcube.managers.DataManager;
import io.github.divinerealms.footcube.managers.UtilManager;
import io.github.divinerealms.footcube.utils.Logger;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.UUID;

@Getter
public class PlayerJoinListener implements Listener {
  private final UtilManager utilManager;
  private final DataManager dataManager;
  private final Logger logger;

  public PlayerJoinListener(UtilManager utilManager) {
    this.utilManager = utilManager;
    this.dataManager = new DataManager(utilManager.getPlugin());
    this.logger = utilManager.getLogger();
  }

  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent event) {
    Player player = event.getPlayer();
    String playerName = player.getName(), folderName = "playerdata";
    UUID playerUUID = player.getUniqueId();

    if (!getDataManager().configExists(folderName, playerUUID.toString())) {
      getDataManager().setFolderName(folderName);
      getDataManager().createNewFile(playerUUID.toString(), null);
      getLogger().info("Creating playerdata file for &b" + playerName + " (&o" + playerUUID + "&b)");
    }

    getDataManager().setConfig(folderName, playerUUID.toString());
    getDataManager().getConfig(playerUUID.toString()).set("name", playerName);

    if (getDataManager().getPlayerName(playerUUID) == null) {
      getDataManager().addPlayerUUID(playerUUID, playerName);
    }

    getDataManager().saveConfig(playerUUID.toString());
  }
}

package io.github.divinerealms.footcube.listeners;

import io.github.divinerealms.footcube.configs.Lang;
import io.github.divinerealms.footcube.managers.PlayerDataManager;
import io.github.divinerealms.footcube.managers.UtilManager;
import io.github.divinerealms.footcube.utils.Logger;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;

@Getter
public class PlayerJoinListener implements Listener {
  private final Plugin plugin;
  private final Logger logger;

  public PlayerJoinListener(final Plugin plugin, final UtilManager utilManager) {
    this.plugin = plugin;
    this.logger = utilManager.getLogger();
  }

  @EventHandler
  public void onPlayerJoin(final PlayerJoinEvent playerJoinEvent) {
    final Player player = playerJoinEvent.getPlayer();
    PlayerDataManager playerData = new PlayerDataManager(getPlugin(), player.getUniqueId());
    if (playerData.getFile().exists()) return;
    playerData.setString("player-name", player.getName());
    playerData.savePlayerData(player.getUniqueId());
    getLogger().send("helper", Lang.CREATED_PLAYER_DATA.getConfigValue(new String[]{player.getName()}));
  }
}

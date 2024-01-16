package io.github.divinerealms.footcube.listeners;

import io.github.divinerealms.footcube.managers.PlayerDataManager;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;

import java.util.UUID;

@Getter
public class PlayerJoinListener implements Listener {
  private final Plugin plugin;
  @Setter private PlayerDataManager playerDataManager;
  @Setter private UUID playerID;

  public PlayerJoinListener(final Plugin plugin) {
    this.plugin = plugin;
  }

  @EventHandler
  public void onPlayerJoin(final PlayerJoinEvent playerJoinEvent) {
    setPlayerID(playerJoinEvent.getPlayer().getUniqueId());
    setPlayerDataManager(new PlayerDataManager(getPlugin(), getPlayerID()));
  }
}

package io.github.divinerealms.footcube.listeners;

import io.github.divinerealms.footcube.managers.PlayerDataManager;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;

import java.util.UUID;

public class PlayerJoinListener implements Listener {
  @Getter private final Plugin plugin;
  @Getter @Setter private PlayerDataManager playerDataManager;
  @Getter @Setter private UUID playerID;

  public PlayerJoinListener(final Plugin plugin) {
    this.plugin = plugin;
  }

  @EventHandler
  public void onPlayerJoin(final PlayerJoinEvent playerJoinEvent) {
    setPlayerID(playerJoinEvent.getPlayer().getUniqueId());
    setPlayerDataManager(new PlayerDataManager(getPlugin(), getPlayerID()));
  }
}
